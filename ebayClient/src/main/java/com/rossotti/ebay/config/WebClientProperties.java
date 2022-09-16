package com.rossotti.ebay.config;

import org.springframework.context.annotation.Configuration;
@Configuration
public class WebClientProperties {
    public WebClientProperties() {}

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
