package com.rossotti.ebay.service;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.returnPolicy.ReturnPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ReturnPolicyService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountUrl = "sell/account/v1/";
    private static final String returnPolicyUrl = "return_policy";

    public ReturnPolicyService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public ReturnPolicy getReturnPolicy(String returnPolicyId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(returnPolicyUrl)
                .path("/" + returnPolicyId)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(ReturnPolicy.class)
                .block();
    }

    public ReturnPolicies getReturnPolicies() {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(returnPolicyUrl)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(ReturnPolicies.class)
                .block();
    }
}
