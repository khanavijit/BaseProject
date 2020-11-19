package org.ak.project.blog.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.cognitor.cassandra.migration.spring.CassandraMigrationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CassandraConfiguration {
    @Bean(name = CassandraMigrationAutoConfiguration.CQL_SESSION_BEAN_NAME)
    public CqlSession cassandraMigrationCqlSession(final CqlSessionBuilder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public CqlSession applicationCqlSession(final CqlSessionBuilder builder) {
        return builder.build();
    }
}
