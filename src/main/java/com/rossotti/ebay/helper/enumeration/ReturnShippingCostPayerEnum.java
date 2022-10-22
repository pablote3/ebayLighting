package com.rossotti.ebay.helper.enumeration;

public enum ReturnShippingCostPayerEnum {
    BUYER ("Buyer"),
    SELLER ("Seller");
    private String code;
    ReturnShippingCostPayerEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
