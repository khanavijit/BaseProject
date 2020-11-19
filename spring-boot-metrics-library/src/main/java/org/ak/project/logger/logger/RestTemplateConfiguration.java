package org.ak.project.logger.logger;

import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final RestTemplateBuilder templateBuilder;

    @Bean
    public RestTemplate restTemplate(Tracer tracer) {
        return templateBuilder
                .setReadTimeout(Duration.ofMinutes(2))
                .interceptors(Collections.singletonList(new TracingRestTemplateInterceptor(tracer)))
                .build();
    }
}
