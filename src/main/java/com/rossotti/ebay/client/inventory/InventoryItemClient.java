package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class InventoryItemClient extends BaseClient {
    private WebClientProperties properties;
    private static final String pathKey = "inventory_item";
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemClient.class);

    public InventoryItemClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<InventoryItem> getByInventoryItemSku(final String sku) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(sku)) {
            builder.path("/" + sku);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, InventoryItem.class);
    }

    public Optional<InventoryItems> getInventoryItems() {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("limit", properties.getLimit());
        builder.queryParam("offset", properties.getOffset());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, InventoryItems.class);
    }
}
