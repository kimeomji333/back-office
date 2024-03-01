package com.sparta.lv3backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Lv3BackOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lv3BackOfficeApplication.class, args);
    }

}
