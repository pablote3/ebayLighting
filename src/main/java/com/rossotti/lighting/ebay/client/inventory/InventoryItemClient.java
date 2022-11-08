package com.rossotti.lighting.ebay.client.inventory;

import com.rossotti.lighting.ebay.client.BaseClient;
import com.rossotti.lighting.ebay.client.util.QueryParam;
import com.rossotti.lighting.ebay.client.util.QueryParamEnum;
import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.client.util.WebClientProperties;
import com.rossotti.lighting.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.lighting.ebay.model.inventory.inventoryItem.InventoryItems;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryItemClient extends BaseClient {
    private static final String pathKey = "inventory_item";

    public InventoryItemClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<InventoryItem> getByInventoryItemSku(final String sku) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, sku, null);
        properties.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, appConfig.getContentLanguage());
        return webClientCall(properties, InventoryItem.class);
    }
    public Optional<InventoryItems> getInventoryItems() {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.LIMIT, appConfig.getLimit()));
        queryParams.add(new QueryParam(QueryParamEnum.OFFSET, appConfig.getOffset()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, null, queryParams);
        return webClientCall(properties, InventoryItems.class);
    }
    public Optional<InventoryItem> createOrUpdate(final InventoryItem inventoryItem, final String sku) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.PUT, sku, null);
        properties.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, appConfig.getContentLanguage());
        return webClientCall(properties, InventoryItem.class, inventoryItem);
    }
    public Optional<InventoryItem> delete(final String sku) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.DELETE, sku, null);
        return webClientCall(properties, InventoryItem.class);
    }
}
