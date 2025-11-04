package com.smr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JrtpElgibilityDeterminationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JrtpElgibilityDeterminationServiceApplication.class, args);
	}

}
