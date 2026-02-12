package com.hypr.marketIntelligenceTokens.pipeline;

import com.hypr.marketIntelligenceTokens.model.OverviewResult;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OverviewPipelineTest {

    private final OverviewPipeline pipeline = new OverviewPipeline();
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Test
    public void testRunWithCompleteTransactions() {
        List<TransactionModel> transactions = List.of(
                transaction("token1", new BigDecimal("50.00"), 2, true),
                transaction("token2", new BigDecimal("30.00"), 3, true)
        );

        OverviewResult result = pipeline.run(transactions);

        assertEquals(2L, result.totalTransactions());
        assertEquals(5L, result.totalQuantity());
        assertEquals(new BigDecimal("80.00"), result.totalGmv());
    }

    @Test
    public void testRunIgnoresIncompleteTransactions() {
        List<TransactionModel> transactions = List.of(
                transaction("token1", new BigDecimal("50.00"), 2, true),
                transaction("token2", null, null, false)
        );

        OverviewResult result = pipeline.run(transactions);

        assertEquals(1L, result.totalTransactions());
        assertEquals(2L, result.totalQuantity());
        assertEquals(new BigDecimal("50.00"), result.totalGmv());
    }

    @Test
    public void testRunWithEmptyList() {
        OverviewResult result = pipeline.run(List.of());

        assertEquals(0L, result.totalTransactions());
        assertEquals(0L, result.totalQuantity());
        assertEquals(BigDecimal.ZERO, result.totalGmv());
    }

    @Test
    public void testRunWithAllIncompleteTransactions() {
        List<TransactionModel> transactions = List.of(
                transaction("token1", null, null, false),
                transaction("token2", null, null, false)
        );

        OverviewResult result = pipeline.run(transactions);

        assertEquals(0L, result.totalTransactions());
        assertEquals(0L, result.totalQuantity());
        assertEquals(BigDecimal.ZERO, result.totalGmv());
    }

    // --- helper ---

    private TransactionModel transaction(String token, BigDecimal gmv, Integer quantity, boolean complete) {
        return new TransactionModel(NOW, token, 100L, 1, gmv, quantity, complete);
    }
}