package com.rossotti.ebay.service.inventory;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItems;
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
public class InventoryItemServiceTests {
    private static final String INVENTORY_ITEM_JSON = "data/inventory/inventoryItem.json";
    private static final String INVENTORY_ITEMS_JSON = "data/inventory/inventoryItems.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private InventoryItemService inventoryItemService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, "payment_policy");
        inventoryItemService = new InventoryItemService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void inventoryItem_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(INVENTORY_ITEM_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        inventoryItemService.getInventoryItem("123");
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/inventory/v1/inventory_item/123", request.getPath());
    }

    @Test
    void inventoryItem_responseDeserialization() {
        String json = TestUtil.readStringFromFile(INVENTORY_ITEM_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        InventoryItem response = inventoryItemService.getInventoryItem("123");

        Assertions.assertEquals("123", response.getSku());
        Assertions.assertEquals("en_US", response.getLocale());
        Assertions.assertEquals("GoPro Hero4 Helmet Cam", response.getProduct().getTitle());
        Assertions.assertEquals("GoPro", response.getProduct().getAspects().getBrand().get(0));
        Assertions.assertEquals("Helmet/Action", response.getProduct().getAspects().getType().get(0));
        Assertions.assertEquals("High Definition", response.getProduct().getAspects().getRecordingDefinition().get(0));
        Assertions.assertEquals("10x", response.getProduct().getAspects().getOpticalZoom().get(0));
        Assertions.assertEquals("Flash Drive (SSD)", response.getProduct().getAspects().getMediaFormat().get(0));
        Assertions.assertEquals("Removable", response.getProduct().getAspects().getStorageType().get(0));
        Assertions.assertEquals("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg", response.getProduct().getImageUrls().get(0));
        Assertions.assertEquals("INCH", response.getPackageWeightAndSize().getDimensions().getUnit());
        Assertions.assertEquals(Double.valueOf("1.16"), response.getPackageWeightAndSize().getDimensions().getLength());
        Assertions.assertEquals("POUND", response.getPackageWeightAndSize().getWeight().getUnit());
        Assertions.assertEquals(Double.valueOf("2.25"), response.getPackageWeightAndSize().getWeight().getValue());
        Assertions.assertEquals(Integer.valueOf("50"), response.getAvailability().getShipToLocationAvailability().getQuantity());
        Assertions.assertEquals(Integer.valueOf("50"), response.getAvailability().getShipToLocationAvailability().getAllocationByFormat().getFixedPrice());
    }

    @Test
    void inventoryItems_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(INVENTORY_ITEMS_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        inventoryItemService.getInventoryItems();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/inventory/v1/inventory_item?limit=20&offset=0", request.getPath());
    }

    @Test
    void inventoryItems_responseDeserialization() {
        String json = TestUtil.readStringFromFile(INVENTORY_ITEMS_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        InventoryItems response = inventoryItemService.getInventoryItems();

        Assertions.assertEquals(1, response.getTotal());
        Assertions.assertEquals(1, response.getSize());
        Assertions.assertEquals("123", response.getInventoryItems().get(0).getSku());
        Assertions.assertEquals("en_US", response.getInventoryItems().get(0).getLocale());
        Assertions.assertEquals("GoPro Hero4 Helmet Cam", response.getInventoryItems().get(0).getProduct().getTitle());
        Assertions.assertEquals("GoPro", response.getInventoryItems().get(0).getProduct().getAspects().getBrand().get(0));
        Assertions.assertEquals("Helmet/Action", response.getInventoryItems().get(0).getProduct().getAspects().getType().get(0));
        Assertions.assertEquals("High Definition", response.getInventoryItems().get(0).getProduct().getAspects().getRecordingDefinition().get(0));
        Assertions.assertEquals("10x", response.getInventoryItems().get(0).getProduct().getAspects().getOpticalZoom().get(0));
        Assertions.assertEquals("Flash Drive (SSD)", response.getInventoryItems().get(0).getProduct().getAspects().getMediaFormat().get(0));
        Assertions.assertEquals("Removable", response.getInventoryItems().get(0).getProduct().getAspects().getStorageType().get(0));
        Assertions.assertEquals("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg", response.getInventoryItems().get(0).getProduct().getImageUrls().get(0));
        Assertions.assertNull(response.getInventoryItems().get(0).getPackageWeightAndSize());
        Assertions.assertEquals(Integer.valueOf("50"), response.getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getQuantity());
        Assertions.assertNull(response.getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getAllocationByFormat());
    }
}
