package com.rossotti.ebay.model.inventory.offer;

import com.rossotti.ebay.model.common.MarketplaceIdEnum;
import lombok.Getter;

@Getter
public class Offer {
    private String offerId;
    private String sku;
    private MarketplaceIdEnum marketplaceId;
    private String format;
    private String listingDescription;
    private Integer availableQuantity;
    private PricingSummary pricingSummary;
    private ListingPolicies listingPolicies;
    private String categoryId;
    private String merchantLocationKey;
    private Tax tax;
    private Listing listing;
    private String status;
    private String listingDuration;
    private Boolean includeCatalogProductDetails;
    private Boolean hideBuyerDetails;
}