package com.rossotti.lighting.ebay.model.inventory.offer;

public enum FormatTypeEnum {
    AUCTION ("Auction"),
    FIXED_PRICE ("Fixed Price");

    private final String code;
    FormatTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
