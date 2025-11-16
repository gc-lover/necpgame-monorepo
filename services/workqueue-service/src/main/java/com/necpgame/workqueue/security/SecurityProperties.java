package com.necpgame.workqueue.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security.api")
public record SecurityProperties(
        Boolean enabled,
        String keyHeader,
        List<String> acceptedKeys
) {
    public SecurityProperties {
        enabled = Boolean.TRUE.equals(enabled);
        acceptedKeys = acceptedKeys == null ? List.of() : List.copyOf(acceptedKeys);
    }

    public boolean isEnabled() {
        return enabled;
    }
}