package com.rossotti.ebay.model.common;

public enum CurrencyCodeEnum {
    USD ("United States Dollar");
    private String code;
    CurrencyCodeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
