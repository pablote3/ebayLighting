package com.rossotti.lighting.ebay.model.inventory.offer;

public enum ListingDurationEnum {
    DAYS_1 ("Days 1"),
    DAYS_3 ("Days 3"),
    DAYS_5 ("Days 5"),
    DAYS_7 ("Days 7"),
    DAYS_10 ("Days 10"),
    DAYS_21 ("Days 21"),
    DAYS_30 ("Days 30"),
    GTC ("Good Til Cancelled");

    private final String code;
    ListingDurationEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
