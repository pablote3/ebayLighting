package com.rossotti.lighting.ebay.model.account.returnPolicy;

public enum RefundMethodEnum {
    MONEY_BACK ("Money Back");
    private final String code;
    RefundMethodEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
