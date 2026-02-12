package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkuConcentrationAnalyzer {

    public static void analyze(List<TransactionModel> dataset, Path outputDir) throws IOException {

        List<TransactionModel> completeTransactions = dataset.stream()
                .filter(TransactionModel::isComplete)
                .toList();

        if (completeTransactions.isEmpty()) {
            System.out.println("Nenhuma transação completa para análise de SKU.");
            return;
        }

        BigDecimal totalGmv = completeTransactions.stream()
                .map(TransactionModel::getGmv)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, BigDecimal> gmvBySku = completeTransactions.stream()
                .collect(Collectors.groupingBy(
                        TransactionModel::getSku,
                        Collectors.mapping(
                                TransactionModel::getGmv,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        List<Map.Entry<Long, BigDecimal>> orderedSkus =
                gmvBySku.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

        BigDecimal cumulative = BigDecimal.ZERO;
        int skuCount80 = 0;

        for (Map.Entry<Long, BigDecimal> entry : orderedSkus) {
            cumulative = cumulative.add(entry.getValue());
            skuCount80++;

            if (cumulative.divide(totalGmv, 4, RoundingMode.HALF_UP)
                    .compareTo(new BigDecimal("0.80")) >= 0) {
                break;
            }
        }

        int totalSkus = orderedSkus.size();

        BigDecimal skuShare = new BigDecimal(skuCount80)
                .divide(new BigDecimal(totalSkus), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        // LOG
        System.out.println("=== Análise de Concentração (Pareto 80/20) ===");
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

    private SkuConcentrationAnalyzer() {}
}
