package com.rossotti.lighting.ebay.model.account.fulfillmentPolicy;

import java.util.List;

import com.rossotti.lighting.ebay.model.common.MarketplaceIdEnum;
import com.rossotti.lighting.ebay.model.common.CategoryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FulfillmentPolicy {
    private String name;
    private String description;
    private MarketplaceIdEnum marketplaceId;
    private List<CategoryType> categoryTypes = null;
    private HandlingTime handlingTime;
    private List<ShippingOption> shippingOptions = null;
    private Boolean globalShipping;
    private Boolean pickupDropOff;
    private Boolean freightShipping;
    private String fulfillmentPolicyId;
}
