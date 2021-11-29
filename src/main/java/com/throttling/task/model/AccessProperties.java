package com.throttling.task.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:access.properties"})
@ConfigurationProperties(prefix = "access")
public class AccessProperties {
    Short minutes;
    Short bursts;

    public Short getMinutes() {
        return minutes;
    }

    public void setMinutes(Short minutes) {
        this.minutes = minutes;
    }

    public Short getBursts() {
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
