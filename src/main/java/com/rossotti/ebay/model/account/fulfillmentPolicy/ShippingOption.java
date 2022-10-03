package com.rossotti.ebay.model.account.fulfillmentPolicy;

import java.util.List;
import lombok.Getter;

@Getter
public class ShippingOption {
    private String optionType;
    private String costType;
    private List<ShippingService> shippingServices = null;
    private Boolean insuranceOffered;
    private InsuranceFee insuranceFee;
}
