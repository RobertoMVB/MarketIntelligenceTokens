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

public class TicketStatsAnalyzerTest {

    @Test
    public void testAnalyzeWithData() throws IOException {
        TransactionModel t1 = new TransactionModel(null, "T1", 100L, 1, new BigDecimal("10"), 1, true);
        TransactionModel t2 = new TransactionModel(null, "T2", 101L, 2, new BigDecimal("30"), 2, true);

        Path tempDir = Files.createTempDirectory("ticketStats");
        TicketStatsAnalyzer.analyze(List.of(t1, t2), tempDir);

        Path outputFile = tempDir.resolve("ticket_stats.json");
        assertTrue(Files.exists(outputFile));
        String json = Files.readString(outputFile);
        assertTrue(json.contains("\"meanTicket\""));
        assertTrue(json.contains("\"medianTicket\""));
    }

    @Test
    public void testAnalyzeWithEmptyDataset() throws IOException {
        Path tempDir = Files.createTempDirectory("ticketEmpty");
        TicketStatsAnalyzer.analyze(List.of(), tempDir);
        assertFalse(Files.exists(tempDir.resolve("ticket_stats.json")));
    }
}
