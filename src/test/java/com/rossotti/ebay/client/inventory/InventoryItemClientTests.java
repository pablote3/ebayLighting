package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.client.inventory.InventoryItemClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class InventoryItemClientTests {
    private static final String INVENTORY_ITEM_JSON = "data/inventory/inventoryItem.json";
    private static final String INVENTORY_ITEMS_JSON = "data/inventory/inventoryItems.json";
    private static final String pathKey = "inventory_item";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private InventoryItemClient inventoryItemClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, pathKey);
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        inventoryItemClient = new InventoryItemClient(WebClient.create(), properties, appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void inventoryItem_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(INVENTORY_ITEM_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        inventoryItemClient.getByInventoryItemSku("123");
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/inventory/v1/inventory_item/123", request.getPath());
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
        Optional<InventoryItem> response = inventoryItemClient.getByInventoryItemSku("123");

        assertTrue(response.isPresent());
        assertEquals("123", response.get().getSku());
        assertEquals("en_US", response.get().getLocale());
        assertEquals("GoPro Hero4 Helmet Cam", response.get().getProduct().getTitle());
        assertEquals("GoPro", response.get().getProduct().getAspects().getBrand().get(0));
        assertEquals("Helmet/Action", response.get().getProduct().getAspects().getType().get(0));
        assertEquals("High Definition", response.get().getProduct().getAspects().getRecordingDefinition().get(0));
        assertEquals("10x", response.get().getProduct().getAspects().getOpticalZoom().get(0));
        assertEquals("Flash Drive (SSD)", response.get().getProduct().getAspects().getMediaFormat().get(0));
        assertEquals("Removable", response.get().getProduct().getAspects().getStorageType().get(0));
        assertEquals("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg", response.get().getProduct().getImageUrls().get(0));
        assertEquals("INCH", response.get().getPackageWeightAndSize().getDimensions().getUnit());
        assertEquals(Double.valueOf("1.16"), response.get().getPackageWeightAndSize().getDimensions().getLength());
        assertEquals("POUND", response.get().getPackageWeightAndSize().getWeight().getUnit());
        assertEquals(Double.valueOf("2.25"), response.get().getPackageWeightAndSize().getWeight().getValue());
        assertEquals(Integer.valueOf("50"), response.get().getAvailability().getShipToLocationAvailability().getQuantity());
        assertEquals(Integer.valueOf("50"), response.get().getAvailability().getShipToLocationAvailability().getAllocationByFormat().getFixedPrice());
    }

    @Test
    void inventoryItems_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(INVENTORY_ITEMS_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        inventoryItemClient.getInventoryItems();
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/inventory/v1/inventory_item?limit=20&offset=0", request.getPath());
    }

    @Test
    void inventoryItems_responseDeserialization() {
        String json = TestUtil.readStringFromFile(INVENTORY_ITEMS_JSON).orElse(null);
        assertNotNull(json);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<InventoryItems> response = inventoryItemClient.getInventoryItems();

        assertTrue(response.isPresent());
        assertEquals(1, response.get().getTotal());
        assertEquals(1, response.get().getSize());
        assertEquals("123", response.get().getInventoryItems().get(0).getSku());
        assertEquals("en_US", response.get().getInventoryItems().get(0).getLocale());
        assertEquals("GoPro Hero4 Helmet Cam", response.get().getInventoryItems().get(0).getProduct().getTitle());
        assertEquals("GoPro", response.get().getInventoryItems().get(0).getProduct().getAspects().getBrand().get(0));
        assertEquals("Helmet/Action", response.get().getInventoryItems().get(0).getProduct().getAspects().getType().get(0));
        assertEquals("High Definition", response.get().getInventoryItems().get(0).getProduct().getAspects().getRecordingDefinition().get(0));
        assertEquals("10x", response.get().getInventoryItems().get(0).getProduct().getAspects().getOpticalZoom().get(0));
        assertEquals("Flash Drive (SSD)", response.get().getInventoryItems().get(0).getProduct().getAspects().getMediaFormat().get(0));
        assertEquals("Removable", response.get().getInventoryItems().get(0).getProduct().getAspects().getStorageType().get(0));
        assertEquals("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg", response.get().getInventoryItems().get(0).getProduct().getImageUrls().get(0));
        assertNull(response.get().getInventoryItems().get(0).getPackageWeightAndSize());
        assertEquals(Integer.valueOf("50"), response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getQuantity());
        assertNull(response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getAllocationByFormat());
    }
}
