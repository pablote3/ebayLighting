package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.client.util.QueryParam;
import com.rossotti.ebay.client.util.QueryParamEnum;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.util.WebClientProperties;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReturnPolicyClient extends BaseClient {
    private static final String pathKey = "return_policy";
    public ReturnPolicyClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }
    public Optional<ReturnPolicy> getByReturnPolicyId(final String returnPolicyId) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, returnPolicyId, queryParams);
        return webClientCall(properties, ReturnPolicy.class);
    }
    public Optional<ReturnPolicies> getReturnPolicies() {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, null, queryParams);
        return webClientCall(properties, ReturnPolicies.class);
    }
    public Optional<ReturnPolicy> create(final ReturnPolicy returnPolicy) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.POST, null, queryParams);
        return webClientCall(properties, ReturnPolicy.class, returnPolicy);
    }
    public Optional<ReturnPolicy> update(final ReturnPolicy returnPolicy, final String returnPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.PUT, returnPolicyId, null);
        return webClientCall(properties, ReturnPolicy.class, returnPolicy);
    }
    public Optional<ReturnPolicy> delete(final String returnPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.DELETE, returnPolicyId, null);
        return webClientCall(properties, ReturnPolicy.class);
    }
}