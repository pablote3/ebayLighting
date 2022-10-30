package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
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
    private WebClientProperties properties;
    private static final String pathKey = "return_policy";
    private static final Logger logger = LoggerFactory.getLogger(ReturnPolicyClient.class);

    public ReturnPolicyClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }
    public Optional<ReturnPolicy> getByReturnPolicyId(final String returnPolicyId) {
        properties = createWebClientProperties(pathKey);
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
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicies.class);
    }
    public Optional<ReturnPolicy> create(final ReturnPolicy returnPolicy) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.POST);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicy.class, returnPolicy);
    }
    public Optional<ReturnPolicy> update(final ReturnPolicy returnPolicy, final String returnPolicyId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(returnPolicyId)) {
            builder.path("/" + returnPolicyId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.PUT);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicy.class, returnPolicy);
    }
    public Optional<ReturnPolicy> delete(final String returnPolicyId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(returnPolicyId)) {
            builder.path("/" + returnPolicyId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.DELETE);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, ReturnPolicy.class);
    }
}
