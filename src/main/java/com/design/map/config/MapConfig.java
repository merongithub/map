package com.design.map.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @auther Meron Abraha 7/6/18
 */

@PropertySources({
                         @PropertySource("classpath:reference-swagger.properties")
                 })
@Configuration
public class MapConfig {

    @Bean
    public ObjectMapper referenceObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConfigurationProperties("reference.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate( new JdbcTemplate( dataSource() ) );
    }

    /**
     * Initiates the DB migration scripts to run upon service start.
     *
     * @return {@link Flyway}
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource( dataSource() );
        return flyway;
    }
}


