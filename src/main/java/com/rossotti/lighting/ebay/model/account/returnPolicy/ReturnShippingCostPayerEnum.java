package com.rossotti.lighting.ebay.model.account.returnPolicy;

public enum ReturnShippingCostPayerEnum {
    BUYER ("Buyer"),
    SELLER ("Seller");
    private final String code;
    ReturnShippingCostPayerEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
