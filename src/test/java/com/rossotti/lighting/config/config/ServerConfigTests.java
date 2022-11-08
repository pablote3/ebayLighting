package com.rossotti.lighting.config.config;

import com.rossotti.lighting.config.ServerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles(profiles = "LOCAL")
public class ServerConfigTests {
    @Autowired
    ServerConfig serverConfig;

    @Test
    public void getString_Exists() {
        assertEquals("localhost", serverConfig.getHost());
    }
    @Test
    public void getString_NotExists() {
        assertNull(serverConfig.getScheme());
    }
}
