package org.ak.project.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "false")
@RequiredArgsConstructor
public class NonEurekaConfiguration {

    private final RestTemplateBuilder templateBuilder;

    @Bean
    public RestTemplate restTemplate() {
        return templateBuilder
                .setReadTimeout(Duration.ofMinutes(2))
                .build();
    }
}
