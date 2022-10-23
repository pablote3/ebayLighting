package com.rossotti.ebay.model.account.fulfillmentPolicy;

import com.rossotti.ebay.helper.enumeration.CurrencyCodeEnum;
import lombok.Getter;

@Getter
public class InsuranceFee {
    private String value;
    private CurrencyCodeEnum currency;
}
