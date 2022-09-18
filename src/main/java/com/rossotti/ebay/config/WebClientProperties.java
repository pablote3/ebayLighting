package com.rossotti.ebay.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
@Configuration
@Getter
@Setter
@NoArgsConstructor
public class WebClientProperties {
    private String baseUrl;
}
