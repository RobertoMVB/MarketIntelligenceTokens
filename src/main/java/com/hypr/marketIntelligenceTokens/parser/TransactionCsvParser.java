package com.hypr.marketIntelligenceTokens.parser;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class TransactionCsvParser {

    // MM/DD/YYYY HH:MM:SS
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");

    private TransactionCsvParser() {
        // utility class
    }

    public static Optional<TransactionModel> parse(String csvLine) {
        try {
            String[] columns = csvLine.split(",");

            if (columns.length < 6) {
                return Optional.empty();
            }

            LocalDateTime timestamp = parseDate(columns[0]);
            String token = columns[1].trim();
            long sku = Long.parseLong(columns[2].trim());
            BigDecimal gmv = new BigDecimal(columns[3].trim());
            int quantity = Integer.parseInt(columns[4].trim());
            int brand = Integer.parseInt(columns[5].trim());

            return Optional.of(new TransactionModel(
                    timestamp,
                    token,
                    sku,
                    gmv,
                    quantity,
                    brand
            ));

        } catch (NumberFormatException | DateTimeParseException e) {
            // linha inválida ou suja → ignora
            return Optional.empty();
        }
    }

    private static LocalDateTime parseDate(String raw) {
        return LocalDateTime.parse(raw.trim(), DATE_FORMAT);
    }
}

