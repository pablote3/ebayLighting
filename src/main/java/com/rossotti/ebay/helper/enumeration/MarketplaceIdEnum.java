package com.rossotti.ebay.helper.enumeration;

public enum MarketplaceIdEnum {
    EBAY_US ("EBAY_US");
    private String code;
    MarketplaceIdEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
