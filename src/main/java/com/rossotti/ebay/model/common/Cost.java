package com.rossotti.ebay.model.common;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Cost {
    private BigDecimal value;
    private CurrencyCodeEnum currency;
}
