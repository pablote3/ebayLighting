package com.rossotti.ebay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import java.util.Map;

@ConfigurationProperties(prefix="ebay")
@ConfigurationPropertiesScan
@Getter
@Setter
public class AppConfig {
    private String contentType;
    private String contentLanguage;
    private String marketplaceId;
    private int httpTimeOutMs;
    private int maxRetries;
    private int backoffInterval;
    private Map<String, String> resourceMap;
}
