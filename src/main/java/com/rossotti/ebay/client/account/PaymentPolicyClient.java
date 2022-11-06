package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.client.util.QueryParam;
import com.rossotti.ebay.client.util.QueryParamEnum;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.util.WebClientProperties;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicies;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class PaymentPolicyClient extends BaseClient {
    private WebClientProperties properties;
    private static final String pathKey = "payment_policy";
    private static final Logger logger = LoggerFactory.getLogger(PaymentPolicyClient.class);

    public PaymentPolicyClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<PaymentPolicy> getByPaymentPolicyId(final String paymentPolicyId) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, paymentPolicyId, queryParams);
        return webClientCall(properties, PaymentPolicy.class);
    }

    public Optional<PaymentPolicies> getPaymentPolicies() {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, null, queryParams);
        return webClientCall(properties, PaymentPolicies.class);
    }
    public Optional<PaymentPolicy> create(final PaymentPolicy paymentPolicy) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.MARKETPLACE_ID, appConfig.getMarketplaceId().getCode()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.POST, null, queryParams);
        return webClientCall(properties, PaymentPolicy.class, paymentPolicy);
    }
    public Optional<PaymentPolicy> update(final PaymentPolicy paymentPolicy, final String paymentPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.PUT, paymentPolicyId, null);
        return webClientCall(properties, PaymentPolicy.class, paymentPolicy);
    }
    public Optional<PaymentPolicy> delete(final String paymentPolicyId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.DELETE, paymentPolicyId, null);
        return webClientCall(properties, PaymentPolicy.class);
    }
}