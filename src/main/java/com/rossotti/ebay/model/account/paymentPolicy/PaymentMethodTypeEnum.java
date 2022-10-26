package com.rossotti.ebay.model.account.paymentPolicy;

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
