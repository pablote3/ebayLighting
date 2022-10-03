package com.rossotti.ebay.service.account;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FulfillmentPolicyService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountUrl = "sell/account/v1/";
    private static final String fulfillmentPolicyUrl = "fulfillment_policy";

    public FulfillmentPolicyService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public FulfillmentPolicy getFulfillmentPolicy(String paymentPolicyId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(fulfillmentPolicyUrl)
                .path("/" + paymentPolicyId)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(FulfillmentPolicy.class)
                .block();
    }

    public FulfillmentPolicies getFulfillmentPolicies() {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(fulfillmentPolicyUrl)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(FulfillmentPolicies.class)
                .block();
    }
}
