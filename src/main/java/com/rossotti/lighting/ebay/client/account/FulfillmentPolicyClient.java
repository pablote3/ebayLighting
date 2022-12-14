package com.rossotti.lighting.ebay.client.account;

import com.rossotti.lighting.ebay.client.BaseClient;
import com.rossotti.lighting.ebay.model.webClient.QueryParam;
import com.rossotti.lighting.ebay.model.webClient.QueryParamEnum;
import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.webClient.WebClientProperties;
import com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FulfillmentPolicyClient extends BaseClient {
    private static final String pathKey = "fulfillment_policy";

    public FulfillmentPolicyClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<FulfillmentPolicy> getByFulfillmentPolicyId(final String fulfillmentPolicyId) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, fulfillmentPolicyId, queryParams);
        return webClientCall(properties, FulfillmentPolicy.class);
    }
    public Optional<FulfillmentPolicies> getFulfillmentPolicies() {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, null, queryParams);
        return webClientCall(properties, FulfillmentPolicies.class);
    }
    public Optional<FulfillmentPolicy> create(final FulfillmentPolicy fulfillmentPolicy) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.POST, null, queryParams);
        return webClientCall(properties, FulfillmentPolicy.class, fulfillmentPolicy);
    }
    public Optional<FulfillmentPolicy> update(final FulfillmentPolicy fulfillmentPolicy, final String fulfillmentPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.PUT, fulfillmentPolicyId, null);
        return webClientCall(properties, FulfillmentPolicy.class, fulfillmentPolicy);
    }
    public Optional<FulfillmentPolicy> delete(final String fulfillmentPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.DELETE, fulfillmentPolicyId, null);
        return webClientCall(properties, FulfillmentPolicy.class);
    }
}
