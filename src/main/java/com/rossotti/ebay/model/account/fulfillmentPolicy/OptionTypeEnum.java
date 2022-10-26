package com.rossotti.ebay.model.account.fulfillmentPolicy;

public enum OptionTypeEnum {
    DOMESTIC ("Domestic"),
    INTERNATIONAL ("International");
    private String code;
    OptionTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
