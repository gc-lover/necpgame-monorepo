package com.necpgame.backjava.entity.enums;

public enum LoreCodexCategory {
    FACTIONS,
    LOCATIONS,
    CHARACTERS,
    EVENTS,
    TECHNOLOGY;

    public static LoreCodexCategory fromValue(String value) {
        for (LoreCodexCategory category : values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown codex category: " + value);
    }
}


