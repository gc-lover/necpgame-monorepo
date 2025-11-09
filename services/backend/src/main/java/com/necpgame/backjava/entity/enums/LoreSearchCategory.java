package com.necpgame.backjava.entity.enums;

public enum LoreSearchCategory {
    ALL,
    UNIVERSE,
    FACTIONS,
    LOCATIONS,
    CHARACTERS;

    public static LoreSearchCategory fromValue(String value) {
        if (value == null || value.isBlank()) {
            return ALL;
        }
        for (LoreSearchCategory category : values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown lore search category: " + value);
    }
}


