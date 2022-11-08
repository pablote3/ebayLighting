package com.rossotti.lighting.ebay.model.account.fulfillmentPolicy;

public enum OptionTypeEnum {
    DOMESTIC ("Domestic"),
    INTERNATIONAL ("International");
    private final String code;
    OptionTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
