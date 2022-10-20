package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class OfferClient extends BaseClient {
    private final WebClientProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(OfferClient.class);

    public OfferClient(WebClient webClient, WebClientProperties properties, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.properties = properties;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<Offer> getByOfferOfferId(final String offerId) {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(offerId)) {
            builder.path("/" + offerId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Offer.class);
    }

    public Optional<Offers> getOffersBySku(final String sku) {
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        builder.queryParam("sku", sku);
        builder.queryParam("limit", properties.getLimit());
        builder.queryParam("offset", properties.getOffset());
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Offers.class);
    }
}