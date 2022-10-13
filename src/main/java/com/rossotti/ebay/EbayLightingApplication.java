package com.rossotti.ebay;

import com.rossotti.ebay.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class EbayLightingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbayLightingApplication.class, args);
	}

}
