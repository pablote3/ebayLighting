package com.rossotti.ebay.model.inventory.inventoryItem;

public enum LengthUnitOfMeasureEnum {
    INCH ("Inch"),
    FEET ("Feet"),
    CENTIMETER ("Centimeter"),
    METER ("Meter");
    private String code;
    LengthUnitOfMeasureEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
