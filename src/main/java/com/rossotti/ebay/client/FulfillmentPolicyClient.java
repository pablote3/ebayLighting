package com.rossotti.ebay.client;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class FulfillmentPolicyClient extends BaseClient {
    private final WebClientProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(FulfillmentPolicyClient.class);

    public FulfillmentPolicyClient(WebClient webClient, WebClientProperties properties, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.properties = properties;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<FulfillmentPolicy> getByFulfillmentPolicyId(final String fulfillmentPolicyId) {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(fulfillmentPolicyId)) {
            builder.path("/" + fulfillmentPolicyId);
        }
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicy.class);
    }

    public Optional<FulfillmentPolicies> getFulfillmentPolicies() {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicies.class);
    }
}
