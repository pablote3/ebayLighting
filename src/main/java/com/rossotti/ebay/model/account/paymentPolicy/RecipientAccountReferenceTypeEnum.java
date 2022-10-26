package com.rossotti.ebay.model.account.paymentPolicy;

public enum RecipientAccountReferenceTypeEnum {
    PAYPAL_EMAIL ("PAYPAL_EMAIL");
    private String code;
    RecipientAccountReferenceTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
