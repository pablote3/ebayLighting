package com.rossotti.ebay.service;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.paymentPolicy.PaymentPolicies;
import com.rossotti.ebay.model.paymentPolicy.PaymentPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PaymentPolicyService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountUrl = "sell/account/v1/";
    private static final String paymentPolicyUrl = "payment_policy";

    public PaymentPolicyService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public PaymentPolicy getPaymentPolicy(String paymentPolicyId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(paymentPolicyUrl)
                .path("/" + paymentPolicyId)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(PaymentPolicy.class)
                .block();
    }

    public PaymentPolicies getPaymentPolicies() {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(paymentPolicyUrl)
                .queryParam("marketplace_id", properties.getMarketplaceId())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(PaymentPolicies.class)
                .block();
    }
}
