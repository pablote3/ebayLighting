package com.rossotti.lighting.ebay.client.inventory;

import com.rossotti.lighting.ebay.client.BaseClient;
import com.rossotti.lighting.ebay.model.webClient.QueryParam;
import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.webClient.WebClientProperties;
import com.rossotti.lighting.ebay.model.webClient.QueryParamEnum;
import com.rossotti.lighting.ebay.model.inventory.offer.Offer;
import com.rossotti.lighting.ebay.model.inventory.offer.Offers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OfferClient extends BaseClient {
    private static final String pathKey = "offer";

    public OfferClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }
    public Optional<Offer> getOfferByOfferId(final String offerId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, offerId, null);
        properties.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, appConfig.getContentLanguage());
        return webClientCall(properties, Offer.class);
    }
    public Optional<Offers> getOffersBySku(final String sku) {
        List<QueryParam> queryParams = new ArrayList<>();
        queryParams.add(new QueryParam(QueryParamEnum.SKU, sku));
        queryParams.add(new QueryParam(QueryParamEnum.LIMIT, appConfig.getLimit()));
        queryParams.add(new QueryParam(QueryParamEnum.OFFSET, appConfig.getOffset()));
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.GET, null, queryParams);
        return webClientCall(properties, Offers.class);
    }
    public Optional<Offer> create(final Offer offer) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.POST, null, null);
        properties.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, appConfig.getContentLanguage());
        return webClientCall(properties, Offer.class, offer);
    }
    public Optional<Offer> update(final Offer offer, final String offerId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.PUT, offerId, null);
        properties.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, appConfig.getContentLanguage());
        return webClientCall(properties, Offer.class, offer);
    }
    public Optional<Offer> delete(final String offerId) {
        WebClientProperties properties = buildProperties(pathKey, HttpMethod.DELETE, offerId, null);
        return webClientCall(properties, Offer.class);
    }
}
