package com.rossotti.ebay.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ebay")
public class AppConfig {
    @Value("ebay.marketplaceId")
    @Getter
    private String marketplaceId;
}
