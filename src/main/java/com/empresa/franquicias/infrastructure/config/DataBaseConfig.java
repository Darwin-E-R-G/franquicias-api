package com.empresa.franquicias.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.empresa.franquicias.infrastructure.entity")
@EnableJpaRepositories(basePackages = "com.empresa.franquicias.infrastructure.repository")
public class DataBaseConfig {
}
