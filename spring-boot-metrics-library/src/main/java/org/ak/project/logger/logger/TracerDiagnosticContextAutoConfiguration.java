package org.ak.project.logger.logger;

import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerDiagnosticContextAutoConfiguration {

    // Code based on https://gist.github.com/mdvorak/80c52b56d2587f66b6201f3cf36c4d1c
    // and modified for later version of the library.

    @Configuration
    @ConditionalOnClass(TracerBuilderCustomizer.class)
    public static class JaegerDiagnosticContextConfiguration {

        @Bean
        @ConditionalOnMissingBean(org.ak.project.logger.logger.SpanDiagnosticContext.class)
        public JaegerDiagnosticContext jaegerDiagnosticContext() {
            return new JaegerDiagnosticContext();
        }

        @Bean
        public TracerBuilderCustomizer diagnosticContextTracerBuilderCustomizer(org.ak.project.logger.logger.SpanDiagnosticContext spanDiagnosticContext) {
            return new org.ak.project.logger.logger.JaegerDiagnosticContextTracerBuilderCustomizer(spanDiagnosticContext);
        }
    }
}
