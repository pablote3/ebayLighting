package com.rossotti.ebay.model.inventory.inventoryItem;

import com.rossotti.ebay.model.common.WeightUnitOfMeasureEnum;
import lombok.Getter;

@Getter
public class Weight {
    private Double value;
    private WeightUnitOfMeasureEnum unit;
}