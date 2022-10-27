package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.common.CurrencyCodeEnum.USD;
import static com.rossotti.ebay.model.inventory.offer.FormatTypeEnum.FIXED_PRICE;
import static com.rossotti.ebay.model.inventory.offer.ListingDurationEnum.GTC;
import static com.rossotti.ebay.model.inventory.offer.ListingStatusEnum.ACTIVE;
import static com.rossotti.ebay.model.common.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.ebay.model.inventory.offer.OfferStatusEnum.PUBLISHED;

@SpringBootTest
public class OfferClientTests {
    private static final String OFFER_JSON = "data/inventory/offer.json";
    private static final String OFFERS_JSON = "data/inventory/offers.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private OfferClient offerClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        offerClient = new OfferClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getOffer_requestSerialize() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFER_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerClient.getByOfferByOfferId("8209815010");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/inventory/v1/offer/8209815010"));
    }

    @Test
    void getOffer_responseDeserialize() {
        String json = TestUtil.readStringFromFile(OFFER_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<Offer> response = offerClient.getByOfferByOfferId("8209815010");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getOfferId(), is("8209815010"));
        assertThat(response.get().getSku(), is("123"));
        assertThat(response.get().getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getFormat(), is(FIXED_PRICE));
        assertThat(response.get().getStatus(), is(PUBLISHED));
        assertThat(response.get().getListingDuration(), is(GTC));
        assertThat(response.get().getIncludeCatalogProductDetails(), is(true));
        assertThat(response.get().getHideBuyerDetails(), is(false));
        assertThat(response.get().getListingDescription(), is("GoPro Hero4 Helmet Cam - order description"));
        assertThat(response.get().getAvailableQuantity(), is(10));
        assertThat(response.get().getPricingSummary().getPrice().getValue(), is("272.17"));
        assertThat(response.get().getPricingSummary().getPrice().getCurrency(), is(USD));
        assertThat(response.get().getListingPolicies().getPaymentPolicyId(), is("6196932000"));
        assertThat(response.get().getListingPolicies().getReturnPolicyId(), is("6196944000"));
        assertThat(response.get().getListingPolicies().getFulfillmentPolicyId(), is("6196947000"));
        assertThat(response.get().getTax().getApplyTax(), is(false));
        assertThat(response.get().getListing().getListingId(), is("110551471149"));
        assertThat(response.get().getListing().getListingStatus(), is(ACTIVE));
        assertThat(response.get().getListing().getSoldQuantity(), is(0));
    }

    @Test
    void getOffers_requestSerialize() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerClient.getOffersBySku("123");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/inventory/v1/offer?sku=123&limit=20&offset=0"));
    }

    @Test
    void getOffers_responseDeserialize() {
        String json = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<Offers> response = offerClient.getOffersBySku("123");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getOffers(), hasSize(1));
        assertThat(response.get().getTotal(), is(1));
        assertThat(response.get().getOffers().get(0).getOfferId(), is("8209815010"));
        assertThat(response.get().getOffers().get(0).getSku(), is("123"));
        assertThat(response.get().getOffers().get(0).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getOffers().get(0).getFormat(), is(FIXED_PRICE));
        assertThat(response.get().getOffers().get(0).getStatus(), is(PUBLISHED));
        assertThat(response.get().getOffers().get(0).getListingDuration(), is(GTC));
        assertThat(response.get().getOffers().get(0).getIncludeCatalogProductDetails(), is(true));
        assertThat(response.get().getOffers().get(0).getHideBuyerDetails(), is(false));
        assertThat(response.get().getOffers().get(0).getListingDescription(), is("GoPro Hero4 Helmet Cam - order description"));
        assertThat(response.get().getOffers().get(0).getAvailableQuantity(), is(10));
        assertThat(response.get().getOffers().get(0).getPricingSummary().getPrice().getValue(), is("272.17"));
        assertThat(response.get().getOffers().get(0).getPricingSummary().getPrice().getCurrency(), is(USD));
        assertThat(response.get().getOffers().get(0).getListingPolicies().getPaymentPolicyId(), is("6196932000"));
        assertThat(response.get().getOffers().get(0).getListingPolicies().getReturnPolicyId(), is("6196944000"));
        assertThat(response.get().getOffers().get(0).getListingPolicies().getFulfillmentPolicyId(), is("6196947000"));
        assertThat(response.get().getOffers().get(0).getTax().getApplyTax(), is(false));
        assertThat(response.get().getOffers().get(0).getListing().getListingId(), is("110551471149"));
        assertThat(response.get().getOffers().get(0).getListing().getSoldQuantity(), is(0));
        assertThat(response.get().getOffers().get(0).getListing().getListingStatus(), is(ACTIVE));
    }
}
