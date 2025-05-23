package com.example.sidedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication()
@EnableCaching

public class SideDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SideDemoApplication.class, args);
    }

}
