package com.sevenhallo.oches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OchesApplication {

    public static void main(String[] args) {
        SpringApplication.run(OchesApplication.class, args);
    }

}
