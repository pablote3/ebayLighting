package com.rossotti.ebay.service.inventory;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OfferService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellInventoryPath = "sell/inventory/v1/";
    private static final String offerPath = "offer";

    public OfferService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public Offer getOffer(String offerId) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellInventoryPath)
                .path(offerPath)
                .path("/" + offerId)
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(Offer.class)
                .block();
    }

    public Offers getOffersBySku(String sku) {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellInventoryPath)
                .path(offerPath)
                .queryParam("sku", sku)
                .queryParam("limit", properties.getLimit())
                .queryParam("offset", properties.getOffset())
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(Offers.class)
                .block();
    }
}
