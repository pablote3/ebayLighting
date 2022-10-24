package com.rossotti.ebay.client.account;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
import com.rossotti.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.helper.enumeration.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.helper.enumeration.CurrencyCodeEnum.USD;
import static com.rossotti.ebay.helper.enumeration.OptionTypeEnum.DOMESTIC;
import static com.rossotti.ebay.helper.enumeration.ShippingCostTypeEnum.CALCULATED;
import static com.rossotti.ebay.helper.enumeration.TimeDurationUnitEnum.DAY;

@SpringBootTest
public class FulfillmentPolicyClientTests {
    private static final String FULFILLMENT_POLICY_JSON = "data/account/fulfillmentPolicy.json";
    private static final String FULFILLMENT_POLICIES_JSON = "data/account/fulfillmentPolicies.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FulfillmentPolicyClient fulfillmentPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        fulfillmentPolicyClient = new FulfillmentPolicyClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fulfillmentPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyClient.getByFulfillmentPolicyId("6196947000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy/6196947000?marketplace_id=EBAY_US"));
    }

    @Test
    void fulfillmentPolicy_responseDeserialization() {
        String json = TestUtil.readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.getByFulfillmentPolicyId("6196932000");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("eBay Fulfillments EBAY_US"));
        assertThat(response.get().getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getHandlingTime().getUnit(), is(DAY));
        assertThat(response.get().getShippingOptions().get(0).getCostType(), is(CALCULATED));
        assertThat(response.get().getShippingOptions().get(0).getPackageHandlingCost().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getShippingOptions().get(0).getPackageHandlingCost().getCurrency(), is(USD));
        assertThat(response.get().getShippingOptions().get(0).getOptionType(), is(DOMESTIC));
        assertEquals("USPS", response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode());
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getCurrency(), is(USD));
    }

    @Test
    void fulfillmentPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        fulfillmentPolicyClient.getFulfillmentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy?marketplace_id=EBAY_US"));
    }

    @Test
    void fulfillmentPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<FulfillmentPolicies> response = fulfillmentPolicyClient.getFulfillmentPolicies();

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getFulfillmentPolicies(), hasSize(1));
        assertThat(response.get().getTotal(), is(1));
        assertThat(response.get().getFulfillmentPolicies().get(0).getName(), is("eBay Fulfillments EBAY_US"));
        assertThat(response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(0).getHandlingTime().getUnit(), is(DAY));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getCostType(), is(CALCULATED));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getPackageHandlingCost().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getPackageHandlingCost().getCurrency(), is(USD));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getOptionType(), is(DOMESTIC));
        assertEquals("USPS", response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode());
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceFee().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceFee().getCurrency(), is(USD));
    }
}
