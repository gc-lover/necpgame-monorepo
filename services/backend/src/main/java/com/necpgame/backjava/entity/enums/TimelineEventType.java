package com.necpgame.backjava.entity.enums;

public enum TimelineEventType {
    WAR,
    ECONOMIC,
    TECHNOLOGICAL,
    SOCIAL,
    CATASTROPHE;

    public static TimelineEventType fromValue(String value) {
        for (TimelineEventType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown timeline event type: " + value);
    }
}


