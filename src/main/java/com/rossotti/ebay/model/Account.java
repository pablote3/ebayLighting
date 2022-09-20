package com.rossotti.ebay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
}
