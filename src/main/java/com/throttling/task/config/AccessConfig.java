package com.throttling.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = {"classpath:application.yml"})
@ConfigurationProperties(prefix = "access")
public class AccessConfig {
    int minutes;
    int bursts;
}
