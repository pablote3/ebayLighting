package com.rossotti.ebay.model.inventory.offer;

import com.rossotti.ebay.model.common.MarketplaceIdEnum;
import lombok.Getter;

import java.text.Normalizer;

@Getter
public class Offer {
    private String offerId;
    private String sku;
    private MarketplaceIdEnum marketplaceId;
    private FormatTypeEnum format;
    private String listingDescription;
    private Integer availableQuantity;
    private PricingSummary pricingSummary;
    private ListingPolicies listingPolicies;
    private String categoryId;
    private String merchantLocationKey;
    private Tax tax;
    private Listing listing;
    private OfferStatusEnum status;
    private ListingDurationEnum listingDuration;
    private Boolean includeCatalogProductDetails;
    private Boolean hideBuyerDetails;
}