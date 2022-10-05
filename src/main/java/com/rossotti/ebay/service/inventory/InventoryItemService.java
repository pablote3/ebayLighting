package com.rossotti.ebay.service.inventory;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItem;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItems;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class InventoryItemService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellInventoryPath = "sell/inventory/v1/";
    private static final String inventoryItemPath = "inventory_item";

    public InventoryItemService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public InventoryItem getInventoryItem(String sku) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellInventoryPath)
                .path(inventoryItemPath)
                .path("/" + sku)
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(InventoryItem.class)
                .block();
    }

    public InventoryItems getInventoryItems() {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellInventoryPath)
                .path(inventoryItemPath)
                .queryParam("limit", properties.getLimit())
                .queryParam("offset", properties.getOffset())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(InventoryItems.class)
                .block();
    }
}
