package com.rossotti.ebay.model.account.fulfillmentPolicy;

import java.util.List;

import com.rossotti.ebay.helper.enumeration.ShippingCostTypeEnum;
import lombok.Getter;

@Getter
public class ShippingOption {
    private String optionType;
    private ShippingCostTypeEnum costType;
    private List<ShippingService> shippingServices = null;
    private Boolean insuranceOffered;
    private InsuranceFee insuranceFee;
}
