package com.rossotti.ebay.client;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class PaymentPolicyClient extends BaseClient {
    private final WebClient webClient;
    private final WebClientProperties properties;

    public PaymentPolicyClient(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public Optional<PaymentPolicy> retrieveByPaymentPolicyId(final String paymentPolicyId) {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(paymentPolicyId)) {
            builder.path("/" + paymentPolicyId + "/");
        }
        builder.queryParam("marketplace_id", properties.getMarketplaceId());
        builder.build();
        properties.setHeaders(createHeaders(properties));
        return Optional.ofNullable(webClient
                .get()
                .uri(properties.getUri())
                .retrieve()
                .bodyToMono(PaymentPolicy.class)
                .block());
    }
}
