package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDTO {

    @JsonProperty("forma")
    private String method;

    @JsonProperty("valor")
    private double amount;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
