package com.hypr.marketIntelligenceTokens.loader;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import com.hypr.marketIntelligenceTokens.parser.TransactionCsvParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DatasetLoader {

    public static List<TransactionModel> load(Path csvPath, boolean parallel) throws IOException {
        AtomicInteger invalidLines = new AtomicInteger(0);

        try (Stream<String> lines = Files.lines(csvPath)) {

            Stream<String> dataStream = lines.skip(1); // ignora header

            if (parallel) {
                dataStream = dataStream.parallel();
            }

            List<TransactionModel> dataset = dataStream
                    .map(TransactionCsvParser::parse)
                    .peek(opt -> {
                        if (opt.isEmpty()) {
                            invalidLines.incrementAndGet();
                        }
                    })
                    .flatMap(Optional::stream)
                    .toList();

            System.out.println("Linhas válidas: " + dataset.size());
            System.out.println("Linhas inválidas ignoradas: " + invalidLines.get());

            return dataset;
        }
    }

    private DatasetLoader() {

    }
}
