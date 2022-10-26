package com.rossotti.ebay.model.inventory.offer;

import com.rossotti.ebay.model.common.CurrencyCodeEnum;
import lombok.Getter;

@Getter
public class Price {
    private String value;
    private CurrencyCodeEnum currency;
}