package com.necpgame.backjava.entity.enums;

public enum LoreLocationType {
    CITY,
    BADLANDS,
    COMBAT_ZONE,
    CORPO_ZONE;

    public static LoreLocationType fromValue(String value) {
        for (LoreLocationType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown location type: " + value);
    }
}


