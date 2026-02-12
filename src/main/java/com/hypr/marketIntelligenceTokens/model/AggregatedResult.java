package com.hypr.marketIntelligenceTokens.model;

import java.util.Map;

public class AggregatedResult {

    private final Summary summary;
    private final Map<String, Integer> transactionsByDate;
    private final Map<Integer, BrandStats> brandStats;

    public AggregatedResult(
            Summary summary,
            Map<String, Integer> transactionsByDate,
            Map<Integer, BrandStats> brandStats) {

        this.summary = summary;
        this.transactionsByDate = transactionsByDate;
        this.brandStats = brandStats;
    }

    public Summary getSummary() { return summary; }
    public Map<String, Integer> getTransactionsByDate() { return transactionsByDate; }
    public Map<Integer, BrandStats> getBrandStats() { return brandStats; }
}
