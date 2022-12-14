package com.rossotti.lighting.ebay.client;

import com.rossotti.lighting.ebay.model.webClient.QueryParam;
import com.rossotti.lighting.ebay.model.webClient.WebClientProperties;
import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public abstract class BaseClient {
    protected WebClient webClient;
    protected AppConfig appConfig;
    protected ServerConfig serverConfig;
    private final Logger logger = LoggerFactory.getLogger(BaseClient.class);
    protected WebClientProperties buildProperties(String pathKey, HttpMethod httpMethod, String addlPath, List<QueryParam> queryParams) {
        WebClientProperties properties = createWebClientProperties(pathKey);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        if (isNotBlank(addlPath)) {
            builder.path("/" + addlPath);
        }
        if (queryParams != null) {
            queryParams.forEach(c -> builder.queryParam(c.getName().getCode(), c.getValue()));
        }
        properties.setUri(builder.build().toUri());
        properties.setMethod(httpMethod);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders());
        return properties;
    }
    private WebClientProperties createWebClientProperties(String pathKey) {
        WebClientProperties properties = new WebClientProperties();
        properties.setScheme(serverConfig.getScheme());
        properties.setHost(serverConfig.getHost());
        properties.setPort(serverConfig.getPort());
        properties.setPath(appConfig.getResourceMap().get(pathKey));
        return properties;
    }
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, appConfig.getContentType());
        return headers;
    }
    private UriComponentsBuilder baseUriComponentBuilder(WebClientProperties properties) {
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