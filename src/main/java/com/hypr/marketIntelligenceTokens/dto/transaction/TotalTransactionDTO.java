package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TotalTransactionDTO {
    @JsonProperty("desconto_subtotal")
    private double subtotalDiscount;

    private double total;

    @JsonProperty("total_bruto_itens")
    private double grossItemsTotal;

    @JsonProperty("total_descontos_itens")
    private double totalItemsDiscount;

    @JsonProperty("total_tributos_aprox")
    private double approximateTaxesTotal;

    public double getSubtotalDiscount() {
        return subtotalDiscount;
    }

    public void setSubtotalDiscount(double subtotalDiscount) {
        this.subtotalDiscount = subtotalDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getGrossItemsTotal() {
        return grossItemsTotal;
    }

    public void setGrossItemsTotal(double grossItemsTotal) {
        this.grossItemsTotal = grossItemsTotal;
    }

    public double getTotalItemsDiscount() {
        return totalItemsDiscount;
    }

    public void setTotalItemsDiscount(double totalItemsDiscount) {
        this.totalItemsDiscount = totalItemsDiscount;
    }

    public double getApproximateTaxesTotal() {
        return approximateTaxesTotal;
    }

    public void setApproximateTaxesTotal(double approximateTaxesTotal) {
        this.approximateTaxesTotal = approximateTaxesTotal;
    }
}
