package com.rossotti.ebay.client.account;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicy;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ReturnPolicyClientTests {
    private static final String RETURN_POLICY_JSON = "data/account/returnPolicy.json";
    private static final String RETURN_POLICIES_JSON = "data/account/returnPolicies.json";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ReturnPolicyClient returnPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        returnPolicyClient = new ReturnPolicyClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void returnPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(RETURN_POLICY_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyClient.getByReturnPolicyId("6196944000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/return_policy/6196944000?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void returnPolicy_responseDeserialization() {
        String json = TestUtil.readStringFromFile(RETURN_POLICY_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<ReturnPolicy> response = returnPolicyClient.getByReturnPolicyId("6196944000");

        assertTrue(response.isPresent());
        assertEquals("eBay Returns EBAY_US", response.get().getName());
        assertEquals("All Excluding Motors Vehicles", response.get().getCategoryTypes().get(0).getName().getCode());
        assertTrue(response.get().getCategoryTypes().get(0).getDefaultValue());
        assertTrue(response.get().getReturnsAccepted());
        assertEquals("Day", response.get().getReturnPeriod().getUnit().getCode());
        assertEquals("Money Back", response.get().getRefundMethod().getCode());
        assertEquals("Replacement", response.get().getReturnMethod().getCode());
        assertEquals("Buyer", response.get().getReturnShippingCostPayer().getCode());
    }

    @Test
    void returnPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(RETURN_POLICIES_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyClient.getReturnPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/return_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void returnPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(RETURN_POLICIES_JSON).orElse(null);
        assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<ReturnPolicies> response = returnPolicyClient.getReturnPolicies();

        assertTrue(response.isPresent());
        assertEquals(2, response.get().getTotal());
        assertEquals("eBay Returns EXCHANGE", response.get().getReturnPolicies().get(0).getName());
        assertEquals("All Excluding Motors Vehicles", response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getName().getCode());
        assertTrue(response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
        assertEquals("Day", response.get().getReturnPolicies().get(0).getReturnPeriod().getUnit().getCode());
        assertTrue(response.get().getReturnPolicies().get(0).getReturnsAccepted());
        assertEquals("Money Back", response.get().getReturnPolicies().get(0).getRefundMethod().getCode());
        assertEquals("Exchange", response.get().getReturnPolicies().get(0).getReturnMethod().getCode());
        assertEquals("Buyer", response.get().getReturnPolicies().get(0).getReturnShippingCostPayer().getCode());
        assertEquals("eBay Returns REPLACEMENT", response.get().getReturnPolicies().get(1).getName());
        assertEquals("All Excluding Motors Vehicles", response.get().getReturnPolicies().get(1).getCategoryTypes().get(0).getName().getCode());
        assertFalse(response.get().getReturnPolicies().get(1).getCategoryTypes().get(0).getDefaultValue());
        assertEquals("Day", response.get().getReturnPolicies().get(1).getReturnPeriod().getUnit().getCode());
        assertTrue(response.get().getReturnPolicies().get(1).getReturnsAccepted());
        assertEquals("Money Back", response.get().getReturnPolicies().get(1).getRefundMethod().getCode());
        assertEquals("Replacement", response.get().getReturnPolicies().get(1).getReturnMethod().getCode());
        assertEquals("Seller", response.get().getReturnPolicies().get(1).getReturnShippingCostPayer().getCode());
    }
}
