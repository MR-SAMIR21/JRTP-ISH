package com.smr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JrtpEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JrtpEurekaServerApplication.class, args);
	}

}
