package com.rossotti.ebay.model.inventory.offer;

import lombok.Getter;

@Getter
public class Listing {
    private String listingId;
    private String listingStatus;
    private Integer soldQuantity;
}