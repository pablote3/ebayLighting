package com.rossotti.ebay.service.inventory;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.inventoryItem.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class InventoryItemService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellInventoryUrl = "sell/inventory/v1/";
    private static final String inventoryItemUrl = "inventory_item";

    public InventoryItemService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public InventoryItem getInventoryItem(String sku) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellInventoryUrl)
                .path(inventoryItemUrl)
                .path("/" + sku)
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(InventoryItem.class)
                .block();
    }

//    public FulfillmentPolicies getFulfillmentPolicies() {
//        UriComponents uriComp = UriComponentsBuilder.newInstance()
//                .scheme(properties.getScheme())
//                .host(properties.getHost())
//                .port(properties.getPort())
//                .path(sellInventoryUrl)
//                .path(inventoryItemUrl)
//                .queryParam("marketplace_id", properties.getMarketplaceId())
//                .build();
//
//        return webClient
//                .get()
//                .uri(uriComp.toUriString())
//                .retrieve()
//                .bodyToMono(FulfillmentPolicies.class)
//                .block();
//    }
}
