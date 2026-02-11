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

public class BrandGmvAnalyzer {

    public static void analyze(List<TransactionModel> dataset, Path outputDir) throws IOException {
        if (dataset.isEmpty()) {
            System.out.println("Dataset vazio.");
            return;
        }

        BigDecimal totalGmv = dataset.stream()
                .map(TransactionModel::getGmv)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Integer, BigDecimal> gmvByBrand = dataset.stream()
                .collect(Collectors.groupingBy(
                        TransactionModel::getBrand,
                        Collectors.mapping(
                                TransactionModel::getGmv,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        List<Map.Entry<Integer, BigDecimal>> orderedBrands =
                gmvByBrand.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

        // LOG
        System.out.println("=== GMV por Brand ===");

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"brands\": [\n");

        for (int i = 0; i < orderedBrands.size(); i++) {
            Map.Entry<Integer, BigDecimal> entry = orderedBrands.get(i);

            BigDecimal share = entry.getValue()
                    .divide(totalGmv, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));

            System.out.println(
                    "Brand " + entry.getKey() +
                            " | GMV: " + entry.getValue().setScale(2, RoundingMode.HALF_UP) +
                            " | Share: " + share.setScale(2, RoundingMode.HALF_UP) + "%"
            );

            json.append("    {\n")
                    .append("      \"brand\": ").append(entry.getKey()).append(",\n")
                    .append("      \"gmv\": ").append(entry.getValue().setScale(2, RoundingMode.HALF_UP)).append(",\n")
                    .append("      \"sharePercent\": ").append(share.setScale(2, RoundingMode.HALF_UP)).append("\n")
                    .append("    }");

            if (i < orderedBrands.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n}");

        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve("brand_gmv.json"), json.toString());
    }

    private BrandGmvAnalyzer() {}
}
