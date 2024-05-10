package com.example.oneinkedoneproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OneinkedOneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneinkedOneProjectApplication.class, args);
    }

}
