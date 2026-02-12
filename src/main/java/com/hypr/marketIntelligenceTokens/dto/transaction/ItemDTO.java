package com.hypr.marketIntelligenceTokens.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemDTO {
    @JsonProperty("codigo")
    private String code;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("item")
    private int index;

    @JsonProperty("qtd")
    private int quantity;

    @JsonProperty("un")
    private String unit;

    @JsonProperty("vl_item")
    private double itemValue;

    @JsonProperty("vl_unitario")
    private double unitValue;

    @JsonProperty("vl_tributos")
    private double taxesValue;

    @JsonProperty("desconto_item")
    private double itemDiscount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getItemValue() {
        return itemValue;
    }

    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }

    public double getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(double unitValue) {
        this.unitValue = unitValue;
    }

    public double getTaxesValue() {
        return taxesValue;
    }

    public void setTaxesValue(double taxesValue) {
        this.taxesValue = taxesValue;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }
}
