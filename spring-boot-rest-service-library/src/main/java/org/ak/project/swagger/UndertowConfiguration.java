package org.ak.project.swagger;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

@Configuration
@Slf4j
public class UndertowConfiguration {

    @Value("${servlet.shutdown.delay:30000}")
    private Long shutdownDelay;

    @Bean
    public GracefulUndertowShutdown gracefulUndertowShutdown(org.ak.project.swagger.ShuttingDownHealthIndicator healthCheck) {
        return new GracefulUndertowShutdown(healthCheck, shutdownDelay);
    }

    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowCustomizer(
            GracefulUndertowShutdown gracefulUndertowShutdown) {
        return (factory) -> {
            factory.addDeploymentInfoCustomizers((builder) -> {
                builder.addInitialHandlerChainWrapper(gracefulUndertowShutdown);
            });
        };
    }

    private static class GracefulUndertowShutdown
            implements ApplicationListener<ContextClosedEvent>, HandlerWrapper {

        private volatile GracefulShutdownHandler handler;
        private volatile org.ak.project.swagger.ShuttingDownHealthIndicator healthCheck;
        private volatile Long shutdownDelay;

        GracefulUndertowShutdown(org.ak.project.swagger.ShuttingDownHealthIndicator healthCheck, Long shutdownDelay) {
            this.healthCheck = healthCheck;
            this.shutdownDelay = shutdownDelay;
        }

        @Override
        public HttpHandler wrap(HttpHandler handler) {
            this.handler = new GracefulShutdownHandler(handler);
            return this.handler;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            try {

                log.info("Signalling down");
                healthCheck.signalIsDown();
                // Give Openshift time to detect the down-status
                Thread.sleep(shutdownDelay);
                if (this.handler != null) {
                    log.info("Calling shutdown");
                    this.handler.shutdown();
                    log.info("Waiting for all requests to finish");
                    this.handler.awaitShutdown(60000);
                }
                log.info("Now shutting down");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
