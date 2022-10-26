package com.rossotti.ebay.model.account.returnPolicy;

public enum RefundMethodEnum {
    MONEY_BACK ("Money Back");
    private String code;
    RefundMethodEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
