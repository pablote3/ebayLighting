package com.rossotti.lighting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix="server")
@ConfigurationPropertiesScan
@Getter
@Setter
public class ServerConfig {
    private String scheme;
    private String host;
    private Integer port;
}