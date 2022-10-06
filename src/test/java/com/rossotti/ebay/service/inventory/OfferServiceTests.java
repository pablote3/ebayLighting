package com.rossotti.ebay.service.inventory;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
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
public class OfferServiceTests {
    private static final String OFFER_JSON = "data/inventory/offer.json";
    private static final String OFFERS_JSON = "data/inventory/offers.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private OfferService offerService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"));
        offerService = new OfferService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void offer_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFER_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerService.getOffer("8209815010");
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/inventory/v1/offer/8209815010", request.getPath());
    }

    @Test
    void offer_responseDeserialization() {
        String json = TestUtil.readStringFromFile(OFFER_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Offer response = offerService.getOffer("8209815010");

        Assertions.assertEquals("8209815010", response.getOfferId());
        Assertions.assertEquals("123", response.getSku());
        Assertions.assertEquals("USD", response.getPricingSummary().getPrice().getCurrency());
        Assertions.assertEquals("6196947000", response.getListingPolicies().getFulfillmentPolicyId());
        Assertions.assertFalse(response.getTax().getApplyTax());
        Assertions.assertEquals("ACTIVE", response.getListing().getListingStatus());
    }

    @Test
    void offers_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerService.getOffersBySku("123");
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/inventory/v1/offer?sku=123&limit=20&offset=0", request.getPath());
    }

    @Test
    void offers_responseDeserialization() {
        String json = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Offers response = offerService.getOffersBySku("123");

        Assertions.assertEquals(1, response.getTotal());
        Assertions.assertEquals(1, response.getSize());
        Assertions.assertEquals("8209815010", response.getOffers().get(0).getOfferId());
        Assertions.assertEquals("123", response.getOffers().get(0).getSku());
        Assertions.assertEquals("USD", response.getOffers().get(0).getPricingSummary().getPrice().getCurrency());
        Assertions.assertEquals("6196947000", response.getOffers().get(0).getListingPolicies().getFulfillmentPolicyId());
        Assertions.assertFalse(response.getOffers().get(0).getTax().getApplyTax());
        Assertions.assertEquals("ACTIVE", response.getOffers().get(0).getListing().getListingStatus());
    }
}
