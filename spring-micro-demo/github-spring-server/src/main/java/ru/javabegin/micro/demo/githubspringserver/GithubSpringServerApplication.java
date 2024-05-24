package ru.javabegin.micro.demo.githubspringserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class GithubSpringServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubSpringServerApplication.class, args);
    }

}
