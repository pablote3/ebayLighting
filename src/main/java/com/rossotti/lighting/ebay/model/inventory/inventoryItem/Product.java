package com.rossotti.lighting.ebay.model.inventory.inventoryItem;

import java.util.List;
import lombok.Getter;

@Getter
public class Product {
    private String title;
    private Aspects aspects;
    private String description;
    private String brand;
    private String mpn;
    private final List<String> imageUrls = null;
}