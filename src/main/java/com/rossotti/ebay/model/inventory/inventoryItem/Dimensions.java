package com.rossotti.ebay.model.inventory.inventoryItem;

import com.rossotti.ebay.model.common.LengthUnitOfMeasureEnum;
import lombok.Getter;

@Getter
public class Dimensions {
    private Double width;
    private Double length;
    private Double height;
    private LengthUnitOfMeasureEnum unit;
}