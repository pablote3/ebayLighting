package com.rossotti.ebay.model.account.program;

public enum ProgramTypeEnum {
    OUT_OF_STOCK_CONTROL ("Out Of Stock Control"),
    SELLING_POLICY_MANAGEMENT ("Selling Policy Management");
    private String code;
    ProgramTypeEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
