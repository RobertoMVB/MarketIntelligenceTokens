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
        List<TransactionModel> validDataset = dataset.stream()
                .filter(t -> t.getGmv() != null)
                .toList();

        if (validDataset.isEmpty()) {
            System.out.println("Dataset vazio ou sem GMV válido.");
            return;
        }

        BigDecimal totalGmv = validDataset.stream()
                .map(TransactionModel::getGmv)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Integer, BigDecimal> gmvByBrand = validDataset.stream()
                .collect(Collectors.groupingBy(
                        TransactionModel::getBrand,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                TransactionModel::getGmv,
                                BigDecimal::add
                        )
                ));

        List<Map.Entry<Integer, BigDecimal>> orderedBrands =
                gmvByBrand.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

        System.out.println("=== GMV por Brand ===");

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"brands\": [\n");

        for (int i = 0; i < orderedBrands.size(); i++) {
            Map.Entry<Integer, BigDecimal> entry = orderedBrands.get(i);

            BigDecimal share = entry.getValue()
                    .divide(totalGmv, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

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
