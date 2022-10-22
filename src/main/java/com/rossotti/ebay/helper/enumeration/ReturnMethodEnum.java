package com.rossotti.ebay.helper.enumeration;

public enum ReturnMethodEnum {
    EXCHANGE ("Exchange"),
    REPLACEMENT ("Replacement");
    private String code;
    ReturnMethodEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
