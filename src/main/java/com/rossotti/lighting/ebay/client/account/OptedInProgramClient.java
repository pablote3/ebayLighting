package com.rossotti.lighting.ebay.client.account;

import com.rossotti.lighting.ebay.client.BaseClient;
import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.webClient.WebClientProperties;
import com.rossotti.lighting.ebay.model.account.program.Program;
import com.rossotti.lighting.ebay.model.account.program.Programs;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
public class OptedInProgramClient extends BaseClient {
    private static final String optedInPrograms = "opted_in_programs";
    private static final String optInProgram = "opt_in_program";
    private static final String optOutProgram = "opt_out_program";

    public OptedInProgramClient(WebClient webClient, AppConfig appConfig, ServerConfig serverConfig) {
        this.webClient = webClient;
        this.appConfig = appConfig;
        this.serverConfig = serverConfig;
    }

    public Optional<Programs> getOptedInPrograms() {
        WebClientProperties properties = buildProperties(optedInPrograms, HttpMethod.GET, null, null);
        return webClientCall(properties, Programs.class);
    }
    public Optional<Program> optIntoProgram(final Program program) {
        WebClientProperties properties = buildProperties(optInProgram, HttpMethod.POST, null, null);
        return webClientCall(properties, Program.class, program);
    }
    public Optional<Program> optOutOfProgram(final Program program) {
        WebClientProperties properties = buildProperties(optOutProgram, HttpMethod.POST, null, null);
        return webClientCall(properties, Program.class, program);
    }
}