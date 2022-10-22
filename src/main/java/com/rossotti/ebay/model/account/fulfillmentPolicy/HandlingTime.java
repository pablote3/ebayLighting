package com.rossotti.ebay.model.account.fulfillmentPolicy;

import com.rossotti.ebay.helper.enumeration.TimeDurationUnitEnum;
import lombok.Getter;

@Getter
public class HandlingTime {
    private Integer value;
    private TimeDurationUnitEnum unit;
}
