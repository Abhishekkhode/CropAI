package com.CropAI.CropAI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories("com.CropAI.CropAI.Repositories")  // if needed
@EntityScan("com.CropAI.CropAI.Entity")
@SpringBootApplication
public class CropAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CropAiApplication.class, args);
	}

}
