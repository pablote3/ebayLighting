package com.rossotti.lighting.ebay.model.account.returnPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReturnPolicies {
    private Integer total;
    private final List<ReturnPolicy> returnPolicies = new ArrayList<>();
}
