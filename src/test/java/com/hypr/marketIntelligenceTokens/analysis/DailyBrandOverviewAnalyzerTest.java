package com.hypr.marketIntelligenceTokens.analysis;

import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DailyBrandOverviewAnalyzerTest {

    @Test
    public void testAnalyzeWithData() throws IOException {
        TransactionModel t1 = new TransactionModel(LocalDateTime.now(), "T1", 100L, 1, new BigDecimal("10"), 1, true);
        TransactionModel t2 = new TransactionModel(LocalDateTime.now(), "T2", 101L, 2, new BigDecimal("20"), 2, true);

        Path tempDir = Files.createTempDirectory("dailyOverview");
        DailyBrandOverviewAnalyzer.analyze(List.of(t1, t2), tempDir);

        Path outputFile = tempDir.resolve("daily_brand_overview.json");
        assertTrue(Files.exists(outputFile));
        String json = Files.readString(outputFile);
        assertTrue(json.contains("\"totalTransactions\""));
        assertTrue(json.contains("\"brands\""));
    }

    @Test
    public void testAnalyzeWithEmptyDataset() throws IOException {
        Path tempDir = Files.createTempDirectory("dailyEmpty");
        DailyBrandOverviewAnalyzer.analyze(List.of(), tempDir);
        assertTrue(Files.exists(tempDir.resolve("daily_brand_overview.json")));
    }
}
