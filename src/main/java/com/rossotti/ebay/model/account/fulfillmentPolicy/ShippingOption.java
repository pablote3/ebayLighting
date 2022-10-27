package com.rossotti.ebay.model.account.fulfillmentPolicy;

import java.util.List;

import lombok.Getter;

@Getter
public class ShippingOption {
    private OptionTypeEnum optionType;
    private ShippingCostTypeEnum costType;
    private Cost packageHandlingCost;
    private List<ShippingService> shippingServices = null;
    private Boolean insuranceOffered;
    private Cost insuranceFee;
}
