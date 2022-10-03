package com.rossotti.ebay.service.account;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.program.Programs;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OptedInProgramsService {
    private final WebClient webClient;
    private final WebClientProperties properties;
    private static final String sellAccountUrl = "sell/account/v1/";
    private static final String optedInProgramsUrl = "program/get_opted_in_programs";

    public OptedInProgramsService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public Programs getOptedInPrograms() {
        UriComponents uriComp = UriComponentsBuilder.newInstance()
                .scheme(properties.getScheme())
                .host(properties.getHost())
                .port(properties.getPort())
                .path(sellAccountUrl)
                .path(optedInProgramsUrl)
                .build();

        return webClient
                .get()
                .uri(uriComp.toUriString())
                .retrieve()
                .bodyToMono(Programs.class)
                .block();
    }
}
