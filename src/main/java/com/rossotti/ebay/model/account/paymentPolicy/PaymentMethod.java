package com.rossotti.ebay.model.account.paymentPolicy;

import lombok.Getter;

@Getter
public class PaymentMethod {
    private String paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
}