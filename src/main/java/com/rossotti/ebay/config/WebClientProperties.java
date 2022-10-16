package com.rossotti.ebay.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class WebClientProperties {
    private String scheme;
    private String host;
    private Integer port;
    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private String resourcePath;
    private String contentType;
    private Integer limit;
    private Integer offset;
    private String marketplaceId;
    private int httpTimeOutMs;
    private int maxRetries;
    private int backoffInterval;
}