package com.rossotti.ebay.service;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.ebay.model.fulfillmentPolicy.FulfillmentPolicy;
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
public class FulfillmentPolicyServiceTests {
    private static final String FULFILLMENT_POLICY_JSON = "data/fulfillmentPolicy.json";
    private static final String FULFILLMENT_POLICIES_JSON = "data/fulfillmentPolicies.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private FulfillmentPolicyService fulfillmentPolicyService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"));
        fulfillmentPolicyService = new FulfillmentPolicyService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fulfillmentPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyService.getFulfillmentPolicy("6196947000");
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/fulfillment_policy/6196947000?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void fulfillmentPolicy_responseDeserialization() {
        String json = TestUtil.readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        FulfillmentPolicy response = fulfillmentPolicyService.getFulfillmentPolicy("6196947000");

        Assertions.assertEquals("eBay Fulfillments EBAY_US", response.getName());
//        Assertions.assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.getCategoryTypes().get(0).getName());
//        Assertions.assertTrue(response.getCategoryTypes().get(0).getDefaultValue());
//        Assertions.assertEquals("PAYPAL", response.getFulfillmentMethods().get(0).getFulfillmentMethodType());
//        Assertions.assertEquals("PAYPAL_EMAIL", response.getFulfillmentMethods().get(0).getRecipientAccountReference().getReferenceType());
//        Assertions.assertEquals("DAY", response.getFullFulfillmentDueIn().getUnit());
    }

    @Test
    void fulfillmentPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyService.getFulfillmentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/fulfillment_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void fulfillmentPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        FulfillmentPolicies response = fulfillmentPolicyService.getFulfillmentPolicies();

        Assertions.assertEquals(1, response.getTotal());
        Assertions.assertEquals("eBay Fulfillments EBAY_US", response.getFulfillmentPolicies().get(0).getName());
//        Assertions.assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getName());
//        Assertions.assertTrue(response.getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
//        Assertions.assertEquals("PAYPAL", response.getFulfillmentPolicies().get(0).getFulfillmentMethods().get(0).getFulfillmentMethodType());
//        Assertions.assertEquals("PAYPAL_EMAIL", response.getFulfillmentPolicies().get(0).getFulfillmentMethods().get(0).getRecipientAccountReference().getReferenceType());
//        Assertions.assertEquals("DAY", response.getFulfillmentPolicies().get(0).getFullFulfillmentDueIn().getUnit());
    }
}
