package com.rossotti.ebay.model.account.paymentPolicy;

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
