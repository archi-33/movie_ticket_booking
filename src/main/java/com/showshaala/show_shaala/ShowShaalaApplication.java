package com.showshaala.show_shaala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShowShaalaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowShaalaApplication.class, args);
	}

}
