package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class ReturnPolicyClient extends BaseClient {
    private final WebClientProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(ReturnPolicyClient.class);

    public ReturnPolicyClient(WebClient webClient, WebClientProperties properties, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.properties = properties;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<ReturnPolicy> getByReturnPolicyId(final String returnPolicyId) {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(returnPolicyId)) {
            builder.path("/" + returnPolicyId);
        }
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicy.class);
    }

    public Optional<ReturnPolicies> getReturnPolicies() {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicies.class);
    }
}
