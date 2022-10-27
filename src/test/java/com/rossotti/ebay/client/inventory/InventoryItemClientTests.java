package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItems;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.common.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.model.inventory.inventoryItem.ConditionEnum.NEW;
import static com.rossotti.ebay.model.inventory.inventoryItem.LocaleEnum.en_US;

@SpringBootTest
public class InventoryItemClientTests {
    private static final String INVENTORY_ITEM_JSON = "data/inventory/inventoryItem.json";
    private static final String INVENTORY_ITEMS_JSON = "data/inventory/inventoryItems.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private InventoryItemClient inventoryItemClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        inventoryItemClient = new InventoryItemClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void inventoryItem_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(INVENTORY_ITEM_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        inventoryItemClient.getByInventoryItemSku("123");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/inventory/v1/inventory_item/123"));
    }

    @Test
    void inventoryItem_responseDeserialization() {
        String json = TestUtil.readStringFromFile(INVENTORY_ITEM_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<InventoryItem> response = inventoryItemClient.getByInventoryItemSku("123");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getSku(), is("123"));
        assertThat(response.get().getLocale(), is(en_US));
        assertThat(response.get().getCondition(), is(NEW));
        assertThat(response.get().getProduct().getTitle(), is("GoPro Hero4 Helmet Cam"));
        assertThat(response.get().getProduct().getDescription(), is("New GoPro Hero4 Helmet Cam. Unopened box."));
        assertThat(response.get().getProduct().getAspects().getBrand().get(0), is("GoPro"));
        assertThat(response.get().getProduct().getAspects().getType().get(0), is("Helmet/Action"));
        assertThat(response.get().getProduct().getAspects().getRecordingDefinition().get(0), is("High Definition"));
        assertThat(response.get().getProduct().getAspects().getOpticalZoom().get(0), is("10x"));
        assertThat(response.get().getProduct().getAspects().getMediaFormat().get(0), is("Flash Drive (SSD)"));
        assertThat(response.get().getProduct().getAspects().getStorageType().get(0), is("Removable"));
        assertThat(response.get().getProduct().getImageUrls().get(0), is("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg"));
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
        assertThat(str, is(notNullValue()));
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
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<InventoryItems> response = inventoryItemClient.getInventoryItems();

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getInventoryItems(), hasSize(1));
        assertThat(response.get().getTotal(), is(1));
        assertThat(response.get().getInventoryItems().get(0).getSku(), is("123"));
        assertThat(response.get().getInventoryItems().get(0).getLocale(), is(en_US));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getTitle(), is("GoPro Hero4 Helmet Cam"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getDescription(), is("New GoPro Hero4 Helmet Cam. Unopened box."));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getBrand().get(0), is("GoPro"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getType().get(0), is("Helmet/Action"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getRecordingDefinition().get(0), is("High Definition"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getOpticalZoom().get(0), is("10x"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getMediaFormat().get(0), is("Flash Drive (SSD)"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getStorageType().get(0), is("Removable"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getImageUrls().get(0), is("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg"));
        assertNull(response.get().getInventoryItems().get(0).getPackageWeightAndSize());
        assertEquals(Integer.valueOf("50"), response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getQuantity());
        assertNull(response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getAllocationByFormat());
    }
}
