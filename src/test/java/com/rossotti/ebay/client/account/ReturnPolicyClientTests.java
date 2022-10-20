package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.account.ReturnPolicyClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
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

@SpringBootTest
public class ReturnPolicyClientTests {
    private static final String RETURN_POLICY_JSON = "data/account/returnPolicy.json";
    private static final String RETURN_POLICIES_JSON = "data/account/returnPolicies.json";
    private static final String pathKey = "return_policy";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ReturnPolicyClient returnPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, pathKey);
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        returnPolicyClient = new ReturnPolicyClient(WebClient.create(), properties, appConfig, serverConfig);
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
        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getCategoryTypes().get(0).getName());
        assertTrue(response.get().getCategoryTypes().get(0).getDefaultValue());
        assertEquals("eBay Returns EBAY_US", response.get().getName());
        assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getCategoryTypes().get(0).getName());
        assertTrue(response.get().getCategoryTypes().get(0).getDefaultValue());
        assertEquals("DAY", response.get().getReturnPeriod().getUnit());
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
        Assertions.assertEquals(1, response.get().getTotal());
        Assertions.assertEquals("eBay Returns EBAY_US", response.get().getReturnPolicies().get(0).getName());
        Assertions.assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getName());
        Assertions.assertTrue(response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
        Assertions.assertEquals("DAY", response.get().getReturnPolicies().get(0).getReturnPeriod().getUnit());
    }
}