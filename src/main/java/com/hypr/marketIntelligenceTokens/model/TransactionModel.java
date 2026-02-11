package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionModel {

    /*
    BigDecimal para GMV → precisão financeira
    String para token → preservação total do identificador
    Modelo imutável → segurança e paralelismo
    */

    private final LocalDateTime timestamp;
    private final String token;
    private final long sku;
    private final BigDecimal gmv;
    private final int quantity;
    private final int brand;

    public TransactionModel(
            LocalDateTime timestamp,
            String token,
            long sku,
            BigDecimal gmv,
            int quantity,
            int brand
    ) {
        this.timestamp = timestamp;
        this.token = token;
        this.sku = sku;
        this.gmv = gmv;
        this.quantity = quantity;
        this.brand = brand;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getToken() {
        return token;
    }

    public long getSku() {
        return sku;
    }

    public BigDecimal getGmv() {
        return gmv;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBrand() {
        return brand;
    }


}
