package com.rossotti.ebay.client;

import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

public class PaymentPolicyClient extends BaseClient {
    public Optional<PaymentPolicy> retrieveByPaymentPolicyId(final String paymentPolicyId) {
        UriComponentsBuilder builder = fromUriString(createUri())
    }
}
