package com.necpgame.backjava.entity.enums;

public enum TimelineImpactLevel {
    LOCAL,
    REGIONAL,
    GLOBAL;

    public static TimelineImpactLevel fromValue(String value) {
        for (TimelineImpactLevel level : values()) {
            if (level.name().equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown impact level: " + value);
    }
}


