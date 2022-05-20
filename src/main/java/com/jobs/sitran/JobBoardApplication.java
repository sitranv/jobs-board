package com.jobs.sitran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableConfigurationProperties({ApplicationProperties.class})
public class JobBoardApplication {

	public static void main(String[] args) {
		System.out.println("123");
		SpringApplication.run(JobBoardApplication.class, args);
	}

}
