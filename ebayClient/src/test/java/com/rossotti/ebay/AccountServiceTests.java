package com.rossotti.ebay;

import com.rossotti.ebay.account.AccountService;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.Account;
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
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

@SpringBootTest
public class AccountServiceTests {
    private static MockWebServer mockWebServer;

    @Autowired
    WebClient webClient;
    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        WebClientProperties properties = new WebClientProperties();
        properties.setBaseUrl(mockWebServer.url("/").url().toString());
        accountService = new AccountService(WebClient.create(), properties);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    @Test
    void getAccounts_requestSerialization() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getJson())
        );
        accountService.getAccounts();
        RecordedRequest request = mockWebServer.takeRequest();

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/accounts", request.getPath());
    }
    @Test
    void getAccounts_responseDeserialization() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(getJson())
        );
        Account[] response = accountService.getAccounts();

        Assertions.assertEquals(9, response.length);
        Assertions.assertEquals("Sincere@april.biz", Objects.requireNonNull(Arrays.stream(response).findFirst().orElse(null)).getEmail());
    }

    private String getJson() {
        try (InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream("account-response.json")) {
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
