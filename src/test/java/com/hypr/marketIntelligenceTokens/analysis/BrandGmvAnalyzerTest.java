package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.analysis.byCsv.BrandGmvAnalyzer;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class BrandGmvAnalyzerTest {

    @Test
    public void testAnalyzeWithValidData() throws IOException {
        TransactionModel t1 = new TransactionModel(null, "T1", 100L, 1, new BigDecimal("10"), 1, true);
        TransactionModel t2 = new TransactionModel(null, "T2", 101L, 2, new BigDecimal("20"), 2, true);
        List<TransactionModel> dataset = List.of(t1, t2);

        Path tempDir = Files.createTempDirectory("brandGmv");
        BrandGmvAnalyzer.analyze(dataset, tempDir);

        Path outputFile = tempDir.resolve("brand_gmv.json");
        assertTrue(Files.exists(outputFile));
        String json = Files.readString(outputFile);
        assertTrue(json.contains("\"brand\": 1"));
        assertTrue(json.contains("\"brand\": 2"));
    }

    @Test
    public void testAnalyzeWithEmptyDataset() throws IOException {
        Path tempDir = Files.createTempDirectory("brandGmvEmpty");
        BrandGmvAnalyzer.analyze(List.of(), tempDir);
        assertFalse(Files.exists(tempDir.resolve("brand_gmv.json")));
    }
}
