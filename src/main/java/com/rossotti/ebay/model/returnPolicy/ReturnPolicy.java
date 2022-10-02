package com.rossotti.ebay.model.returnPolicy;

import com.rossotti.ebay.model.common.CategoryType;
import lombok.Getter;
import java.util.List;

@Getter
public class ReturnPolicy {
    private String name;
    private String description;
    private String marketplaceId;
    private List<CategoryType> categoryTypes = null;
    private Boolean returnsAccepted;
    private ReturnPeriod returnPeriod;
    private String refundMethod;
    private String returnMethod;
    private String returnShippingCostPayer;
    private String returnPolicyId;
}
