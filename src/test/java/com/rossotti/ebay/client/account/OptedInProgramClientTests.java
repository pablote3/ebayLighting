package com.rossotti.ebay.client.account;

import com.rossotti.ebay.client.account.OptedInProgramClient;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.account.program.Programs;
import com.rossotti.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OptedInProgramClientTests {
    private static final String OPTED_IN_PROGRAMS_JSON = "data/account/optedInPrograms.json";
    private static final String pathKey = "opted_in_program";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private OptedInProgramClient optedInProgramClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"), appConfig, pathKey);
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        optedInProgramClient = new OptedInProgramClient(WebClient.create(), properties, appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void optedInPrograms_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OPTED_IN_PROGRAMS_JSON).orElse(null);
        assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        optedInProgramClient.getOptedInPrograms();
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/sell/account/v1/program/get_opted_in_programs", request.getPath());
    }

    @Test
    void optedInPrograms_responseDeserialization() {
        String json = TestUtil.readStringFromFile(OPTED_IN_PROGRAMS_JSON).orElse(null);
        Assertions.assertNotNull(json);
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<Programs> response = optedInProgramClient.getOptedInPrograms();

        assertTrue(response.isPresent());
        assertEquals(2, response.get().getPrograms().size());
        assertEquals("OUT_OF_STOCK_CONTROL", response.get().getPrograms().get(0).getProgramType());
        assertEquals("SELLING_POLICY_MANAGEMENT", response.get().getPrograms().get(1).getProgramType());
    }
}
