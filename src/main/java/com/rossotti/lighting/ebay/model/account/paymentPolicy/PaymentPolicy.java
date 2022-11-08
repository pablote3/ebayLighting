package com.rossotti.lighting.ebay.model.account.paymentPolicy;

import com.rossotti.lighting.ebay.model.common.MarketplaceIdEnum;
import com.rossotti.lighting.ebay.model.common.CategoryType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class PaymentPolicy {
    private String name;
    private String description;
    private MarketplaceIdEnum marketplaceId;
    private List<CategoryType> categoryTypes = new ArrayList<>();
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private FullPaymentDueIn fullPaymentDueIn;
    private Boolean immediatePay;
    private String paymentPolicyId;
}