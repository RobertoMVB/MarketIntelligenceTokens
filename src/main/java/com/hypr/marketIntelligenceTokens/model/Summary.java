package com.hypr.marketIntelligenceTokens.model;

public record Summary(
        int totalTransactions,
        int completeTransactions,
        int incompleteTransactions
) {}
