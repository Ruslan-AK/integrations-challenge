package com.yaypay.challenge.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class DBConfiguration {
    @Value("${com.yaypay.challenge.db.schema:challenge}")
    private String schemaName;

    private GenericContainer container;

    @PostConstruct
    public void dataBase() {
        container = new GenericContainer("mysql:8.0.19")
                .withCommand("--default-authentication-plugin=mysql_native_password")
                .withEnv("MYSQL_ROOT_PASSWORD", "root")
                .withEnv("MYSQL_DATABASE", schemaName)
                .withEnv("MYSQL_ALLOW_EMPTY_PASSWORD", "yes")
                .waitingFor(Wait.forLogMessage(".*MySQL Community Server - GPL*.\\n", 3)
                        .withStartupTimeout(Duration.of(90, ChronoUnit.SECONDS)))
                .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("[MYSQL]"));
        container.start();
    }

    @PreDestroy
    public void destroyDataBase() {
        container.stop();
    }


    @Bean(name = "app.datasource")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setPassword("root");
        hikariConfig.setUsername("root");
        hikariConfig.setSchema(schemaName);
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:" + container.getMappedPort(3306) + "/" + schemaName);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("app.datasource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }


}
