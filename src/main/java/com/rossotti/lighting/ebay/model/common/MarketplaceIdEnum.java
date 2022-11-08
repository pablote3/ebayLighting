package com.rossotti.lighting.ebay.model.common;

public enum MarketplaceIdEnum {
    EBAY_US ("EBAY_US");
    private final String code;
    MarketplaceIdEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
