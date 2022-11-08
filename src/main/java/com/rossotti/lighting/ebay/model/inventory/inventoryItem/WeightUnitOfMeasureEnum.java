package com.rossotti.lighting.ebay.model.inventory.inventoryItem;

public enum WeightUnitOfMeasureEnum {
    POUND ("Pound"),
    KILOGRAM ("Kilogram"),
    OUNCE ("Ounce"),
    GRAM ("Gram");
    private final String code;
    WeightUnitOfMeasureEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
