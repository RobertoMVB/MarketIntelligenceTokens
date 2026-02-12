package com.hypr.marketIntelligenceTokens.pipeline;

import com.hypr.marketIntelligenceTokens.loader.DatasetLoader;
import com.hypr.marketIntelligenceTokens.model.OverviewResult;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import com.hypr.marketIntelligenceTokens.service.TransactionAggregator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OverviewPipeline {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Ponto de entrada chamado pela App
     */
    public static void generate(List<TransactionModel> transactions, Path outputDir) throws IOException {
        OverviewPipeline pipeline = new OverviewPipeline();
        OverviewResult result = pipeline.run(transactions);

        Files.createDirectories(outputDir);

        Path outputFile = outputDir.resolve("overview.json");
        MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(outputFile.toFile(), result);
    }

    /**
     * Execução da lógica de agregação
     */
    public OverviewResult run(List<TransactionModel> transactions) {

        TransactionAggregator aggregator = new TransactionAggregator();

        for (TransactionModel tx : transactions) {
            aggregator.accept(tx);
        }

        return aggregator.result();
    }
}
