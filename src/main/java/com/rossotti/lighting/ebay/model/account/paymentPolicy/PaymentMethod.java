package com.rossotti.lighting.ebay.model.account.paymentPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaymentMethod {
    private PaymentMethodTypeEnum paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
    private final List<PaymentInstrumentBrandEnum> brands = new ArrayList<>();
}