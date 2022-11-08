package com.rossotti.lighting.ebay.client.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryParam {
    public QueryParam(QueryParamEnum name, String value) {
        this.name = name;
        this.value = value;
    }
    private QueryParamEnum name;
    private String value;
}