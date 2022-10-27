package com.rossotti.ebay.model.common;

public enum WeightUnitOfMeasureEnum {
    POUND ("Pound"),
    KILOGRAM ("Kilogram"),
    OUNCE ("Ounce"),
    GRAM ("Gram");
    private String code;
    WeightUnitOfMeasureEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
