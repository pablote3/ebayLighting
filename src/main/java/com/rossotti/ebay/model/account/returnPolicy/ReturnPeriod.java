package com.rossotti.ebay.model.account.returnPolicy;

import com.rossotti.ebay.helper.enumeration.TimeDurationUnitEnum;
import lombok.Getter;

@Getter
public class ReturnPeriod {
    private Integer value;
    private TimeDurationUnitEnum unit;
}
