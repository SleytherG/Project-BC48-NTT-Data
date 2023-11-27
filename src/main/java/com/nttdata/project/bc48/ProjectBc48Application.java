package com.nttdata.project.bc48;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ProjectBc48Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjectBc48Application.class, args);
	}

}
