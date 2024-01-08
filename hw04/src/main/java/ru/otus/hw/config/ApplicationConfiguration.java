package ru.otus.hw.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({AppProperties.class})
@SpringBootConfiguration
public class ApplicationConfiguration {
}
