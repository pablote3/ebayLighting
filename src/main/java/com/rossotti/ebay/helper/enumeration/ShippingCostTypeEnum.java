package com.rossotti.ebay.helper.enumeration;

public enum ShippingCostTypeEnum {
    CALCULATED ("Calculated"),
    FLAT_RATE ("Flat Rage"),
    NOT_SPECIFIED ("Not Specified");
    private String code;
    ShippingCostTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
