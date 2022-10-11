package com.rossotti.ebay.service.account;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ReturnPolicyService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountPath = "sell/account/v1/";
    private static final String returnPolicyPath = "return_policy";

    public ReturnPolicyService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public ReturnPolicy getReturnPolicy(String returnPolicyId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountPath)
                .path(returnPolicyPath)
                .path("/" + returnPolicyId)
                .queryParam("marketplace_id", "EBAY_US")
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
                .path(sellAccountPath)
                .path(returnPolicyPath)
                .queryParam("marketplace_id", "EBAY_US")
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(ReturnPolicies.class)
                .block();
    }
}
