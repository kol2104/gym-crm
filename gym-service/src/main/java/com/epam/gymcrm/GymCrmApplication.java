package com.epam.gymcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebSecurity
@EnableFeignClients
@EnableDiscoveryClient
public class GymCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymCrmApplication.class, args);
	}

}
