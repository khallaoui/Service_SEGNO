package com.marketplace.segno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class SegnoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SegnoApplication.class, args);
    }
}
