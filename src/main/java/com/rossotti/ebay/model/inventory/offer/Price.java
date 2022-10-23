package com.rossotti.ebay.model.inventory.offer;

import com.rossotti.ebay.helper.enumeration.CurrencyCodeEnum;
import lombok.Getter;

@Getter
public class Price {
    private String value;
    private CurrencyCodeEnum currency;
}