package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
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
    private WebClientProperties properties;
    private static final String pathKey = "fulfillment_policy";
    private static final Logger logger = LoggerFactory.getLogger(FulfillmentPolicyClient.class);

    public FulfillmentPolicyClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<FulfillmentPolicy> getByFulfillmentPolicyId(final String fulfillmentPolicyId) {
        properties = createWebClientProperties(pathKey);
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
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicies.class);
    }
    public Optional<FulfillmentPolicy> create(final FulfillmentPolicy fulfillmentPolicy) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.POST);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicy.class, fulfillmentPolicy);
    }
    public Optional<FulfillmentPolicy> update(final FulfillmentPolicy fulfillmentPolicy, final String fulfillmentPolicyId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(fulfillmentPolicyId)) {
            builder.path("/" + fulfillmentPolicyId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.PUT);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicy.class, fulfillmentPolicy);
    }
    public Optional<FulfillmentPolicy> delete(final String fulfillmentPolicyId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(fulfillmentPolicyId)) {
            builder.path("/" + fulfillmentPolicyId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.DELETE);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, FulfillmentPolicy.class);
    }
}
