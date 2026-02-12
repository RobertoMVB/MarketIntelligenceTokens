package com.hypr.marketIntelligenceTokens.parser.csv;

import com.hypr.marketIntelligenceTokens.model.ParsedTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class TransactionCsvParser {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy, hh:mm:ss a", Locale.ENGLISH);

    private TransactionCsvParser() {}

    public static Optional<ParsedTransaction> parse(String line) {
        try {
            if (!line.startsWith("\"")) return Optional.empty();

            int endDate = line.indexOf("\",");
            if (endDate < 0) return Optional.empty();

            String rawDate = line.substring(1, endDate);
            LocalDateTime timestamp = LocalDateTime.parse(rawDate, DATE_FORMAT);

            String[] cols = line.substring(endDate + 2).split(",", -1);

            // token, sku, gmv, quantity, brand
            if (cols.length < 5) return Optional.empty();

            String token = cols[0].trim();

            Long sku = cols[1].isBlank() ? null : Long.parseLong(cols[1].trim());
            BigDecimal gmv = cols[2].isBlank() ? null : new BigDecimal(cols[2].trim());
            Integer quantity = cols[3].isBlank() ? null : Integer.parseInt(cols[3].trim());
            Integer brand = cols[4].isBlank() ? null : Integer.parseInt(cols[4].trim());

            boolean complete =
                    sku != null && gmv != null && quantity != null && brand != null;

            return Optional.of(
                    new ParsedTransaction(
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
