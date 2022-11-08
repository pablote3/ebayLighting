package com.rossotti.lighting.ebay.client.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

@Getter
@Setter
public class WebClientProperties {
    private String scheme;
    private String host;
    private Integer port;
    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private String path;
}