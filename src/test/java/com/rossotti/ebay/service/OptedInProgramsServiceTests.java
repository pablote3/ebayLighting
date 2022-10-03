package com.rossotti.ebay.service;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.program.Programs;
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

@SpringBootTest
public class OptedInProgramsServiceTests {
    private static final String OPTED_IN_PROGRAMS_JSON = "data/optedInPrograms.json";
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private OptedInProgramsService optedInProgramsService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = TestUtil.createWebClientProperties(mockWebServer.url("/"));
        optedInProgramsService = new OptedInProgramsService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void optedInPrograms_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OPTED_IN_PROGRAMS_JSON).orElse(null);
        Assertions.assertNotNull(str);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        optedInProgramsService.getOptedInPrograms();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/sell/account/v1/program/get_opted_in_programs", request.getPath());
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
        Programs response = optedInProgramsService.getOptedInPrograms();

        Assertions.assertEquals(2, response.getPrograms().size());
        Assertions.assertEquals("OUT_OF_STOCK_CONTROL", response.getPrograms().get(0).getProgramType());
        Assertions.assertEquals("SELLING_POLICY_MANAGEMENT", response.getPrograms().get(1).getProgramType());
    }
}
