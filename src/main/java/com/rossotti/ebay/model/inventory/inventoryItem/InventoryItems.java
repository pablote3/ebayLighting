package com.rossotti.ebay.model.inventory.inventoryItem;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class InventoryItems {
    private Integer total;
    private Integer size;
    private String href;
    private Integer limit;
    private List<InventoryItem> inventoryItems = new ArrayList<>();
}