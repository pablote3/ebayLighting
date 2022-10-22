package com.rossotti.ebay.model.account.paymentPolicy;

import com.rossotti.ebay.helper.enumeration.MarketplaceIdEnum;
import com.rossotti.ebay.model.common.CategoryType;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
@Getter
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