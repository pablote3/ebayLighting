package com.rossotti.lighting.ebay.model.account.paymentPolicy;

public enum RecipientAccountReferenceTypeEnum {
    PAYPAL_EMAIL ("PAYPAL_EMAIL");
    private final String code;
    RecipientAccountReferenceTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
