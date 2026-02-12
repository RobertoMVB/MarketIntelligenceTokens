package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;

public record OverviewResult(
        long totalTransactions,
        long totalQuantity,
        BigDecimal totalGmv
) {}

