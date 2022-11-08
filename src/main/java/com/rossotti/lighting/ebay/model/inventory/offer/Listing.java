package com.rossotti.lighting.ebay.model.inventory.offer;

import lombok.Getter;

@Getter
public class Listing {
    private String listingId;
    private ListingStatusEnum listingStatus;
    private Integer soldQuantity;
}