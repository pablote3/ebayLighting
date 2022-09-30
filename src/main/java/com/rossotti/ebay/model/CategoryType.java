package com.rossotti.ebay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
@Getter
public class CategoryType {
    public String name;

    @JsonProperty("default")
    private Boolean defaultValue;
}