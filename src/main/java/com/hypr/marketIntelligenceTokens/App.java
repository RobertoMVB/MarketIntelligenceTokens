package com.hypr.marketIntelligenceTokens;

import com.hypr.marketIntelligenceTokens.analysis.byCsv.BrandGmvAnalyzer;
import com.hypr.marketIntelligenceTokens.analysis.byCsv.DailyBrandOverviewAnalyzer;
import com.hypr.marketIntelligenceTokens.analysis.byCsv.SkuConcentrationAnalyzer;
import com.hypr.marketIntelligenceTokens.loader.DatasetLoader;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import com.hypr.marketIntelligenceTokens.pipeline.OverviewPipeline;
import com.hypr.marketIntelligenceTokens.report.HtmlReportGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) {

        System.out.println("Market Intelligence Tokens!");

        Path csvPath = Path.of(args[0]);
        Path outputDir = Path.of("TransactionAnalysis");

        try {

            List<TransactionModel> dataset = DatasetLoader.loadCSV(csvPath);
            System.out.println("Dataset carregado com sucesso.");

            SkuConcentrationAnalyzer.analyze(dataset, outputDir);
            BrandGmvAnalyzer.analyze(dataset, outputDir);
            DailyBrandOverviewAnalyzer.analyze(dataset, outputDir);
            OverviewPipeline.generate(csvPath, outputDir);

            HtmlReportGenerator.generate(outputDir);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
