package com.hypr.marketIntelligenceTokens;

import com.hypr.marketIntelligenceTokens.analysis.BrandGmvAnalyzer;
import com.hypr.marketIntelligenceTokens.analysis.SkuConcentrationAnalyzer;
import com.hypr.marketIntelligenceTokens.loader.DatasetLoader;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        System.out.println( "Market Intelligence Tokens!" );
        Path csvPath = Path.of(args[0]);
        try {
            List<TransactionModel> dataset = DatasetLoader.load(csvPath); // true = parallel
            System.out.println("Dataset carregado com sucesso.");
            Path outputDir = Path.of("output");
            SkuConcentrationAnalyzer.analyze(dataset, outputDir);
            BrandGmvAnalyzer.analyze(dataset, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
