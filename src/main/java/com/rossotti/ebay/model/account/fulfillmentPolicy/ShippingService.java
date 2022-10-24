package com.rossotti.ebay.model.account.fulfillmentPolicy;

import com.rossotti.ebay.model.common.Cost;
import lombok.Getter;

@Getter
public class ShippingService {
    private Integer sortOrder;
    private String shippingCarrierCode;
    private String shippingServiceCode;
    private Cost shippingCost;
    private Boolean freeShipping;
    private Boolean buyerResponsibleForShipping;
    private Boolean buyerResponsibleForPickup;
}
