package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;

public class BrandStats {

    private final int brand;
    private BigDecimal totalGmv = BigDecimal.ZERO;
    private long transactions = 0;

    public BrandStats(int brand) {
        this.brand = brand;
    }

    public void addGmv(BigDecimal gmv) {
        if (gmv == null) return;
        totalGmv = totalGmv.add(gmv);
        transactions++;
    }

    public int getBrand() {
        return brand;
    }

    public BigDecimal getTotalGmv() {
        return totalGmv;
    }

    public long getTransactions() {
        return transactions;
    }
}
