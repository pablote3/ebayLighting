package com.rossotti.ebay.model.account.paymentPolicy;

import com.rossotti.ebay.helper.enumeration.PaymentInstrumentBrandEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaymentMethod {
    private String paymentMethodType;
    private RecipientAccountReference recipientAccountReference;
    private List<PaymentInstrumentBrandEnum> brands = new ArrayList<>();
}