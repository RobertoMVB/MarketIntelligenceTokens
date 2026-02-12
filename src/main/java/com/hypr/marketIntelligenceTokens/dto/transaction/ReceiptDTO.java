package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReceiptDTO {
    @JsonProperty("chave_acesso")
    private String accessKey;

    @JsonProperty("data_emissao")
    private String issueDate;

    @JsonProperty("hora_emissao")
    private String issueTime;

    @JsonProperty("numero")
    private String number;

    @JsonProperty("pdv_transacao")
    private String posTransaction;

    @JsonProperty("sat_numero_serie")
    private String satSerialNumber;

    @JsonProperty("tipo")
    private String type;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPosTransaction() {
        return posTransaction;
    }

    public void setPosTransaction(String posTransaction) {
        this.posTransaction = posTransaction;
    }

    public String getSatSerialNumber() {
        return satSerialNumber;
    }

    public void setSatSerialNumber(String satSerialNumber) {
        this.satSerialNumber = satSerialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
