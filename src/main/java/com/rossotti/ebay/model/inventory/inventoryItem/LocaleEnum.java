package com.rossotti.ebay.model.inventory.inventoryItem;

public enum LocaleEnum {
    en_US ("en_US");
    private String code;
    LocaleEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
