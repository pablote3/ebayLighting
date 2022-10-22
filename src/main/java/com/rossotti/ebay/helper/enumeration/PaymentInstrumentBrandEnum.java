package com.rossotti.ebay.helper.enumeration;

public enum PaymentInstrumentBrandEnum {
    AMERICAN_EXPRESS ("American Express"),
    DISCOVER ("Discover card"),
    MASTERCARD ("MasterCard"),
    VISA ("Visa");
    private String code;
    PaymentInstrumentBrandEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
