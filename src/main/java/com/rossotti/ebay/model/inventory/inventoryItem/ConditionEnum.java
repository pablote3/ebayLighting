package com.rossotti.ebay.model.inventory.inventoryItem;

public enum ConditionEnum {
    NEW ("New"),
    LIKE_NEW ("Like New"),
    CERTIFIED_REFURBISHED ("Certified Refurbished"),
    VERY_GOOD_REFURBISHED ("Very Good Refurbished"),
    GOOD_REFURBISHED ("Good Refurbished"),
    SELLER_REFURBISHED ("Seller Refurbished"),
    USED_EXCELLENT ("Used Excellent"),
    USED_VERY_GOOD ("Used Very Good"),
    USED_ACCEPTABLE ("Used Acceptable"),
    FOR_PARTS_OR_NOT_WORKING ("For Parts Or Not Working");
    private String code;
    ConditionEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
