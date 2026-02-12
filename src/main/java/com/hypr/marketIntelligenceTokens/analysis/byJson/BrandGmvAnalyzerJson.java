package com.hypr.marketIntelligenceTokens.analysis.byJson;

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

public class BrandGmvAnalyzerJson {

    public static void analyze(List<TransactionDTO> dataset, Path outputDir) throws IOException {

        List<TransactionDTO> validDataset = dataset.stream()
                .filter(t ->
                        t.getIssuer() != null &&
                                t.getIssuer().getLegalName() != null &&
                                t.getTotals() != null
                )
                .toList();

        if (validDataset.isEmpty()) {
            System.out.println("Dataset vazio ou sem GMV válido.");
            return;
        }

        BigDecimal totalGmv = validDataset.stream()
                .map(t -> BigDecimal.valueOf(t.getTotals().getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> gmvByBrand =
                validDataset.stream()
                        .collect(Collectors.groupingBy(
                                t -> t.getIssuer().getLegalName(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        t -> BigDecimal.valueOf(t.getTotals().getTotal()),
                                        BigDecimal::add
                                )
                        ));

        List<Map.Entry<String, BigDecimal>> orderedBrands =
                gmvByBrand.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .toList();

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"brands\": [\n");

        for (int i = 0; i < orderedBrands.size(); i++) {
            Map.Entry<String, BigDecimal> entry = orderedBrands.get(i);

            BigDecimal share = entry.getValue()
                    .divide(totalGmv, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            json.append("    {\n")
                    .append("      \"brand\": \"").append(entry.getKey()).append("\",\n")
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

    private BrandGmvAnalyzerJson() {}
}
