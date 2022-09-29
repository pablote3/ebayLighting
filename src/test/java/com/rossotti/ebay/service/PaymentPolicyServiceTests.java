package com.rossotti.ebay.service;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.PaymentPolicies;
import com.rossotti.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;

@SpringBootTest
public class PaymentPolicyServiceTests {
    private static final String PAYMENT_POLICIES_JSON = "data/paymentPolicies.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private PaymentPolicyService paymentPolicyService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"));
        paymentPolicyService = new PaymentPolicyService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getPaymentPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        paymentPolicyService.getPaymentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/payment_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void getPaymentPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        PaymentPolicies response = paymentPolicyService.getPaymentPolicies();

        Assertions.assertEquals(1, response.getTotal());
        Assertions.assertEquals("eBay Payments EBAY_US PayPal", response.getPaymentPolicies().get(0).getName());
        Assertions.assertEquals("eBay Payments EBAY_US PayPal", response.getPaymentPolicies().get(0).getName());
    }
}
