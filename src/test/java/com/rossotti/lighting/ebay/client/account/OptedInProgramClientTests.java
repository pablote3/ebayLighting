package com.rossotti.lighting.ebay.client.account;

import com.rossotti.lighting.config.AppConfig;
import com.rossotti.lighting.config.ServerConfig;
import com.rossotti.lighting.ebay.model.account.program.Program;
import com.rossotti.lighting.ebay.model.account.program.Programs;
import com.rossotti.lighting.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.lighting.ebay.model.account.program.ProgramTypeEnum.OUT_OF_STOCK_CONTROL;
import static com.rossotti.lighting.ebay.model.account.program.ProgramTypeEnum.SELLING_POLICY_MANAGEMENT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest
public class OptedInProgramClientTests {
    private static final String OPTED_IN_PROGRAMS_JSON = "data/ebay/account/optedInPrograms.json";
    private static MockWebServer mockWebServer;
    private final BasicJsonTester json = new BasicJsonTester(this.getClass());
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
    void getOptedInPrograms_requestSerialize() throws InterruptedException {
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

        assertThat(request.getMethod(), is(GET.name()));
        assertThat(request.getPath(), is("/sell/account/v1/program/get_opted_in_programs"));
    }
    @Test
    void getOptedInPrograms_responseDeserialize() {
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
    @Test
    void optIntoProgram_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        optedInProgramClient.optIntoProgram(new Program());

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(POST.name()));
        assertThat(request.getPath(), is("/sell/account/v1/program/opt_in"));
    }
    @Test
    void optIntoProgram_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        Optional<Program> response = optedInProgramClient.optIntoProgram(new Program());
        assertThat(response.isPresent(), is(false));
    }
    @Test
    void optOutProgram_request() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        optedInProgramClient.optOutOfProgram(new Program());

        RecordedRequest request = mockWebServer.takeRequest();
        JsonContent<Object> body = json.from(request.getBody().readUtf8());

        assertThat(body, is(notNullValue()));
        assertThat(request.getMethod(), is(POST.name()));
        assertThat(request.getPath(), is("/sell/account/v1/program/opt_out"));
    }
    @Test
    void optOutProgram_response() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        Optional<Program> response = optedInProgramClient.optOutOfProgram(new Program());
        assertThat(response.isPresent(), is(false));
    }
}
