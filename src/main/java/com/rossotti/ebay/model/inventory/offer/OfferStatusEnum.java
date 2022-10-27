package com.rossotti.ebay.model.inventory.offer;

public enum OfferStatusEnum {
    PUBLISHED ("Published"),
    UNPUBLISHED ("Unpublished");

    private String code;
    OfferStatusEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
