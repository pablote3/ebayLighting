package com.rossotti.ebay.model.account.paymentPolicy;

import lombok.Getter;

@Getter
public class RecipientAccountReference {
    private RecipientAccountReferenceTypeEnum referenceType;
    private String referenceId;
}
