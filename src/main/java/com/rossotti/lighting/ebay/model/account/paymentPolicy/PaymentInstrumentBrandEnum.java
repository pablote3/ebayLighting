package com.rossotti.lighting.ebay.model.account.paymentPolicy;

public enum PaymentInstrumentBrandEnum {
    AMERICAN_EXPRESS ("American Express"),
    DISCOVER ("Discover card"),
    MASTERCARD ("MasterCard"),
    VISA ("Visa");
    private final String code;
    PaymentInstrumentBrandEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
