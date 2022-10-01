package com.rossotti.ebay.model.paymentPolicy;

import lombok.Getter;

@Getter
public class PaymentMethod {
    private String paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
}