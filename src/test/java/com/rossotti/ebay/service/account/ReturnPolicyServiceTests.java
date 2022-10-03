package com.rossotti.ebay.service.account;

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

@SpringBootTest
public class ReturnPolicyServiceTests {
    private static final String RETURN_POLICY_JSON = "data/returnPolicy.json";
    private static final String RETURN_POLICIES_JSON = "data/returnPolicies.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private ReturnPolicyService returnPolicyService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"));
        returnPolicyService = new ReturnPolicyService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void returnPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(RETURN_POLICY_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyService.getReturnPolicy("6196944000");
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/return_policy/6196944000?marketplace_id=EBAY_US", request.getPath());
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
        ReturnPolicy response = returnPolicyService.getReturnPolicy("6196944000");

        Assertions.assertEquals("eBay Returns EBAY_US", response.getName());
        Assertions.assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.getCategoryTypes().get(0).getName());
        Assertions.assertTrue(response.getCategoryTypes().get(0).getDefaultValue());
        Assertions.assertEquals("DAY", response.getReturnPeriod().getUnit());
    }

    @Test
    void returnPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(RETURN_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyService.getReturnPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/return_policy?marketplace_id=EBAY_US", request.getPath());
    }

    @Test
    void returnPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(RETURN_POLICIES_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        ReturnPolicies response = returnPolicyService.getReturnPolicies();

        Assertions.assertEquals(1, response.getTotal());
        Assertions.assertEquals("eBay Returns EBAY_US", response.getReturnPolicies().get(0).getName());
        Assertions.assertEquals("ALL_EXCLUDING_MOTORS_VEHICLES", response.getReturnPolicies().get(0).getCategoryTypes().get(0).getName());
        Assertions.assertTrue(response.getReturnPolicies().get(0).getCategoryTypes().get(0).getDefaultValue());
        Assertions.assertEquals("DAY", response.getReturnPolicies().get(0).getReturnPeriod().getUnit());
    }
}
