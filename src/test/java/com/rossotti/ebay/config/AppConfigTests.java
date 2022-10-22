package com.rossotti.ebay.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class AppConfigTests {
    @Autowired
    AppConfig appConfig;

    @Test
    public void getString_Exists() {
        assertEquals("en-US", appConfig.getContentLanguage());
    }
    @Test
    public void getInt_Exists() {
        assertEquals(3, appConfig.getMaxRetries());
    }
    @Test
    public void getMap_Exists() {
        assertEquals("/sell/account/v1/payment_policy", appConfig.getResourceMap().get("payment_policy"));
    }
    @Test
    public void getMap_NotExists() {
        assertNull(appConfig.getResourceMap().get("lala"));
    }
}
