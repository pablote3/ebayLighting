package com.rossotti.ebay.client;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class PaymentPolicyClient extends BaseClient {
    private final WebClient webClient;
    private final WebClientProperties properties;

    private static final String pathKey = "payment_policy";
    @Autowired
    private AppConfig appConfig;

    public PaymentPolicyClient(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public Optional<PaymentPolicy> retrieveByPaymentPolicyId(final String paymentPolicyId) {
        WebClientProperties webClientProperties = new WebClientProperties();
        UriComponentsBuilder builder = baseUriComponentBuilder(webClientProperties);
        if (isNotBlank(paymentPolicyId)) {
            builder.path("/" + paymentPolicyId);
        }
        builder.queryParam("marketplace_id", getAppConfig().getMarketplaceId());
        builder.build();
        HttpHeaders headers = createHeaders();
        return Optional.ofNullable(webClient
                .get()
                .uri(webClientProperties.getUri())
                .retrieve()
                .bodyToMono(PaymentPolicy.class)
                .block());
    }
}
