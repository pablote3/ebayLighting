package com.rossotti.ebay.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ebay")
@Getter
public class AppConfig {
    @Value("${ebay.marketplaceId}")
    private String marketplaceId;

    @Value("${ebay.httpTimeOutMs}")
    private int httpTimeOutMs;

    @Value("${retry.maxRetries}")
    private int maxRetries;

    @Value("${retry.backoffInterval}")
    private int backoffInterval;


}
