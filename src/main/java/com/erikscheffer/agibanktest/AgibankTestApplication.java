package com.erikscheffer.agibanktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgibankTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgibankTestApplication.class, args);
    }

}
