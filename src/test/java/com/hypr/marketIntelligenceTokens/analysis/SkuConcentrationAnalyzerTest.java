package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SkuConcentrationAnalyzerTest {

    @Test
    public void testAnalyzeWithCompleteTransactions() throws IOException {
        TransactionModel t1 = new TransactionModel(null, "T1", 100L, 1, new BigDecimal("10"), 1, true);
        TransactionModel t2 = new TransactionModel(null, "T2", 101L, 2, new BigDecimal("20"), 2, true);

        Path tempDir = Files.createTempDirectory("skuConcentration");
        SkuConcentrationAnalyzer.analyze(List.of(t1, t2), tempDir);

        Path outputFile = tempDir.resolve("sku_concentration.json");
        assertTrue(Files.exists(outputFile));
        String json = Files.readString(outputFile);
        assertTrue(json.contains("\"totalSkus\""));
        assertTrue(json.contains("\"skusFor80PercentGmv\""));
    }

    @Test
    public void testAnalyzeWithNoCompleteTransactions() throws IOException {
        TransactionModel t1 = new TransactionModel(null, "T1", null, 1, new BigDecimal("10"), 1, false);

        Path tempDir = Files.createTempDirectory("skuEmpty");
        SkuConcentrationAnalyzer.analyze(List.of(t1), tempDir);

        assertFalse(Files.exists(tempDir.resolve("sku_concentration.json")));
    }
}
