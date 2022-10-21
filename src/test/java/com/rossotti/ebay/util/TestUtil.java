package com.rossotti.ebay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.client.WebClientProperties;
import okhttp3.HttpUrl;
import java.io.InputStream;
import java.util.Optional;

public class TestUtil {
    public static ObjectMapper objectMapper;
    public static WebClientProperties createWebClientProperties(HttpUrl url, AppConfig appConfig, String pathKey) {
        WebClientProperties properties = new WebClientProperties();
        properties.setScheme(url.scheme());
        properties.setHost(url.host());
        properties.setPort(url.port());
        properties.setContentType(appConfig.getContentType());
        properties.setMarketplaceId(appConfig.getMarketplaceId());
        properties.setPath(appConfig.getResourceMap().get(pathKey));
        properties.setLimit(20);
        properties.setOffset(0);
        return properties;
    }

    public static ServerConfig createServerConfig(HttpUrl url) {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setScheme(url.scheme());
        serverConfig.setHost(url.host());
        serverConfig.setPort(url.port());
        return serverConfig;
    }

    public static <T> T createFromJson(Class<T> type, String fileName) {
        T retVal = null;
        try {
            retVal = objectMapper.readValue(readStringFromFile(fileName).get(), type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retVal;
    }
    public static Optional<String> readStringFromFile(String fileName) {
        try {
            InputStream jsonStream = TestUtil.class.getClassLoader().getResourceAsStream(fileName);
            return Optional.ofNullable(new String(jsonStream.readAllBytes()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
