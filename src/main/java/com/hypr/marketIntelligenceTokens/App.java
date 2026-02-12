package com.hypr.marketIntelligenceTokens;

import com.hypr.marketIntelligenceTokens.analysis.byCsv.BrandGmvAnalyzer;
import com.hypr.marketIntelligenceTokens.analysis.byCsv.DailyBrandOverviewAnalyzer;
import com.hypr.marketIntelligenceTokens.analysis.byCsv.SkuConcentrationAnalyzer;
import com.hypr.marketIntelligenceTokens.loader.DatasetLoader;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import com.hypr.marketIntelligenceTokens.pipeline.OverviewPipeline;
import com.hypr.marketIntelligenceTokens.report.HtmlReportGenerator;
import com.hypr.marketIntelligenceTokens.service.FileType;
import com.hypr.marketIntelligenceTokens.service.FileValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) {

        System.out.println("Market Intelligence Tokens!");

        Path filePath = Path.of(args[0]);
        Path outputDir = Path.of("TransactionAnalysis");
        try {
            switch (FileValidator.detectFileType(filePath)) {
                case CSV : System.out.println("Arquivo CSV");
                    List<TransactionModel> dataset = DatasetLoader.loadCSV(filePath);
                    System.out.println("Dataset carregado com sucesso.");
                    SkuConcentrationAnalyzer.analyze(dataset, outputDir);
                    BrandGmvAnalyzer.analyze(dataset, outputDir);
                    DailyBrandOverviewAnalyzer.analyze(dataset, outputDir);
                    OverviewPipeline.generate(dataset, outputDir);
                    break;
                case JSON : System.out.println("Arquivo JSON");
                case INVALID : System.out.println("Arquivo inválido");
            }
            HtmlReportGenerator.generate(outputDir);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
