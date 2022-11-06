package com.rossotti.ebay.model.common;

public enum QueryParamEnum {
    LIMIT ("limit"),
    OFFSET ("offset"),
    MARKETPLACE_ID ("marketplace_id"),
    SKU ("sku");
    private final String code;
    QueryParamEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
