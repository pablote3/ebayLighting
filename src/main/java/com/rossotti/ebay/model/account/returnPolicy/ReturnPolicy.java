package com.rossotti.ebay.model.account.returnPolicy;

import com.rossotti.ebay.helper.enumeration.MarketplaceIdEnum;
import com.rossotti.ebay.helper.enumeration.RefundMethodEnum;
import com.rossotti.ebay.helper.enumeration.ReturnMethodEnum;
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
    private String returnShippingCostPayer;
    private String returnPolicyId;
}
