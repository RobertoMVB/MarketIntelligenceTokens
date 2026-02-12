package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionModel {

    private final LocalDateTime timestamp;
    private final String token;
    private final Long sku;          // pode ser null
    private final Integer brand;     // pode ser null
    private final BigDecimal gmv;     // pode ser null se incompleta
    private final Integer quantity;  // pode ser null se incompleta
    private final boolean complete;

    public TransactionModel(
            LocalDateTime timestamp,
            String token,
            Long sku,
            Integer brand,
            BigDecimal gmv,
            Integer quantity,
            boolean complete
    ) {
        this.timestamp = timestamp;
        this.token = token;
        this.sku = sku;
        this.brand = brand;
        this.gmv = gmv;
        this.quantity = quantity;
        this.complete = complete;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getToken() { return token; }
    public Long getSku() { return sku; }
    public Integer getBrand() { return brand; }
    public BigDecimal getGmv() { return gmv; }
    public Integer getQuantity() { return quantity; }
    public boolean isComplete() { return complete; }
}
