package com.rossotti.ebay.model.returnPolicy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReturnPolicies {
    private Integer total;
    private List<ReturnPolicy> returnPolicies = new ArrayList<>();
}
