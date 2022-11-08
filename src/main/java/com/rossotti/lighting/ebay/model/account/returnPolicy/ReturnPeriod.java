package com.rossotti.lighting.ebay.model.account.returnPolicy;

import com.rossotti.lighting.ebay.model.common.TimeDurationUnitEnum;
import lombok.Getter;

@Getter
public class ReturnPeriod {
    private Integer value;
    private TimeDurationUnitEnum unit;
}
