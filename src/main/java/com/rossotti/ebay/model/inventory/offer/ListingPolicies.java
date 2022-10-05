package com.rossotti.ebay.model.inventory.offer;

import lombok.Getter;

@Getter
public class ListingPolicies {
    private String paymentPolicyId;
    private String returnPolicyId;
    private String fulfillmentPolicyId;
    private Boolean eBayPlusIfEligible;
}