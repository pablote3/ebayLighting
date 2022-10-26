package com.rossotti.ebay.model.account.paymentPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaymentMethod {
    private PaymentMethodTypeEnum paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
    private List<PaymentInstrumentBrandEnum> brands = new ArrayList<>();
}