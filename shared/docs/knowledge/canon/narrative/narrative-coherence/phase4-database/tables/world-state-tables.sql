-- World State Tables for Hybrid Player Impact System
-- Version: 1.0.0
-- Date: 2025-11-06 23:31

-- 1. PERSONAL WORLD STATE (per character)
CREATE TABLE IF NOT EXISTS player_world_state (
    id SERIAL PRIMARY KEY,
    character_id UUID NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE(character_id, state_key)
);

-- 2. SERVER WORLD STATE (global for server)
CREATE TABLE IF NOT EXISTS server_world_state (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    player_votes INTEGER DEFAULT 0,
    threshold_required INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    updated_at TIMESTAMP NOT NULL,
    UNIQUE(server_id, state_key)
);

-- 3. FACTION WORLD STATE (per faction per server)
CREATE TABLE IF NOT EXISTS faction_world_state (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    faction_id VARCHAR(100) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    member_contributions INTEGER DEFAULT 0,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE(server_id, faction_id, state_key)
);

-- 4. WORLD STATE VOTES (голосование игроков)
CREATE TABLE IF NOT EXISTS world_state_votes (
    id UUID PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    character_id UUID NOT NULL,
    vote_value JSONB NOT NULL,
    weight INTEGER DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    UNIQUE(server_id, state_key, character_id)
);

-- 5. TERRITORY CONTROL (контроль территорий)
CREATE TABLE IF NOT EXISTS territory_control (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    territory_id VARCHAR(100) NOT NULL,
    controlling_faction VARCHAR(100),
    control_percentage INTEGER DEFAULT 0,
    contested BOOLEAN DEFAULT FALSE,
    last_battle TIMESTAMP,
    UNIQUE(server_id, territory_id)
);

-- ИНДЕКСЫ
CREATE INDEX idx_player_world_state_character ON player_world_state(character_id);
CREATE INDEX idx_server_world_state_server ON server_world_state(server_id);
CREATE INDEX idx_faction_world_state_faction ON faction_world_state(faction_id);
CREATE INDEX idx_world_state_votes_server ON world_state_votes(server_id);
CREATE INDEX idx_territory_control_server ON territory_control(server_id);

-- История изменений:
-- v1.0.0 (2025-11-06 23:31) - Таблицы для гибридной системы влияния

