package com.example.logger;

import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.service.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggerApplication.class, args);
	}
	@Bean
	CommandLineRunner run(ClientService clientService){
		return args -> {
			Client client = new Client(0,"admin","admin@gmail.com","Password123!", ClientRole.ADMIN, UUID.randomUUID().toString());
			clientService.save(client);
			clientService.setRoleToAdmin(client);

		};

		};

}
