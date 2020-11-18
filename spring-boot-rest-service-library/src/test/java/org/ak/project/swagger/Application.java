package org.ak.project.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.ak.project"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(org.ak.project.swagger.Application.class, args);
    }
}
