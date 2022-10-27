package com.rossotti.ebay.model.account.fulfillmentPolicy;

import com.rossotti.ebay.model.common.CurrencyCodeEnum;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Cost {
    private BigDecimal value;
    private CurrencyCodeEnum currency;
}
