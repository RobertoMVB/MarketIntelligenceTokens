package com.hypr.marketIntelligenceTokens.analysis.byJson;

import com.hypr.marketIntelligenceTokens.dto.transaction.ItemDTO;
import com.hypr.marketIntelligenceTokens.dto.transaction.TransactionDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkuConcentrationAnalyzerJson {

    public static void analyze(List<TransactionDTO> dataset, Path outputDir) throws IOException {

        List<ItemDTO> allItems = dataset.stream()
                .filter(t -> t.getItems() != null)
                .flatMap(t -> t.getItems().stream())
                .filter(i ->
                        i.getCode() != null &&
                                !i.getCode().isBlank() &&
                                i.getItemValue() > 0
                )
                .toList();

        if (allItems.isEmpty()) {
            System.out.println("Nenhum item válido para análise de SKU.");
            return;
        }

        BigDecimal totalGmv = allItems.stream()
                .map(i -> BigDecimal.valueOf(i.getItemValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> gmvBySku =
                allItems.stream()
                        .collect(Collectors.groupingBy(
                                ItemDTO::getCode,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        i -> BigDecimal.valueOf(i.getItemValue()),
                                        BigDecimal::add
                                )
                        ));

        List<Map.Entry<String, BigDecimal>> orderedSkus =
                gmvBySku.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

        BigDecimal cumulative = BigDecimal.ZERO;
        int skuCount80 = 0;

        for (Map.Entry<String, BigDecimal> entry : orderedSkus) {
            cumulative = cumulative.add(entry.getValue());
            skuCount80++;

            if (cumulative
                    .divide(totalGmv, 4, RoundingMode.HALF_UP)
                    .compareTo(new BigDecimal("0.80")) >= 0) {
                break;
            }
        }

        int totalSkus = orderedSkus.size();

        BigDecimal skuShare = BigDecimal.valueOf(skuCount80)
                .divide(BigDecimal.valueOf(totalSkus), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // LOG
        System.out.println("=== Análise de Concentração de SKU (Pareto 80/20) ===");
        System.out.println("SKUs totais: " + totalSkus);
        System.out.println("SKUs responsáveis por ~80% do GMV: " + skuCount80);
        System.out.println("Percentual do catálogo: " +
                skuShare.setScale(2, RoundingMode.HALF_UP) + "%");

        // JSON
        String json = """
        {
          "totalSkus": %d,
          "skusFor80PercentGmv": %d,
          "catalogSharePercent": %.2f
        }
        """.formatted(
                totalSkus,
                skuCount80,
                skuShare.setScale(2, RoundingMode.HALF_UP)
        );

        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve("sku_concentration.json"), json);
    }

    private SkuConcentrationAnalyzerJson() {}
}
