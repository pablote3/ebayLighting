package com.rossotti.lighting.ebay.model.account.fulfillmentPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FulfillmentPolicies {
    private Integer total;
    private final List<FulfillmentPolicy> fulfillmentPolicies = new ArrayList<>();
}
