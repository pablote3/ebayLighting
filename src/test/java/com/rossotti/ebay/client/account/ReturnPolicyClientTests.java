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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.helper.enumeration.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.helper.enumeration.RefundMethodEnum.MONEY_BACK;
import static com.rossotti.ebay.helper.enumeration.ReturnMethodEnum.REPLACEMENT;
import static com.rossotti.ebay.helper.enumeration.ReturnShippingCostPayerEnum.SELLER;
import static com.rossotti.ebay.helper.enumeration.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.ebay.helper.enumeration.TimeDurationUnitEnum.DAY;

@SpringBootTest
public class ReturnPolicyClientTests {
    private static final String RETURN_POLICY_JSON = "data/account/returnPolicy.json";
    private static final String RETURN_POLICIES_JSON = "data/account/returnPolicies.json";
    private static final String GET = "GET";
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
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyClient.getByReturnPolicyId("6196944000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy/6196944000?marketplace_id=EBAY_US"));
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

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Return Accepted"));
        assertThat(response.get().getDescription(), is("Return Accepted: Seller Pays Shipping"));
        assertThat(response.get().getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertTrue(response.get().getReturnsAccepted());
        assertThat(response.get().getReturnPeriod().getUnit(), is(DAY));
        assertThat(response.get().getRefundMethod(), is(MONEY_BACK));
        assertThat(response.get().getReturnMethod(), is(REPLACEMENT));
        assertThat(response.get().getReturnShippingCostPayer(), is(SELLER));
    }

    @Test
    void returnPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(RETURN_POLICIES_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        returnPolicyClient.getReturnPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy?marketplace_id=EBAY_US"));
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

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getReturnPolicies(), hasSize(2));
        assertThat(response.get().getTotal(), is(2));
        assertThat(response.get().getReturnPolicies().get(0).getName(), is("No Return Accepted"));
        assertThat(response.get().getReturnPolicies().get(0).getDescription(), is("No Return Accepted"));
        assertThat(response.get().getReturnPolicies().get(0).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getReturnPolicies().get(0).getCategoryTypes().get(0).getDefaultValue(), is(false));
        assertThat(response.get().getReturnPolicies().get(0).getReturnPeriod(), is(nullValue()));
        assertThat(response.get().getReturnPolicies().get(0).getReturnsAccepted(), is(false));
        assertThat(response.get().getReturnPolicies().get(0).getRefundMethod(), is(nullValue()));
        assertThat(response.get().getReturnPolicies().get(0).getReturnMethod(), is(nullValue()));
        assertThat(response.get().getReturnPolicies().get(0).getReturnShippingCostPayer(), is(nullValue()));
        assertThat(response.get().getReturnPolicies().get(1).getName(), is("Return Accepted"));
        assertThat(response.get().getReturnPolicies().get(1).getDescription(), is("Return Accepted: Seller Pays Shipping"));
        assertThat(response.get().getReturnPolicies().get(1).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getReturnPolicies().get(1).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getReturnPolicies().get(1).getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getReturnPolicies().get(1).getReturnPeriod().getUnit(), is(DAY));
        assertThat(response.get().getReturnPolicies().get(1).getReturnsAccepted(), is(true));
        assertThat(response.get().getReturnPolicies().get(1).getRefundMethod(), is(MONEY_BACK));
        assertThat(response.get().getReturnPolicies().get(1).getReturnMethod(), is(REPLACEMENT));
        assertThat(response.get().getReturnPolicies().get(1).getReturnShippingCostPayer(), is(SELLER));
    }
}
