package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.account.FulfillmentPolicyClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
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
public class FulfillmentPolicyClientTests {
    private static final String FULFILLMENT_POLICY_JSON = "data/account/fulfillmentPolicy.json";
    private static final String FULFILLMENT_POLICIES_JSON = "data/account/fulfillmentPolicies.json";
    private static final String pathKey = "fulfillment_policy";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FulfillmentPolicyClient fulfillmentPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, pathKey);
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        fulfillmentPolicyClient = new FulfillmentPolicyClient(WebClient.create(), properties, appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fulfillmentPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyClient.getByFulfillmentPolicyId("6196947000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/fulfillment_policy/6196947000?marketplace_id=EBAY_US", request.getPath());
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
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.getByFulfillmentPolicyId("6196932000");

        assertTrue(response.isPresent());
        assertEquals("eBay Fulfillments EBAY_US", response.get().getName());
        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getCategoryTypes().get(0).getName());
        assertTrue(response.get().getCategoryTypes().get(0).getDefaultValue());
        assertEquals("DAY", response.get().getHandlingTime().getUnit());
        assertEquals("CALCULATED", response.get().getShippingOptions().get(0).getCostType());
        assertEquals("USPS", response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode());
        assertEquals("USD", response.get().getShippingOptions().get(0).getInsuranceFee().getCurrency());
    }

    @Test
    void fulfillmentPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyClient.getFulfillmentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/fulfillment_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void fulfillmentPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<FulfillmentPolicies> response = fulfillmentPolicyClient.getFulfillmentPolicies();

        assertTrue(response.isPresent());
        assertEquals(1, response.get().getTotal());
        assertEquals("eBay Fulfillments EBAY_US", response.get().getFulfillmentPolicies().get(0).getName());
        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getName());
        assertTrue(response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
        assertEquals("DAY", response.get().getFulfillmentPolicies().get(0).getHandlingTime().getUnit());
        assertEquals("CALCULATED", response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getCostType());
        assertEquals("USPS", response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode());
        assertEquals("USD", response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceFee().getCurrency());
    }
}