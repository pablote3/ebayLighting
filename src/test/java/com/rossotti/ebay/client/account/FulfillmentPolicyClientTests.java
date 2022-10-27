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
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.common.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.model.common.CurrencyCodeEnum.USD;
import static com.rossotti.ebay.model.common.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.ebay.model.account.fulfillmentPolicy.OptionTypeEnum.DOMESTIC;
import static com.rossotti.ebay.model.account.fulfillmentPolicy.ShippingCostTypeEnum.CALCULATED;
import static com.rossotti.ebay.model.account.fulfillmentPolicy.ShippingCostTypeEnum.FLAT_RATE;
import static com.rossotti.ebay.model.common.TimeDurationUnitEnum.DAY;

@SpringBootTest
public class FulfillmentPolicyClientTests {
    private static final String FULFILLMENT_POLICY_JSON = "data/account/fulfillmentPolicy.json";
    private static final String FULFILLMENT_POLICIES_JSON = "data/account/fulfillmentPolicies.json";
    private static final String GET = "GET";
    private static final String USPS = "USPS";
    private static final String USPS_FIRST_CLASS = "USPSFirstClass";
    private static final String USPS_PARCEL = "USPSParcel";
    private static final String USPS_PRIORITY = "USPSPriority";
    private static final BigDecimal SEVEN_FIFTEEN = BigDecimal.valueOf(7.15);
    private static final String USPS_PRIORITY_FLAT_RATE_BOX = "USPSPriorityFlatRateBox";
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
    void getFulfillmentPolicy_requestSerialize() throws InterruptedException {
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
    void getFulfillmentPolicy_responseDeserialize() {
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
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getValue(), comparesEqualTo(SEVEN_FIFTEEN));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getCurrency(), is(USD));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getFreeShipping(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForShipping(), is(true));
        assertThat(response.get().getShippingOptions().get(0).getShippingServices().get(0).getBuyerResponsibleForPickup(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceOffered(), is(false));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getValue(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(response.get().getShippingOptions().get(0).getInsuranceFee().getCurrency(), is(USD));
    }

    @Test
    void getFulfillmentPolicies_requestSerialize() throws InterruptedException {
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
    void getFulfillmentPolicies_responseDeserialize() {
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
        assertThat(response.get().getFulfillmentPolicies().get(0).getShippingOptions().get(0).getShippingServices().get(0).getShippingCost().getValue(), comparesEqualTo(SEVEN_FIFTEEN));
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
}
