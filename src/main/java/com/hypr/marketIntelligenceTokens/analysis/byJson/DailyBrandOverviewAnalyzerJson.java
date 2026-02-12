package com.hypr.marketIntelligenceTokens.analysis.byJson;

import com.hypr.marketIntelligenceTokens.dto.transaction.TransactionDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailyBrandOverviewAnalyzerJson {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void analyze(
            List<TransactionDTO> dataset,
            Path outputDir
    ) throws IOException {

        // Agrupa por data de emissão do cupom
        Map<LocalDate, List<TransactionDTO>> byDate =
                dataset.stream()
                        .filter(t ->
                                t.getReceipt() != null &&
                                        t.getReceipt().getIssueDate() != null
                        )
                        .collect(Collectors.groupingBy(
                                t -> LocalDate.parse(
                                        t.getReceipt().getIssueDate(),
                                        DATE_FORMAT
                                )
                        ));

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        int dayIndex = 0;
        for (var dayEntry : byDate.entrySet()) {
            LocalDate date = dayEntry.getKey();
            List<TransactionDTO> dayTxs = dayEntry.getValue();

            json.append("  \"").append(date).append("\": {\n");
            json.append("    \"totalTransactions\": ")
                    .append(dayTxs.size()).append(",\n");

            // Agrupa por brand (razão social do emitente)
            Map<String, List<TransactionDTO>> byBrand =
                    dayTxs.stream()
                            .filter(t ->
                                    t.getIssuer() != null &&
                                            t.getIssuer().getLegalName() != null
                            )
                            .collect(Collectors.groupingBy(
                                    t -> t.getIssuer().getLegalName()
                            ));

            json.append("    \"brands\": {\n");

            int brandIndex = 0;
            for (var brandEntry : byBrand.entrySet()) {
                String brand = brandEntry.getKey();
                List<TransactionDTO> brandTxs = brandEntry.getValue();

                // GMV = soma do total do cupom
                BigDecimal gmv =
                        brandTxs.stream()
                                .filter(t -> t.getTotals() != null)
                                .map(t ->
                                        BigDecimal.valueOf(
                                                t.getTotals().getTotal()
                                        )
                                )
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Quantidade = soma de itens vendidos
                int quantity =
                        brandTxs.stream()
                                .mapToInt(t ->
                                        t.getItems() != null
                                                ? t.getItems().size()
                                                : 0
                                )
                                .sum();

                json.append("      \"").append(brand).append("\": {\n")
                        .append("        \"transactions\": ")
                        .append(brandTxs.size()).append(",\n")
                        .append("        \"quantity\": ")
                        .append(quantity).append(",\n")
                        .append("        \"gmv\": ")
                        .append(
                                gmv.setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                )
                        )
                        .append("\n")
                        .append("      }");

                if (++brandIndex < byBrand.size()) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append("    }\n");
            json.append("  }");

            if (++dayIndex < byDate.size()) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("}\n");

        Files.createDirectories(outputDir);
        Files.writeString(
                outputDir.resolve("daily_brand_overview.json"),
                json.toString()
        );
    }

    private DailyBrandOverviewAnalyzerJson() {}
}
