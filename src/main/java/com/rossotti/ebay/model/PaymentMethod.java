package com.rossotti.ebay.model;

import lombok.Getter;

@Getter
public class PaymentMethod {
    private String paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
}