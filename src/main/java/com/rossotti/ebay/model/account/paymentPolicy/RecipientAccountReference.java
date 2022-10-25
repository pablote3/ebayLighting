package com.rossotti.ebay.model.account.paymentPolicy;

import com.rossotti.ebay.helper.enumeration.RecipientAccountReferenceTypeEnum;
import lombok.Getter;

@Getter
public class RecipientAccountReference {
    private RecipientAccountReferenceTypeEnum referenceType;
    private String referenceId;
}
