package com.rossotti.lighting.ebay.model.common;

public enum CurrencyCodeEnum {
    USD ("United States Dollar");
    private final String code;
    CurrencyCodeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
