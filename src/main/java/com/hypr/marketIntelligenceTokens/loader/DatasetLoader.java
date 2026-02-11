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

    public static List<TransactionModel> load(Path csvPath) throws IOException {

        AtomicInteger invalid = new AtomicInteger();
        AtomicInteger incomplete = new AtomicInteger();

        try (Stream<String> lines = Files.lines(csvPath)) {

            List<TransactionModel> dataset = lines
                    .skip(1) // header
                    .map(line -> {
                        String[] cols = line.split(",");

                        if (cols.length < 6) {
                            incomplete.incrementAndGet();
                            return Optional.<TransactionModel>empty();
                        }

                        Optional<TransactionModel> opt =
                                TransactionCsvParser.parse(line);

                        if (opt.isEmpty()) {
                            invalid.incrementAndGet();
                        }

                        return opt;
                    })
                    .flatMap(Optional::stream)
                    .toList();

            System.out.println("Linhas válidas: " + dataset.size());
            System.out.println("Linhas incompletas (campos ausentes): " + incomplete.get());
            System.out.println("Linhas inválidas (parse): " + invalid.get());

            return dataset;
        }
    }

    private DatasetLoader() {
    }
}
