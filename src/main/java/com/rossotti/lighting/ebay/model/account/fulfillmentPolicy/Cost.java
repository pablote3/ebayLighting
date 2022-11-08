package com.rossotti.lighting.ebay.model.account.fulfillmentPolicy;

import com.rossotti.lighting.ebay.model.common.CurrencyCodeEnum;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Cost {
    private BigDecimal value;
    private CurrencyCodeEnum currency;
}
