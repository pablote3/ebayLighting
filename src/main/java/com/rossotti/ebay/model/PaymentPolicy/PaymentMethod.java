package com.rossotti.ebay.model.PaymentPolicy;

import lombok.Getter;

@Getter
public class PaymentMethod {
    private String paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
}