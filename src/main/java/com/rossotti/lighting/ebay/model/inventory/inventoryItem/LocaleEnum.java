package com.rossotti.lighting.ebay.model.inventory.inventoryItem;

public enum LocaleEnum {
    en_US ("en_US");
    private final String code;
    LocaleEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
