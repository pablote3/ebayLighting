package com.rossotti.lighting.ebay.model.inventory.inventoryItem;

import lombok.Getter;

@Getter
public class InventoryItem {
    private String sku;
    private LocaleEnum locale;
    private Product product;
    private ConditionEnum condition;
    private PackageWeightAndSize packageWeightAndSize;
    private Availability availability;
}