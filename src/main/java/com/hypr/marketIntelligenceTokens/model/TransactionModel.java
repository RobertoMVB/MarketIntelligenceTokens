package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionModel {

    /*
    BigDecimal para GMV → precisão financeira
    String para token → preservação total do identificador
    Modelo imutável → segurança e paralelismo
    */

    private LocalDateTime timestamp;
    private String token;
    private Long sku;
    private Integer brand;
    private BigDecimal gmv;
    private Integer quantity;
    private boolean complete;

    public TransactionModel(LocalDateTime timestamp, String token, Long sku, Integer brand, BigDecimal gmv, Integer quantity, boolean complete) {
        this.timestamp = timestamp;
        this.token = token;
        this.sku = sku;
        this.brand = brand;
        this.gmv = gmv;
        this.quantity = quantity;
        this.complete = complete;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getSku() {
        return sku;
    }

    public void setSku(Long sku) {
        this.sku = sku;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    public BigDecimal getGmv() {
        return gmv;
    }

    public void setGmv(BigDecimal gmv) {
        this.gmv = gmv;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }
}
