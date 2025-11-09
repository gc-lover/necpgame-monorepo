-- Migration для Character
-- Generated from JPA Entity: Character.java
-- Auto-generated on 2025-11-04 04:14:51

CREATE TABLE IF NOT EXISTS characters (
    origin VARCHAR(255) NOT NULL,
    faction_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

