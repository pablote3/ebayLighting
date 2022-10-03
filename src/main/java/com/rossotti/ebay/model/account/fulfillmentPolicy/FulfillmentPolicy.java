package com.rossotti.ebay.model.account.fulfillmentPolicy;

import java.util.List;
import com.rossotti.ebay.model.common.CategoryType;
import lombok.Getter;

@Getter
public class FulfillmentPolicy {
    private String name;
    private String description;
    private String marketplaceId;
    private List<CategoryType> categoryTypes = null;
    private HandlingTime handlingTime;
    private List<ShippingOption> shippingOptions = null;
    private Boolean globalShipping;
    private Boolean pickupDropOff;
    private Boolean freightShipping;
    private String fulfillmentPolicyId;
}
