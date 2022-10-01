package com.rossotti.ebay.model.fulfillmentPolicy;

import lombok.Getter;

@Getter
public class ShippingService {
    private Integer sortOrder;
    private String shippingCarrierCode;
    private String shippingServiceCode;
    private Boolean freeShipping;
    private Boolean buyerResponsibleForShipping;
    private Boolean buyerResponsibleForPickup;
}
