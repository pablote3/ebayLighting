package com.rossotti.ebay.helper.enumeration;

public enum PaymentMethodTypeEnum {
    CREDIT_CARD ("Credit Card"),
    PAYPAL ("Paypal");
    private String code;
    PaymentMethodTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
