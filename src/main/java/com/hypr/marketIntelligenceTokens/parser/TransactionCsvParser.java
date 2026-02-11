package com.hypr.marketIntelligenceTokens.parser;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

public class TransactionCsvParser {

    // Ex: May 29, 2025, 10:03:53 AM
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy, hh:mm:ss a", Locale.ENGLISH);

    private TransactionCsvParser() {
    }

    public static Optional<TransactionModel> parse(String line) {
        try {
            if (!line.startsWith("\"")) {
                return Optional.empty();
            }

            int closingQuote = line.indexOf("\",");
            if (closingQuote < 0) {
                return Optional.empty();
            }

            // 1️⃣ DATA (obrigatória)
            String rawDate = line.substring(1, closingQuote);
            LocalDateTime timestamp =
                    LocalDateTime.parse(rawDate, DATE_FORMAT);

            String[] cols = line.substring(closingQuote + 2)
                    .split(",", -1);

            if (cols.length < 5) {
                return Optional.empty();
            }

            // 2️⃣ TOKEN (obrigatório)
            String token = cols[0].trim();
            if (token.isBlank()) {
                return Optional.empty();
            }

            // 3️⃣ SKU (opcional)
            Long sku = cols[1].isBlank()
                    ? null
                    : Long.parseLong(cols[1].trim());

            // 4️⃣ BRAND (opcional)
            Integer brand = cols[4].isBlank()
                    ? null
                    : Integer.parseInt(cols[4].trim());

            boolean complete =
                    brand != null &&
                            !cols[2].isBlank() &&
                            !cols[3].isBlank();

            BigDecimal gmv = complete
                    ? new BigDecimal(cols[2].trim())
                    : null;

            Integer quantity = complete
                    ? Integer.parseInt(cols[3].trim())
                    : null;

            return Optional.of(
                    new TransactionModel(
                            timestamp,
                            token,
                            sku,
                            brand,
                            gmv,
                            quantity,
                            complete
                    )
            );

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
