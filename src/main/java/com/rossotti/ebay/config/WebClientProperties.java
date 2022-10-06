package com.rossotti.ebay.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class WebClientProperties {
    private HttpMethod method;
    private String baseUrl;
    private String scheme;
    private String host;
    private Integer port;
    private String marketplaceId;
    private Integer limit;
    private Integer offset;
}
