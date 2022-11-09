package com.rossotti.lighting.ebay.client.inventory;

import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.lighting.ebay.model.inventory.inventoryItem.InventoryItems;
import com.rossotti.lighting.ebay.util.TestUtil;
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

import static com.rossotti.lighting.ebay.util.TestUtil.readStringFromFile;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.lighting.ebay.model.inventory.inventoryItem.ConditionEnum.NEW;
import static com.rossotti.lighting.ebay.model.inventory.inventoryItem.LengthUnitOfMeasureEnum.INCH;
import static com.rossotti.lighting.ebay.model.inventory.inventoryItem.LocaleEnum.en_US;
import static com.rossotti.lighting.ebay.model.inventory.inventoryItem.WeightUnitOfMeasureEnum.POUND;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;

@SpringBootTest
public class InventoryItemClientTests {
    private static final String INVENTORY_ITEM_JSON = "data/ebay/inventory/inventoryItem.json";
    private static final String INVENTORY_ITEMS_JSON = "data/ebay/inventory/inventoryItems.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    private final BasicJsonTester json = new BasicJsonTester(this.getClass());

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
    void getInventoryItem_requestSerialize() throws InterruptedException {
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
    void getInventoryItem_responseDeserialize() {
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
        assertThat(response.get().getProduct().getBrand(), is("GoPro"));
        assertThat(response.get().getProduct().getMpn(), is("CHDHX-401"));
        assertThat(response.get().getProduct().getTitle(), is("GoPro Hero4 Helmet Cam"));
        assertThat(response.get().getProduct().getDescription(), is("New GoPro Hero4 Helmet Cam. Unopened box."));
        assertThat(response.get().getProduct().getAspects().getBrand().get(0), is("GoPro"));
        assertThat(response.get().getProduct().getAspects().getType().get(0), is("Helmet/Action"));
        assertThat(response.get().getProduct().getAspects().getRecordingDefinition().get(0), is("High Definition"));
        assertThat(response.get().getProduct().getAspects().getOpticalZoom().get(0), is("10x"));
        assertThat(response.get().getProduct().getAspects().getMediaFormat().get(0), is("Flash Drive (SSD)"));
        assertThat(response.get().getProduct().getAspects().getStorageType().get(0), is("Removable"));
        assertThat(response.get().getProduct().getImageUrls().get(0), is("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg"));
        assertThat(response.get().getPackageWeightAndSize().getDimensions().getUnit(), is(INCH));
        assertThat(response.get().getPackageWeightAndSize().getDimensions().getWidth(), is(2.32));
        assertThat(response.get().getPackageWeightAndSize().getDimensions().getLength(), is(1.16));
        assertThat(response.get().getPackageWeightAndSize().getDimensions().getHeight(), is(1.61));
        assertThat(response.get().getPackageWeightAndSize().getWeight().getUnit(), is(POUND));
        assertThat(response.get().getPackageWeightAndSize().getWeight().getValue(), is(2.25));
        assertThat(response.get().getAvailability().getShipToLocationAvailability().getQuantity(), is(50));
        assertThat(response.get().getAvailability().getShipToLocationAvailability().getAllocationByFormat().getFixedPrice(), is(50));
    }
    @Test
    void getInventoryItems_requestSerialize() throws InterruptedException {
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

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/inventory/v1/inventory_item?limit=20&offset=0"));
    }
    @Test
    void getInventoryItems_responseDeserialize() {
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
        assertThat(response.get().getInventoryItems().get(0).getProduct().getBrand(), is("GoPro"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getMpn(), is("CHDHX-401"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getTitle(), is("GoPro Hero4 Helmet Cam"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getDescription(), is("New GoPro Hero4 Helmet Cam. Unopened box."));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getTitle(), is("GoPro Hero4 Helmet Cam"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getDescription(), is("New GoPro Hero4 Helmet Cam. Unopened box."));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getBrand().get(0), is("GoPro"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getType().get(0), is("Helmet/Action"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getRecordingDefinition().get(0), is("High Definition"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getOpticalZoom().get(0), is("10x"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getMediaFormat().get(0), is("Flash Drive (SSD)"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getAspects().getStorageType().get(0), is("Removable"));
        assertThat(response.get().getInventoryItems().get(0).getProduct().getImageUrls().get(0), is("https://i.ebayimg.com/images/g/ySgAAOSw4-hZsdNS/s-l1600.jpg"));
        assertThat(response.get().getInventoryItems().get(0).getPackageWeightAndSize(), is(nullValue()));
        assertThat(response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getQuantity(), is(50));
        assertThat(response.get().getInventoryItems().get(0).getAvailability().getShipToLocationAvailability().getAllocationByFormat(), is(nullValue()));
    }
    @Test
    void createOrUpdateInventoryItem_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(INVENTORY_ITEM_JSON).orElse(null)))
        );
        inventoryItemClient.createOrUpdate(new InventoryItem(), "123");

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(PUT.name()));
        assertThat(request.getPath(), is("/sell/inventory/v1/inventory_item/123"));
    }
    @Test
    void createOrUpdateInventoryItem_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Objects.requireNonNull(readStringFromFile(INVENTORY_ITEM_JSON).orElse(null)))
        );

        InventoryItem inventoryItem = new InventoryItem();
        Optional<InventoryItem> response = inventoryItemClient.createOrUpdate(inventoryItem, "123");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getSku(), is("123"));
    }
    @Test
    void deleteInventoryItem_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        inventoryItemClient.delete("6196932000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(DELETE.name()));
        assertThat(request.getPath(), is("/sell/inventory/v1/inventory_item/6196932000"));
    }
    @Test
    void deleteInventoryItem_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(204)
        );
        Optional<InventoryItem> response = inventoryItemClient.delete("6196932000");

        assertThat(response.isPresent(), is(false));
    }
}