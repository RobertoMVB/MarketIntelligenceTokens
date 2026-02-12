package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssuerDTO {
    private String cnpj;

    @JsonProperty("endereco")
    private AddressDTO address;

    @JsonProperty("inscricao_estadual")
    private String stateRegistration;

    @JsonProperty("inscricao_municipal")
    private String municipalRegistration;

    @JsonProperty("nome_fantasia")
    private String tradeName;

    @JsonProperty("razao_social")
    private String legalName;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public String getStateRegistration() {
        return stateRegistration;
    }

    public void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

    public String getMunicipalRegistration() {
        return municipalRegistration;
    }

    public void setMunicipalRegistration(String municipalRegistration) {
        this.municipalRegistration = municipalRegistration;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }
}
