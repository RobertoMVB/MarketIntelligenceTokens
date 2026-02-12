package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TransactionDTO {
    @JsonProperty("capturado_em")
    private String capturedAt;

    @JsonProperty("consumidor")
    private ConsumerDTO consumer;

    @JsonProperty("cupom")
    private ReceiptDTO receipt;

    @JsonProperty("emitente")
    private IssuerDTO issuer;

    @JsonProperty("itens")
    private List<ItemDTO> items;

    @JsonProperty("pagamento")
    private List<PaymentDTO> payments;

    private String status;

    @JsonProperty("totais")
    private TotalTransactionDTO totals;

    @JsonProperty("troco")
    private double change;

    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }

    public ConsumerDTO getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerDTO consumer) {
        this.consumer = consumer;
    }

    public ReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDTO receipt) {
        this.receipt = receipt;
    }

    public IssuerDTO getIssuer() {
        return issuer;
    }

    public void setIssuer(IssuerDTO issuer) {
        this.issuer = issuer;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TotalTransactionDTO getTotals() {
        return totals;
    }

    public void setTotals(TotalTransactionDTO totals) {
        this.totals = totals;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
