package com.rossotti.lighting.ebay.model.inventory.offer;

public enum OfferStatusEnum {
    PUBLISHED ("Published"),
    UNPUBLISHED ("Unpublished");

    private final String code;
    OfferStatusEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
