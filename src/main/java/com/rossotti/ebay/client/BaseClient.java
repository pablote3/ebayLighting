package com.rossotti.ebay.client;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.WebClientProperties;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

@Component
@Getter
public abstract class BaseClient {
    private Logger logger = LoggerFactory.getLogger(BaseClient.class);
    @Autowired
    private AppConfig appConfig;

    @Autowired
    protected WebClient webClient;

//        public void throwScapiResponseException(String errorMessage) { throwScapiResponseException(errorMessage, HttpStatus.BAD_REQUEST, null); }
//        public void throwScapiResponseException(String errorMessage, HttpStatus statusCode, Throwable t) { logger.throwScapiResponseException(createRestClientFailMsg() + " " + errorMessage, statusCode, t); }
//        protected void throwRestClientException(final Exception exception, String... params) { logger.throwRestClientException(exception, tackOnRestClientFailMsg(params)); }

    protected UriComponentsBuilder baseUriComponentBuilder(WebClientProperties properties) {
        UriComponentsBuilder uriComp = UriComponentsBuilder.newInstance();
        uriComp.scheme(properties.getScheme());
        uriComp.host(properties.getHost());
        uriComp.port(properties.getPort());
        uriComp.path(appConfig.getResourceMap().get(properties.getResourcePath()));
        return uriComp;
    }

    protected <T> Optional<T> webClientCall(WebClientProperties webClientProperties, Class<T> responseClass) {
        return Optional.ofNullable(
            webClient.method(webClientProperties.getMethod())
                     .uri(webClientProperties.getUri())
                     .headers(h -> h.addAll(webClientProperties.getHeaders()))
                     .retrieve()
                     .bodyToMono(responseClass)
                     .timeout(Duration.ofMillis(appConfig.getHttpTimeOutMs()))
                     .retryWhen(Retry.backoff(appConfig.getMaxRetries(), Duration.ofMillis(appConfig.getBackoffInterval()))
                     .doAfterRetry((r) -> logRetryCount(r.totalRetries(), appConfig.getMaxRetries())))
                     .block()
        );
    }
    private void logRetryCount(long current, int max) {
        logger.info("Retry " + (current + 1) + " of " + max);
    }
    protected HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, appConfig.getContentType());
        return headers;
    }
}