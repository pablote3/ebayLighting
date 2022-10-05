package com.rossotti.ebay.service.account;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicies;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PaymentPolicyService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountPath = "sell/account/v1/";
    private static final String paymentPolicyPath = "payment_policy";

    public PaymentPolicyService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public PaymentPolicy getPaymentPolicy(String paymentPolicyId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountPath)
                .path(paymentPolicyPath)
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
                .path(sellAccountPath)
                .path(paymentPolicyPath)
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
