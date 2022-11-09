package com.rossotti.lighting.ebay.client.account;

import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.FulfillmentPolicies;
import com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.FulfillmentPolicy;
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
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.lighting.ebay.model.common.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.lighting.ebay.model.common.CurrencyCodeEnum.USD;
import static com.rossotti.lighting.ebay.model.common.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.OptionTypeEnum.DOMESTIC;
import static com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.ShippingCostTypeEnum.CALCULATED;
import static com.rossotti.lighting.ebay.model.account.fulfillmentPolicy.ShippingCostTypeEnum.FLAT_RATE;
import static com.rossotti.lighting.ebay.model.common.TimeDurationUnitEnum.DAY;

import static com.rossotti.lighting.ebay.util.TestUtil.createServerConfig;
import static com.rossotti.lighting.ebay.util.TestUtil.readStringFromFile;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;

@SpringBootTest
public class FulfillmentPolicyClientTests {
    private static final String FULFILLMENT_POLICY_JSON = "data/ebay/account/fulfillmentPolicy.json";
    private static final String FULFILLMENT_POLICIES_JSON = "data/ebay/account/fulfillmentPolicies.json";
    private static final String USPS = "USPS";
    private static final String USPS_FIRST_CLASS = "USPSFirstClass";
    private static final String USPS_PARCEL = "USPSParcel";
    private static final String USPS_PRIORITY = "USPSPriority";
    private static final String USPS_PRIORITY_FLAT_RATE_BOX = "USPSPriorityFlatRateBox";
    private static MockWebServer mockWebServer;
    private final BasicJsonTester json = new BasicJsonTester(this.getClass());
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FulfillmentPolicyClient fulfillmentPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = createServerConfig(mockWebServer.url("/"));
        fulfillmentPolicyClient = new FulfillmentPolicyClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getFulfillmentPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );
        fulfillmentPolicyClient.getByFulfillmentPolicyId("6196947000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET.name()));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy/6196947000?marketplace_id=EBAY_US"));
    }
    @Test
    void getFulfillmentPolicy_response() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.getByFulfillmentPolicyId("6196932000");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Domestic: FlatRate: USPS"));
        assertThat(response.get().getDescription(), is("Domestic: FlatRate: USPS"));
        assertThat(response.get().getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getHandlingTime().getValue(), is(2));
        assertThat(response.get().getHandlingTime().getUnit(), is(DAY));
        assertThat(response.get().getShippingOptions().get(0).getCostType(), is(FLAT_RATE));
        assertThat(response.get().getShippingOptions().get(0).getOptionType(), is(DOMESTIC));
        assertThat(response.get().getShippingOptions().get(0).getPackageHandlingCost().getValue(), comparesEqualTo(BigDecimal.TEN));
        assertThat(response.get().getShippingOptions().get(0).getPackageHandlingCost().getCurrency(), is(USD));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getSortOrder(), is(1));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode(), is(USPS));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingServiceCode(), is(USPS_PRIORITY_FLAT_RATE_BOX));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getValue(), comparesEqualTo(BigDecimal.valueOf(7.15)));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getCurrency(), is(USD));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getFreeShipping(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForPickup(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceOffered(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getCurrency(), is(USD));
    }
    @Test
    void getFulfillmentPolicies_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null)))
        );
        fulfillmentPolicyClient.getFulfillmentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET.name()));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy?marketplace_id=EBAY_US"));
    }
    @Test
    void getFulfillmentPolicies_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICIES_JSON).orElse(null)))
        );
        Optional<FulfillmentPolicies> response = fulfillmentPolicyClient.getFulfillmentPolicies();

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getFulfillmentPolicies(), hasSize(2));
        assertThat(response.get().getTotal(), is(2));
        assertThat(response.get().getFulfillmentPolicies().get(0).getName(), is("Domestic: FlatRate: USPS"));
        assertThat(response.get().getFulfillmentPolicies().get(0).getDescription(), is("Domestic: FlatRate: USPS"));
        assertThat(response.get().getFulfillmentPolicies().get(0).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getFulfillmentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(0).getHandlingTime().getValue(), is(2));
        assertThat(response.get().getFulfillmentPolicies().get(0).getHandlingTime().getUnit(), is(DAY));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getCostType(), is(FLAT_RATE));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getOptionType(), is(DOMESTIC));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getPackageHandlingCost().getValue(), comparesEqualTo(BigDecimal.TEN));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getPackageHandlingCost().getCurrency(), is(USD));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getSortOrder(), is(1));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode(), is(USPS));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingServiceCode(), is(USPS_PRIORITY_FLAT_RATE_BOX));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getValue(), comparesEqualTo(BigDecimal.valueOf(7.15)));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getCurrency(), is(USD));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getFreeShipping(), is(false));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForPickup(), is(false));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceOffered(), is(false));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceFee().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getInsuranceFee().getCurrency(), is(USD));
        assertThat(response.get().getFulfillmentPolicies().get(1).getName(), is("Domestic: Calculated: USPS"));
        assertThat(response.get().getFulfillmentPolicies().get(1).getDescription(), is("Domestic: Calculated: USPS"));
        assertThat(response.get().getFulfillmentPolicies().get(1).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getFulfillmentPolicies().get(1).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getFulfillmentPolicies().get(1).getHandlingTime().getValue(), is(2));
        assertThat(response.get().getFulfillmentPolicies().get(1).getHandlingTime().getUnit(), is(DAY));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getCostType(), is(CALCULATED));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getOptionType(), is(DOMESTIC));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getPackageHandlingCost().getValue(), comparesEqualTo(BigDecimal.TEN));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getPackageHandlingCost().getCurrency(), is(USD));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().size(), is(3));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(0).getSortOrder(), is(1));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(0).getShippingCarrierCode(), is(USPS));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(0).getShippingServiceCode(), is(USPS_PRIORITY));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForPickup(), is(false));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(1).getSortOrder(), is(2));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(1).getShippingCarrierCode(), is(USPS));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(1).getShippingServiceCode(), is(USPS_FIRST_CLASS));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(1).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(1).getBuyerResponsibleForPickup(), is(false));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(2).getSortOrder(), is(3));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(2).getShippingCarrierCode(), is(USPS));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(2).getShippingServiceCode(), is(USPS_PARCEL));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(2).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getFulfillmentPolicies().get(1).getShippingOptions().get(0).getShippingServices().get(2).getBuyerResponsibleForPickup(), is(false));
    }
    @Test
    void createFulfillmentPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );
        fulfillmentPolicyClient.create(new FulfillmentPolicy());

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(POST.name()));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy?marketplace_id=EBAY_US"));
    }
    @Test
    void createFulfillmentPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );

        FulfillmentPolicy fulfillmentPolicy = new FulfillmentPolicy();
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.create(fulfillmentPolicy);

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Domestic: FlatRate: USPS"));
    }
    @Test
    void updateFulfillmentPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );
        fulfillmentPolicyClient.update(new FulfillmentPolicy(), "123456");

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(PUT.name()));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy/123456"));
    }
    @Test
    void updateFulfillmentPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(FULFILLMENT_POLICY_JSON).orElse(null)))
        );

        FulfillmentPolicy fulfillmentPolicy = new FulfillmentPolicy();
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.update(fulfillmentPolicy, "123456");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("Domestic: FlatRate: USPS"));
    }
    @Test
    void deleteFulfillmentPolicy_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        fulfillmentPolicyClient.delete("6196932000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(DELETE.name()));
        assertThat(request.getPath(), is("/sell/account/v1/fulfillment_policy/6196932000"));
    }
    @Test
    void deleteFulfillmentPolicy_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(204)
        );
        Optional<FulfillmentPolicy> response = fulfillmentPolicyClient.delete("6196932000");

        assertThat(response.isPresent(), is(false));
    }
}
