package com.rossotti.ebay.model.inventory.inventoryItem;

import lombok.Getter;

@Getter
public class InventoryItem {
    private String sku;
    private String locale;
    private Product product;
    private String condition;
    private PackageWeightAndSize packageWeightAndSize;
    private Availability availability;
}