package com.rossotti.ebay.model.inventory.offer;

public enum ListingStatusEnum {
    ACTIVE ("Active"),
    OUT_OF_STOCK ("Out Of Stock"),
    INACTIVE ("Inactive"),
    ENDED ("Ended"),
    EBAY_ENDED ("Ebay Ended"),
    NOT_LISTED ("Not Listed");

    private String code;
    ListingStatusEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
