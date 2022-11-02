package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class OfferClient extends BaseClient {
    private WebClientProperties properties;
    private static final String pathKey = "offer";
    private static final Logger logger = LoggerFactory.getLogger(OfferClient.class);

    public OfferClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<Offer> getByOfferByOfferId(final String offerId) {
        properties = createWebClientProperties(pathKey);
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
        properties = createWebClientProperties(pathKey);
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
    public Optional<Offer> create(final Offer offer) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.POST);
        HttpHeaders headers = createHeaders(properties);
        headers.add(HttpHeaders.CONTENT_LANGUAGE, properties.getContentLanguage());
        properties.setHeaders(headers);
        return webClientCall(properties, Offer.class, offer);
    }
    public Optional<Offer> update(final Offer offer, final String offerId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(offerId)) {
            builder.path("/" + offerId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.PUT);
        logger.info(builder.build().toUriString());
        HttpHeaders headers = createHeaders(properties);
        headers.add(HttpHeaders.CONTENT_LANGUAGE, properties.getContentLanguage());
        properties.setHeaders(headers);
        return webClientCall(properties, Offer.class, offer);
    }
    public Optional<Offer> delete(final String offerId) {
        properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(offerId)) {
            builder.path("/" + offerId);
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.DELETE);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Offer.class);
    }
}
