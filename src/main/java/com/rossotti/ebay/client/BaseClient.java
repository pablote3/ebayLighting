package com.rossotti.ebay.client;

import com.vsp.api.home.model.Home;
import com.vsp.api.submitclaimapi.config.AppConfig;
import com.vsp.api.submitclaimapi.config.key.RetryInfo;
import com.vsp.api.submitclaimapi.exception.DownstreamClientException;
import com.vsp.api.submitclaimapi.helper.LogHandler;
import com.vsp.api.submitclaimapi.helper.cache.HomeCache;
import com.vsp.claim.common.webclient.WebClientParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

    @Component
    public abstract class BaseClient {

        @Autowired
        private AppConfig appConfig;
        @Autowired
        protected WebClient webClient;
        @Autowired
        protected HomeCache homeCache;
        @Autowired
        protected LogHandler logger;

        @Autowired
        private Environment environment;

        private static final String DEFAULT_RETRY_INFO_KEY = "default";

        public LogHandler getLogger() { return logger; }
        protected AppConfig getAppConfig() { return appConfig; }
        protected Environment getEnvironment() { return environment; }
        protected RetryInfo getDefaultRetryInfo() { return appConfig.getRetryInfos().get(DEFAULT_RETRY_INFO_KEY); }
        protected Home getHome() { return homeCache.getCachedHome(); }
        protected long getTimeOutMs() { return appConfig.getHttpTimeOutMs(); }
        private RetryInfo getRetryInfo(RetryInfo[] infos) { return infos == null || infos.length == 0 ? getDefaultRetryInfo() : infos[0]; }

        public void error(String... params) { logger.error(tackOnRestClientFailMsg(params)); }
        public String createRestClientFailMsg() { return String.format("RestClient_%s_failure", getClass().getSimpleName()); }
        public void throwScapiResponseException(String errorMessage) { throwScapiResponseException(errorMessage, HttpStatus.BAD_REQUEST, null); }
        public void throwScapiResponseException(String errorMessage, HttpStatus statusCode, Throwable t) { logger.throwScapiResponseException(createRestClientFailMsg() + " " + errorMessage, statusCode, t); }
        public void throwScapiResponseException(String... params) { logger.throwScapiResponseException(tackOnRestClientFailMsg(params)); }
        protected void throwRestClientException(final Exception exception, String... params) { logger.throwRestClientException(exception, tackOnRestClientFailMsg(params)); }

        private String[] tackOnRestClientFailMsg(String... params) {
            if (params == null || params.length == 0) { return params; }
            params[0] = createRestClientFailMsg() + " " + params[0];
            return params;
        }

        //Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<T> responseClass, RetryInfo... infos) {
            RetryInfo retryInfo = getRetryInfo(infos);
            return Optional.ofNullable(
                    webClient.method(webClientParam.getMethod())
                            .uri(webClientParam.getUri())
                            .headers(h -> h.addAll(webClientParam.getHeaders()))
                            .retrieve()
                            .bodyToMono(responseClass)
                            .timeout(Duration.ofMillis(getTimeOutMs()))
                            .retryWhen(Retry.backoff(retryInfo.getMaxRetries(), Duration.ofMillis(retryInfo.getRetryBackoffInterval()))
                                    .doAfterRetry((r) -> logRetryCount(r.totalRetries(), retryInfo.getMaxRetries())))
                            .block());
        }

        //Use when certain status codes should not retry. Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<T> responseClass, List<Integer> allowableErrorStatuses, RetryInfo... infos) {
            RetryInfo retryInfo = getRetryInfo(infos);
            return Optional.ofNullable(
                    webClient.method(webClientParam.getMethod())
                            .uri(webClientParam.getUri())
                            .headers(h -> h.addAll(webClientParam.getHeaders()))
                            .retrieve()
                            .bodyToMono(responseClass)
                            .timeout(Duration.ofMillis(getTimeOutMs()))
                            .retryWhen(Retry.backoff(retryInfo.getMaxRetries(), Duration.ofMillis(retryInfo.getRetryBackoffInterval()))
                                    .filter(throwable -> shouldRetry(throwable, allowableErrorStatuses))
                                    .doAfterRetry((r) -> logRetryCount(r.totalRetries(), retryInfo.getMaxRetries())))
                            .block());
        }

        //Use when request and response classes are the same. Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<T> responseClass, Object body, RetryInfo... infos) {
            return webClientCall(webClientParam, responseClass, responseClass, body, infos);
        }

        //Use when request and response classes are the same and certain status codes should not retry. Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<T> responseClass, Object body, List<Integer> allowableErrorStatuses, RetryInfo... infos) {
            return webClientCall(webClientParam, responseClass, responseClass, body, allowableErrorStatuses, infos);
        }

        //Use when request and response classes are the different. Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<?> requestClass, Class<T> responseClass, Object body, RetryInfo... infos) {
            RetryInfo retryInfo = getRetryInfo(infos);
            return Optional.ofNullable(
                    webClient.method(webClientParam.getMethod())
                            .uri(webClientParam.getUri())
                            .headers(h -> h.addAll(webClientParam.getHeaders()))
                            .body(Mono.just(body), requestClass)
                            .retrieve()
                            .bodyToMono(responseClass)
                            .timeout(Duration.ofMillis(getTimeOutMs()))
                            .retryWhen(Retry.backoff(retryInfo.getMaxRetries(), Duration.ofMillis(retryInfo.getRetryBackoffInterval()))
                                    .doAfterRetry((r) -> logRetryCount(r.totalRetries(), retryInfo.getMaxRetries())))
                            .block());
        }

        //Use when request and response classes are the different and certain status codes should not retry. Provide a single RetryInfo in infos to override the default if necessary.
        protected <T> Optional<T> webClientCall(WebClientParam webClientParam, Class<?> requestClass, Class<T> responseClass, Object body, List<Integer> allowableErrorStatuses, RetryInfo... infos) {
            RetryInfo retryInfo = getRetryInfo(infos);
            return Optional.ofNullable(
                    webClient.method(webClientParam.getMethod())
                            .uri(webClientParam.getUri())
                            .headers(h -> h.addAll(webClientParam.getHeaders()))
                            .body(Mono.just(body), requestClass)
                            .retrieve()
                            .bodyToMono(responseClass)
                            .timeout(Duration.ofMillis(getTimeOutMs()))
                            .retryWhen(Retry.backoff(retryInfo.getMaxRetries(), Duration.ofMillis(retryInfo.getRetryBackoffInterval()))
                                    .filter(throwable -> shouldRetry(throwable, allowableErrorStatuses))
                                    .doAfterRetry((r) -> logRetryCount(r.totalRetries(), retryInfo.getMaxRetries())))
                            .block());
        }

        private void logRetryCount(long current, int max) {
            logger.info("Retry " + (current + 1) + " of " + max);
        }

        private boolean shouldRetry(Throwable throwable, List<Integer> allowableErrorStatuses) {
            return !(throwable instanceof DownstreamClientException && allowableErrorStatuses.contains(((DownstreamClientException) throwable).getStatus().value()));
        }

}
