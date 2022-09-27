package com.rossotti.ebay;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.PaymentPoliciesResponse;
import com.rossotti.ebay.service.PaymentPolicyService;
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
import java.io.InputStream;

@SpringBootTest
public class PaymentPolicyServiceTests {
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private PaymentPolicyService paymentPolicyService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = new WebClientProperties();
        properties.setBaseUrl(mockWebServer.url("/").url().toString());
        properties.setMarketplaceId("EBAY_US");
        paymentPolicyService = new PaymentPolicyService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getPaymentPolicies_requestSerialization() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getJson("paymentPolicies-response.json"))
        );
        paymentPolicyService.getPaymentPoliciesResponse();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/payment_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void getPaymentPolicies_responseDeserialization() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(getJson("paymentPolicies-response.json"))
        );
        PaymentPoliciesResponse response = paymentPolicyService.getPaymentPoliciesResponse();

        Assertions.assertEquals(1, response.getTotal());
    }

    private String getJson(String fileName) {
        try (InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
