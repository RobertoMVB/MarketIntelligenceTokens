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
            // Linha deve começar com data entre aspas
            if (!line.startsWith("\"")) {
                return Optional.empty();
            }

            int closingQuote = line.indexOf("\",");
            if (closingQuote < 0) {
                return Optional.empty();
            }

            // 1) Data
            String rawDate = line.substring(1, closingQuote);
            LocalDateTime timestamp =
                    LocalDateTime.parse(rawDate, DATE_FORMAT);

            // 2) Restante das colunas
            String remaining = line.substring(closingQuote + 2);
            String[] cols = remaining.split(",", -1);

            // Esperado: token, sku, gmv, quantity, brand
            if (cols.length < 5) {
                return Optional.empty();
            }

            String token = cols[0].trim();

            // Campos obrigatórios para transação válida
            if (cols[1].isBlank()
                    || cols[2].isBlank()
                    || cols[3].isBlank()
                    || cols[4].isBlank()) {
                return Optional.empty();
            }

            long sku = Long.parseLong(cols[1].trim());
            BigDecimal gmv = new BigDecimal(cols[2].trim());
            int quantity = Integer.parseInt(cols[3].trim());
            int brand = Integer.parseInt(cols[4].trim());

            return Optional.of(new TransactionModel(
                    timestamp,
                    token,
                    sku,
                    gmv,
                    quantity,
                    brand
            ));

        } catch (NumberFormatException | DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
