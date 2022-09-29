package com.rossotti.ebay.model;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
@Getter
public class PaymentPolicy {

    private String name;
    private String description;
    private String marketplaceId;
    private List<CategoryType> categoryTypes = new ArrayList<>();
//    private List<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();
//    private FullPaymentDueIn fullPaymentDueIn;
    private Boolean immediatePay;
    private String paymentPolicyId;
}