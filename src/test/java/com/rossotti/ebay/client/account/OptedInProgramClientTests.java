package com.rossotti.ebay.client.account;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.account.program.Programs;
import com.rossotti.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.account.program.ProgramTypeEnum.OUT_OF_STOCK_CONTROL;
import static com.rossotti.ebay.model.account.program.ProgramTypeEnum.SELLING_POLICY_MANAGEMENT;

@SpringBootTest
public class OptedInProgramClientTests {
    private static final String OPTED_IN_PROGRAMS_JSON = "data/account/optedInPrograms.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private OptedInProgramClient optedInProgramClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        optedInProgramClient = new OptedInProgramClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void optedInPrograms_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(OPTED_IN_PROGRAMS_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        optedInProgramClient.getOptedInPrograms();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/program/get_opted_in_programs"));
    }

    @Test
    void optedInPrograms_responseDeserialization() {
        String json = TestUtil.readStringFromFile(OPTED_IN_PROGRAMS_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<Programs> response = optedInProgramClient.getOptedInPrograms();

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getPrograms(), hasSize(2));
        assertThat(response.get().getPrograms().get(0).getProgramType(), is(OUT_OF_STOCK_CONTROL));
        assertThat(response.get().getPrograms().get(1).getProgramType(), is(SELLING_POLICY_MANAGEMENT));
    }
}
