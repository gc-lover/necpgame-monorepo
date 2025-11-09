package com.necpgame.backjava.entity.enums;

public enum LoreFactionType {
    CORPORATION,
    GANG,
    ORGANIZATION,
    GOVERNMENT;

    public static LoreFactionType fromValue(String value) {
        for (LoreFactionType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown faction type: " + value);
    }
}


