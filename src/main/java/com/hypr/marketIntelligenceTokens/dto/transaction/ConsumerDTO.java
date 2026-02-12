package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerDTO {
    @JsonProperty("cpf_cnpj")
    private String taxId;

    @JsonProperty("razao_social")
    private String legalName;

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }
}
