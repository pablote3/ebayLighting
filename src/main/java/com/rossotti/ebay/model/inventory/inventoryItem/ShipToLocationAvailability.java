package com.rossotti.ebay.model.inventory.inventoryItem;

import lombok.Getter;

@Getter
public class ShipToLocationAvailability {
    private Integer quantity;
    private AllocationByFormat allocationByFormat;
}