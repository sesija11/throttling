package com.throttling.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:application.yml"})
@ConfigurationProperties(prefix = "access")
public class AccessConfig {
    int minutes;
    int bursts;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(Short minutes) {
        this.minutes = minutes;
    }

    public int getBursts() {
        return bursts;
    }

    public void setBursts(Short bursts) {
        this.bursts = bursts;
    }

    @Override
    public String toString() {
        return "AccessProperties{" +
                "minutes=" + minutes +
                ", bursts=" + bursts +
                '}';
    }
}
