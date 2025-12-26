package com.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableAsync
@org.springframework.cloud.client.discovery.EnableDiscoveryClient
public class OnlineShoppingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShoppingSystemApplication.class, args);
		System.out.println("Backend server is running.......");
	}

}
