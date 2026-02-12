package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailyBrandOverviewAnalyzer {

    public static void analyze(List<TransactionModel> dataset, Path outputDir) throws IOException {

        Map<LocalDate, List<TransactionModel>> byDate =
                dataset.stream()
                        .collect(Collectors.groupingBy(
                                t -> t.getTimestamp().toLocalDate()
                        ));

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        int dayIndex = 0;
        for (var dayEntry : byDate.entrySet()) {
            LocalDate date = dayEntry.getKey();
            List<TransactionModel> dayTxs = dayEntry.getValue();

            json.append("  \"").append(date).append("\": {\n");

            json.append("    \"totalTransactions\": ").append(dayTxs.size()).append(",\n");
            json.append("    \"completeTransactions\": ")
                    .append(dayTxs.stream().filter(TransactionModel::isComplete).count()).append(",\n");
            json.append("    \"incompleteTransactions\": ")
                    .append(dayTxs.stream().filter(t -> !t.isComplete()).count()).append(",\n");

            Map<Integer, List<TransactionModel>> byBrand =
                    dayTxs.stream()
                            .filter(t -> t.getBrand() != null)
                            .collect(Collectors.groupingBy(TransactionModel::getBrand));

            json.append("    \"brands\": {\n");

            int brandIndex = 0;
            for (var brandEntry : byBrand.entrySet()) {
                Integer brand = brandEntry.getKey();
                List<TransactionModel> brandTxs = brandEntry.getValue();

                BigDecimal gmv = brandTxs.stream()
                        .map(TransactionModel::getGmv)
                        .filter(v -> v != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                int quantity = brandTxs.stream()
                        .map(TransactionModel::getQuantity)
                        .filter(v -> v != null)
                        .reduce(0, Integer::sum);

                json.append("      \"").append(brand).append("\": {\n")
                        .append("        \"transactions\": ").append(brandTxs.size()).append(",\n")
                        .append("        \"quantity\": ").append(quantity).append(",\n")
                        .append("        \"gmv\": ").append(gmv.setScale(2, RoundingMode.HALF_UP)).append("\n")
                        .append("      }");

                if (++brandIndex < byBrand.size()) json.append(",");
                json.append("\n");
            }

            json.append("    }\n");
            json.append("  }");

            if (++dayIndex < byDate.size()) json.append(",");
            json.append("\n");
        }

        json.append("}\n");

        Files.createDirectories(outputDir);
        Files.writeString(
                outputDir.resolve("daily_brand_overview.json"),
                json.toString()
        );
    }

    private DailyBrandOverviewAnalyzer() {}
}
