package com.hypr.marketIntelligenceTokens.service;

import com.hypr.marketIntelligenceTokens.model.OverviewResult;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TransactionAggregatorTest {

    private TransactionAggregator aggregator;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Before
    public void setUp() {
        aggregator = new TransactionAggregator();
    }

    // --- accept() ---

    @Test
    public void testAcceptCompleteTransaction() {
        aggregator.accept(completeTransaction("token1", 100L, new BigDecimal("50.00"), 2, 1));

        OverviewResult result = aggregator.result();
        assertEquals(1L, result.totalTransactions());
        assertEquals(2L, result.totalQuantity());
        assertEquals(new BigDecimal("50.00"), result.totalGmv());
    }

    @Test
    public void testAcceptMultipleCompleteTransactions() {
        aggregator.accept(completeTransaction("token1", 100L, new BigDecimal("50.00"), 2, 1));
        aggregator.accept(completeTransaction("token2", 101L, new BigDecimal("30.00"), 3, 2));

        OverviewResult result = aggregator.result();
        assertEquals(2L, result.totalTransactions());
        assertEquals(5L, result.totalQuantity());
        assertEquals(new BigDecimal("80.00"), result.totalGmv());
    }

    @Test
    public void testAcceptIncompleteTransactionIsIgnored() {
        aggregator.accept(incompleteTransaction("token1"));

        OverviewResult result = aggregator.result();
        assertEquals(0L, result.totalTransactions());
        assertEquals(0L, result.totalQuantity());
        assertEquals(BigDecimal.ZERO, result.totalGmv());
    }

    @Test
    public void testAcceptNullIsIgnored() {
        aggregator.accept(null);

        OverviewResult result = aggregator.result();
        assertEquals(0L, result.totalTransactions());
        assertEquals(0L, result.totalQuantity());
        assertEquals(BigDecimal.ZERO, result.totalGmv());
    }

    @Test
    public void testAcceptMixedTransactions() {
        aggregator.accept(completeTransaction("token1", 100L, new BigDecimal("50.00"), 2, 1));
        aggregator.accept(incompleteTransaction("token2"));
        aggregator.accept(null);
        aggregator.accept(completeTransaction("token3", 102L, new BigDecimal("20.00"), 1, 3));

        OverviewResult result = aggregator.result();
        assertEquals(2L, result.totalTransactions());
        assertEquals(3L, result.totalQuantity());
        assertEquals(new BigDecimal("70.00"), result.totalGmv());
    }

    // --- result() ---

    @Test
    public void testResultWithNoTransactions() {
        OverviewResult result = aggregator.result();

        assertEquals(0L, result.totalTransactions());
        assertEquals(0L, result.totalQuantity());
        assertEquals(BigDecimal.ZERO, result.totalGmv());
    }

    // --- helpers ---

    private TransactionModel completeTransaction(String token, Long sku, BigDecimal gmv, int quantity, int brand) {
        return new TransactionModel(NOW, token, sku, brand, gmv, quantity, true);
    }

    private TransactionModel incompleteTransaction(String token) {
        return new TransactionModel(NOW, token, null, null, null, null, false);
    }
}