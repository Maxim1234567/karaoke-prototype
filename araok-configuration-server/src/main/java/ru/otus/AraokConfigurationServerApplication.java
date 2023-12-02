package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class AraokConfigurationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AraokConfigurationServerApplication.class, args);
    }

}
