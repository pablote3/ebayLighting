package com.rossotti.ebay.client.account;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicies;
import com.rossotti.ebay.model.account.returnPolicy.ReturnPolicy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.common.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.model.account.returnPolicy.RefundMethodEnum.MONEY_BACK;
import static com.rossotti.ebay.model.account.returnPolicy.ReturnMethodEnum.REPLACEMENT;
import static com.rossotti.ebay.model.account.returnPolicy.ReturnShippingCostPayerEnum.SELLER;
import static com.rossotti.ebay.model.common.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.ebay.model.common.TimeDurationUnitEnum.DAY;
import static com.rossotti.ebay.util.TestUtil.createServerConfig;
import static com.rossotti.ebay.util.TestUtil.readStringFromFile;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;

@SpringBootTest
public class ReturnPolicyClientTests {
    private static final String RETURN_POLICY_JSON = "data/account/returnPolicy.json";
    private static final String RETURN_POLICIES_JSON = "data/account/returnPolicies.json";
    private static MockWebServer mockWebServer;
    private final BasicJsonTester json = new BasicJsonTester(this.getClass());
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ReturnPolicyClient returnPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = createServerConfig(mockWebServer.url("/"));
        returnPolicyClient = new ReturnPolicyClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getReturnPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );
        returnPolicyClient.getByReturnPolicyId("6196944000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET.name()));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy/6196944000?marketplace_id=EBAY_US"));
    }
    @Test
    void getReturnPolicy_response() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );
        Optional<ReturnPolicy> response = returnPolicyClient.getByReturnPolicyId("6196944000");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Return Accepted"));
        assertThat(response.get().getDescription(), is("Return Accepted: Seller Pays Shipping"));
        assertThat(response.get().getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getReturnsAccepted(), is(true));
        assertThat(response.get().getReturnPeriod().getUnit(), is(DAY));
        assertThat(response.get().getRefundMethod(), is(MONEY_BACK));
        assertThat(response.get().getReturnMethod(), is(REPLACEMENT));
        assertThat(response.get().getReturnShippingCostPayer(), is(SELLER));
    }
    @Test
    void getReturnPolicies_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICIES_JSON).orElse(null)))
        );
        returnPolicyClient.getReturnPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET.name()));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy?marketplace_id=EBAY_US"));
    }
    @Test
    void getReturnPolicies_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICIES_JSON).orElse(null)))
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
    @Test
    void createReturnPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );
        returnPolicyClient.create(new ReturnPolicy());

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(POST.name()));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy?marketplace_id=EBAY_US"));
    }
    @Test
    void createReturnPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );

        ReturnPolicy returnPolicy = new ReturnPolicy();
        Optional<ReturnPolicy> response = returnPolicyClient.create(returnPolicy);

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Return Accepted"));
    }
    @Test
    void updateReturnPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );
        returnPolicyClient.update(new ReturnPolicy(), "123456");

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(PUT.name()));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy/123456"));
    }
    @Test
    void updateReturnPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(RETURN_POLICY_JSON).orElse(null)))
        );

        ReturnPolicy returnPolicy = new ReturnPolicy();
        Optional<ReturnPolicy> response = returnPolicyClient.update(returnPolicy, "123456");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Return Accepted"));
    }
    @Test
    void deleteReturnPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        returnPolicyClient.delete("6196932000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(DELETE.name()));
        assertThat(request.getPath(), is("/sell/account/v1/return_policy/6196932000"));
    }
    @Test
    void deleteReturnPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(204)
        );
        Optional<ReturnPolicy> response = returnPolicyClient.delete("6196932000");

        assertThat(response.isPresent(), is(false));
    }
}
