package com.necpgame.backjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.necpgame.backjava.repository")
@EntityScan(basePackages = "com.necpgame.backjava.entity")
public class NecpgameBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NecpgameBackendApplication.class, args);
    }
}

