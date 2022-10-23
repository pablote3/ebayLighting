package com.rossotti.ebay.helper.enumeration;

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
