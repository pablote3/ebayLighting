package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.BaseClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
import com.rossotti.ebay.model.account.program.Program;
import com.rossotti.ebay.model.account.program.Programs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class OptedInProgramClient extends BaseClient {
    private WebClientProperties properties;
    private static final String optedInPrograms = "opted_in_programs";
    private static final String optInProgram = "opt_in_program";
    private static final String optOutProgram = "opt_out_program";
    private static final Logger logger = LoggerFactory.getLogger(OptedInProgramClient.class);

    public OptedInProgramClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<Programs> getOptedInPrograms() {
        properties = createWebClientProperties(optedInPrograms);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.GET);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Programs.class);
    }
    public Optional<Program> optIntoProgram(final Program program) {
        properties = createWebClientProperties(optInProgram);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.POST);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Program.class, program);
    }
    public Optional<Program> optOutOfProgram(final Program program) {
        properties = createWebClientProperties(optOutProgram);
        UriComponentsBuilder builder = baseUriComponentBuilder(properties);
        properties.setUri(builder.build().toUri());
        properties.setMethod(HttpMethod.POST);
        logger.info(builder.build().toUriString());
        properties.setHeaders(createHeaders(properties));
        return webClientCall(properties, Program.class, program);
    }
}