package com.rossotti.ebay.client.inventory;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.client.util.QueryParam;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.util.WebClientProperties;
import com.rossotti.ebay.client.util.QueryParamEnum;
import com.rossotti.ebay.model.inventory.offer.Offer;
import com.rossotti.ebay.model.inventory.offer.Offers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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
