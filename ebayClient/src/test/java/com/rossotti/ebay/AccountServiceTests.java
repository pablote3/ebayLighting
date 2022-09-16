package com.rossotti.ebay;

import com.rossotti.ebay.account.AccountService;
import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.Account;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;

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
    void makesTheCorrectRequest() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(getJson("account-response.json"))
        );
        Account[] response = accountService.getAccounts();

        Assert.assertTrue(response.length==9);
    }
//    @Test
//    void integrationTest() {
//        User[] users = accountService.getUsers();
//        Assert.assertTrue(users.length == 10);
//    }

    private String getJson(String path) {
        try {
            InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(path);
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
