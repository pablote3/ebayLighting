package com.rossotti.ebay.model.PaymentPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PaymentPolicies {
    private Integer total;
    private List<PaymentPolicy> paymentPolicies = new ArrayList<>();
}