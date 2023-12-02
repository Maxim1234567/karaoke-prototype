package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AraokEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AraokEurekaServerApplication.class, args);
    }

}
