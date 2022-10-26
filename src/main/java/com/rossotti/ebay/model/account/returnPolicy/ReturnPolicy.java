package com.rossotti.ebay.model.account.returnPolicy;

import com.rossotti.ebay.model.common.MarketplaceIdEnum;
import com.rossotti.ebay.model.common.CategoryType;
import lombok.Getter;
import java.util.List;

@Getter
public class ReturnPolicy {
    private String name;
    private String description;
    private MarketplaceIdEnum marketplaceId;
    private List<CategoryType> categoryTypes = null;
    private Boolean returnsAccepted;
    private ReturnPeriod returnPeriod;
    private RefundMethodEnum refundMethod;
    private ReturnMethodEnum returnMethod;
    private ReturnShippingCostPayerEnum returnShippingCostPayer;
    private String returnPolicyId;
}
