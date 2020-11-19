package org.ak.project.cache;

import com.hazelcast.config.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "hazelcast")
@EnableCaching
@Slf4j
@RequiredArgsConstructor
public class HazelcastSpringCacheConfiguration extends CachingConfigurerSupport {

    @Value("${hazelcast.service-dns:localhost}")
    private String serviceDns;

    @Value("${hazelcast.namespace:default}")
    private String namespace;

    @Value("${hazelcast.service-name:default}")
    private String serviceName;

    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "hazelcast")
    public Config hazelCastConfig(@Autowired(required = false) final BuildProperties buildProperties) {
        final Config config = new Config();
        config.setProperty("hazelcast.phone.home.enabled", "false");
        config.setProperty("hazelcast.shutdownhook.policy", "GRACEFUL");
        config.setProperty("hazelcast.graceful.shutdown.max.wait", "600");
        config.setProperty("hazelcast.cluster.version.auto.upgrade.enabled", "true");
        config.setProperty("hazelcast.client.heartbeat.interval", "2000");
        config.setProperty("hazelcast.client.heartbeat.timeout", "10000");
        config.setProperty("hazelcast.client.endpoint.remove.delay.seconds", "5");
        config.setProperty("hazelcast.logging.type", "slf4j");
        config.setProperty("hazelcast.jmx", "true");
        config.setProperty("hazelcast.jmx.detailed", "true");
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        if (StringUtils.isNoneEmpty(serviceDns) && !serviceDns.equals("localhost")) {
            config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                    .setProperty("service-dns", serviceDns);

        } else if (StringUtils.isNoneEmpty(namespace) && !namespace.equals("default")) {
            config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                    .setProperty("namespace", namespace)
                    .setProperty("pod-label-name", "deployment")
                    .setProperty("pod-label-value", buildProperties.getName() + "-" + buildProperties.getVersion())
                    .setProperty("resolve-not-ready-addresses", "true");
        }

        return config;
    }

}
