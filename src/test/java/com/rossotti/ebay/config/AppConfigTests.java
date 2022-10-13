package com.rossotti.ebay.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AppConfigTests {
    @Autowired
    AppConfig appConfig;

    @Test
    public void getString_Valid() {
        assertEquals("EBAY_US", appConfig.getMarketplaceId());
    }
    @Test
    public void getInt_Exists() {
        assertEquals(3, appConfig.getMaxRetries());
    }
    @Test
    public void getInt_NotExists() {
        assertEquals(0, appConfig.getHttpTimeOutMs());
    }
}
