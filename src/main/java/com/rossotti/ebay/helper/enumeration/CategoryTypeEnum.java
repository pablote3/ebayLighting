package com.rossotti.ebay.helper.enumeration;

public enum CategoryTypeEnum {
    MOTORS_VEHICLES ("Motors Vehicles"),
    ALL_EXCLUDING_MOTORS_VEHICLES ("All Excluding Motors Vehicles");
    private String code;
    CategoryTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
