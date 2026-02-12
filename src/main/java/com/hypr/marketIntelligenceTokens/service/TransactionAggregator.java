package com.hypr.marketIntelligenceTokens.service;

import com.hypr.marketIntelligenceTokens.model.OverviewResult;
import com.hypr.marketIntelligenceTokens.model.TransactionModel;

import java.math.BigDecimal;

public class TransactionAggregator {

    private BigDecimal totalGmv = BigDecimal.ZERO;
    private long totalQuantity = 0;
    private long totalTransactions = 0;

    public void accept(TransactionModel tx) {
        if (tx == null || !tx.isComplete()) {
            return;
        }

        totalTransactions++;
        totalQuantity += tx.getQuantity();
        totalGmv = totalGmv.add(tx.getGmv());
    }

    public OverviewResult result() {
        return new OverviewResult(
                totalTransactions,
                totalQuantity,
                totalGmv
        );
    }
}
