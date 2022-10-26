package com.rossotti.ebay.model.account.paymentPolicy;

import com.rossotti.ebay.model.common.TimeDurationUnitEnum;
import lombok.Getter;

@Getter
public class FullPaymentDueIn {
    private Integer value;
    private TimeDurationUnitEnum unit;
}