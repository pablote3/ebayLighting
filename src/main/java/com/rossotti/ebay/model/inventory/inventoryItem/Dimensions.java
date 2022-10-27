package com.rossotti.ebay.model.inventory.inventoryItem;

import lombok.Getter;

@Getter
public class Dimensions {
    private Double width;
    private Double length;
    private Double height;
    private LengthUnitOfMeasureEnum unit;
}