package com.hypr.marketIntelligenceTokens.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypr.marketIntelligenceTokens.dto.transaction.TransactionDTO;
import com.hypr.marketIntelligenceTokens.model.ParsedTransaction;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import com.hypr.marketIntelligenceTokens.parser.csv.TransactionCsvParser;
import com.hypr.marketIntelligenceTokens.parser.json.TransactionJsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatasetLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TransactionJsonParser transactionJsonParser = new TransactionJsonParser();
    private DatasetLoader() {}

    public List<TransactionDTO> loadJson(String jsonPath) throws IOException {
        List<TransactionDTO> transactions = objectMapper.readValue(
                new File(jsonPath),
                new TypeReference<List<TransactionDTO>>() {}
        );

        return transactionJsonParser.process(transactions);
    }

    public static List<TransactionModel> loadCSV(Path csvPath) throws IOException {

        return Files.lines(csvPath)
                .skip(1) // header
                .map(DatasetLoader::parseLine)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private static Optional<TransactionModel> parseLine(String line) {

        Optional<ParsedTransaction> parsed =
                TransactionCsvParser.parse(line);

        return parsed.map(p ->
                new TransactionModel(
                        p.timestamp(),
                        p.token(),
                        p.sku(),
                        p.brand(),
                        p.gmv(),
                        p.quantity(),
                        p.complete()
                )
        );
    }
}
