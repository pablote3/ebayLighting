package com.rossotti.ebay.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
public class PaymentPolicy {
    @Getter
    @Setter
    private String name;
    private String description;
    private String marketplaceId;
//    private List<CategoryType> categoryTypes = new ArrayList<CategoryType>();
//    private List<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();
//    private FullPaymentDueIn fullPaymentDueIn;
    private Boolean immediatePay;
    private String paymentPolicyId;
}