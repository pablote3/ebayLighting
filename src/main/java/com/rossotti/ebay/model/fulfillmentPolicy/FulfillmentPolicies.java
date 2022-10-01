package com.rossotti.ebay.model.fulfillmentPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FulfillmentPolicies {
    private Integer total;
    private List<FulfillmentPolicy> fulfillmentPolicies = new ArrayList<>();
}
