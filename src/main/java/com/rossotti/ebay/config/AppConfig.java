package com.rossotti.ebay.config;

import com.rossotti.ebay.helper.enumeration.MarketplaceIdEnum;
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
    private MarketplaceIdEnum marketplaceId;
    private int httpTimeOutMs;
    private int maxRetries;
    private int backoffInterval;
    private Map<String, String> resourceMap;
}
