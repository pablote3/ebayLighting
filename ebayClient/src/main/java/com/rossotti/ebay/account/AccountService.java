package com.rossotti.ebay.account;

import com.rossotti.ebay.config.WebClientProperties;
import com.rossotti.ebay.model.Account;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AccountService {
    private final WebClient webClient;
    private final WebClientProperties properties;

    public AccountService(WebClient webClient, WebClientProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }
    public Account getAccount() {
        return webClient
                .get()
                .uri(properties.getBaseUrl() + "/account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Account.class)
                .block();
    }
    public Account[] getAccounts() {
        return webClient
                .get()
                .uri(properties.getBaseUrl() + "/accounts")
                .retrieve()
                .bodyToMono(Account[].class)
                .block();
    }

}
