package com.rossotti.ebay.client;

import com.rossotti.ebay.client.util.QueryParam;
import com.rossotti.ebay.client.util.WebClientProperties;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Getter
public abstract class BaseClient {

    @Autowired
    protected WebClient webClient;

    protected AppConfig appConfig;
    protected ServerConfig serverConfig;

    private final Logger logger = LoggerFactory.getLogger(BaseClient.class);

//        public void throwScapiResponseException(String errorMessage) { throwScapiResponseException(errorMessage, HttpStatus.BAD_REQUEST, null); }
//        public void throwScapiResponseException(String errorMessage, HttpStatus statusCode, Throwable t) { logger.throwScapiResponseException(createRestClientFailMsg() + " " + errorMessage, statusCode, t); }
//        protected void throwRestClientException(final Exception exception, String... params) { logger.throwRestClientException(exception, tackOnRestClientFailMsg(params)); }

    protected WebClientProperties buildProperties(String pathKey, HttpMethod httpMethod, String addlPath, List<QueryParam> queryParams) {
        WebClientProperties properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(addlPath)) {
            builder.path("/" + addlPath);
        }
        if (queryParams != null) {
            queryParams.forEach(c -> {
                builder.queryParam(c.getName().name(), c.getValue());
            });
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(httpMethod);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return properties;
    }
    protected WebClientProperties createWebClientProperties(String pathKey) {
        WebClientProperties properties = new WebClientProperties();
        properties.setScheme(serverConfig.getScheme());
        properties.setHost(serverConfig.getHost());
        properties.setPort(serverConfig.getPort());
        properties.setContentType(appConfig.getContentType());
        properties.setContentLanguage(appConfig.getContentLanguage());
        properties.setMarketplaceId(appConfig.getMarketplaceId().getCode());
        properties.setPath(appConfig.getResourceMap().get(pathKey));
        return properties;
    }
    protected HttpHeaders createHeaders(WebClientProperties properties) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, properties.getContentType());
        return headers;
    }
    protected UriComponentsBuilder baseUriComponentBuilder(WebClientProperties properties) {
        UriComponentsBuilder uriComp = UriComponentsBuilder.newInstance();
        uriComp.scheme(properties.getScheme());
        uriComp.host(properties.getHost());
        uriComp.port(properties.getPort());
        uriComp.path(properties.getPath());
        return uriComp;
    }

    protected <T> Optional<T> webClientCall(WebClientProperties properties, Class<T> responseClass) {
        return Optional.ofNullable(
            webClient.method(properties.getMethod())
                     .uri(properties.getUri())
                     .headers(h -> h.addAll(properties.getHeaders()))
                     .retrieve()
                     .bodyToMono(responseClass)
                     .timeout(Duration.ofMillis(appConfig.getHttpTimeOutMs()))
                     .retryWhen(Retry.backoff(appConfig.getMaxRetries(), Duration.ofMillis(appConfig.getBackoffInterval()))
                     .doAfterRetry((r) -> logRetryCount(r.totalRetries(), appConfig.getMaxRetries())))
                     .block()
        );
    }
    protected <T> Optional<T> webClientCall(WebClientProperties properties, Class<T> requestClass, Object body) {
        return Optional.ofNullable(
                webClient.method(properties.getMethod())
                        .uri(properties.getUri())
                        .headers(h -> h.addAll(properties.getHeaders()))
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(requestClass)
                        .timeout(Duration.ofMillis(appConfig.getHttpTimeOutMs()))
                        .retryWhen(Retry.backoff(appConfig.getMaxRetries(), Duration.ofMillis(appConfig.getBackoffInterval()))
                                .doAfterRetry((r) -> logRetryCount(r.totalRetries(), appConfig.getMaxRetries())))
                        .block()
        );
    }
    private void logRetryCount(long current, int max) {
        logger.info("Retry " + (current + 1) + " of " + max);
    }
}