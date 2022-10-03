package com.rossotti.ebay.model.inventory.inventoryItem;

import lombok.Getter;

@Getter
public class PackageWeightAndSize {
    private Dimensions dimensions;
    private Weight weight;
}