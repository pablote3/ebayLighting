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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OfferClientTests {
    private static final String OFFER_JSON = "data/inventory/offer.json";
    private static final String OFFERS_JSON = "data/inventory/offers.json";
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
    void offer_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFER_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerClient.getByOfferOfferId("8209815010");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/inventory/v1/offer/8209815010", request.getPath());
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
        Optional<Offer> response = offerClient.getByOfferOfferId("8209815010");

        assertTrue(response.isPresent());
        Assertions.assertEquals("8209815010", response.get().getOfferId());
        Assertions.assertEquals("123", response.get().getSku());
        Assertions.assertEquals("USD", response.get().getPricingSummary().getPrice().getCurrency());
        Assertions.assertEquals("6196947000", response.get().getListingPolicies().getFulfillmentPolicyId());
        Assertions.assertFalse(response.get().getTax().getApplyTax());
        Assertions.assertEquals("ACTIVE", response.get().getListing().getListingStatus());
    }

    @Test
    void offers_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        offerClient.getOffersBySku("123");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/inventory/v1/offer?sku=123&limit=20&offset=0", request.getPath());
    }

    @Test
    void offers_responseDeserialization() {
        String json = TestUtil.readStringFromFile(OFFERS_JSON).orElse(null);
        assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<Offers> response = offerClient.getOffersBySku("123");

        assertTrue(response.isPresent());
        Assertions.assertEquals(1, response.get().getTotal());
        Assertions.assertEquals(1, response.get().getSize());
        Assertions.assertEquals("8209815010", response.get().getOffers().get(0).getOfferId());
        Assertions.assertEquals("123", response.get().getOffers().get(0).getSku());
        Assertions.assertEquals("USD", response.get().getOffers().get(0).getPricingSummary().getPrice().getCurrency());
        Assertions.assertEquals("6196947000", response.get().getOffers().get(0).getListingPolicies().getFulfillmentPolicyId());
        Assertions.assertFalse(response.get().getOffers().get(0).getTax().getApplyTax());
        Assertions.assertEquals("ACTIVE", response.get().getOffers().get(0).getListing().getListingStatus());
    }
}
