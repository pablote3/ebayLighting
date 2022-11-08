package com.rossotti.lighting.ebay.model.account.returnPolicy;

public enum ReturnMethodEnum {
    EXCHANGE ("Exchange"),
    REPLACEMENT ("Replacement");
    private final String code;
    ReturnMethodEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
