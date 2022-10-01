package com.rossotti.ebay.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
@Getter
public class CategoryType {
    public String name;

    @JsonProperty("default")
    private Boolean defaultValue;
}