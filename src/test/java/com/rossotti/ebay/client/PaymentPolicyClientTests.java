package com.rossotti.ebay.client;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PaymentPolicyClientTests {
    private static final String PAYMENT_POLICY_JSON = "data/account/paymentPolicy.json";
    private static final String PAYMENT_POLICIES_JSON = "data/account/paymentPolicies.json";
    private static final String pathKey = "payment_policy";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private PaymentPolicyClient paymentPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, pathKey);
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        paymentPolicyClient = new PaymentPolicyClient(WebClient.create(), properties, appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void paymentPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(PAYMENT_POLICY_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        paymentPolicyClient.retrieveByPaymentPolicyId("6196932000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/payment_policy/6196932000?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void paymentPolicy_responseDeserialization() {
        String json = TestUtil.readStringFromFile(PAYMENT_POLICY_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<PaymentPolicy> response = paymentPolicyClient.retrieveByPaymentPolicyId("6196932000");

        assertTrue(response.isPresent());
        assertEquals("eBay Payments EBAY_US PayPal", response.get().getName());
        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getCategoryTypes().get(0).getName());
        assertTrue(response.get().getCategoryTypes().get(0).getDefaultValue());
        assertEquals("PAYPAL", response.get().getPaymentMethods().get(0).getPaymentMethodType());
        assertEquals("PAYPAL_EMAIL", response.get().getPaymentMethods().get(0).getRecipientAccountReference().getReferenceType());
        assertEquals("DAY", response.get().getFullPaymentDueIn().getUnit());
    }

//    @Test
//    void paymentPolicies_requestSerialization() throws InterruptedException {
//        String str = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
//        assertNotNull(str);
//        mockWebServer.enqueue(
//                new MockResponse()
//                        .setResponseCode(200)
//                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                        .setBody(str)
//        );
//        paymentPolicyClient.getPaymentPolicies();
//        RecordedRequest request = mockWebServer.takeRequest();
//
//        assertEquals("GET", request.getMethod());
//        assertEquals("/sell/account/v1/payment_policy?marketplace_id=EBAY_US", request.getPath());
//    }

//    @Test
//    void paymentPolicies_responseDeserialization() {
//        String json = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
//        assertNotNull(json);
//        mockWebServer.enqueue(
//                new MockResponse()
//                        .setResponseCode(200)
//                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                        .setBody(json)
//        );
//        PaymentPolicies response = paymentPolicyClient.getPaymentPolicies();
//
//        assertTrue(response.isPresent());
//        assertEquals(1, response.getTotal());
//        assertEquals("eBay Payments EBAY_US PayPal", response.getPaymentPolicies().get(0).getName());
//        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.getPaymentPolicies().get(0).getCategoryTypes().get(0).getName());
//        assertTrue(response.getPaymentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
//        assertEquals("PAYPAL", response.getPaymentPolicies().get(0).getPaymentMethods().get(0).getPaymentMethodType());
//        assertEquals("PAYPAL_EMAIL", response.getPaymentPolicies().get(0).getPaymentMethods().get(0).getRecipientAccountReference().getReferenceType());
//        assertEquals("DAY", response.getPaymentPolicies().get(0).getFullPaymentDueIn().getUnit());
//    }
}
