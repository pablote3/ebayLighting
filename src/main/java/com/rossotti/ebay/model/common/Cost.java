package com.rossotti.ebay.model.common;

import com.rossotti.ebay.helper.enumeration.CurrencyCodeEnum;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Cost {
    private BigDecimal value;
    private CurrencyCodeEnum currency;
}
