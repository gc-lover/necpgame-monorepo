-- Migration: 005-create-world-state-system.sql
-- Version: 1.0.0
-- Date: 2025-11-07 00:26
-- Description: Создание таблиц для гибридной системы world state

BEGIN;

-- =====================================================
-- TABLE: player_world_state (Personal Impact)
-- =====================================================

CREATE TABLE IF NOT EXISTS player_world_state (
    id SERIAL PRIMARY KEY,
    character_id UUID NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    
    -- Метаданные
    set_by_quest VARCHAR(100),
    set_by_choice VARCHAR(50),
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_character_state UNIQUE(character_id, state_key)
);

CREATE INDEX idx_player_world_state_character ON player_world_state(character_id);
CREATE INDEX idx_player_world_state_key ON player_world_state(state_key);

COMMENT ON TABLE player_world_state IS 'Персональное состояние мира игрока (видит только он)';

-- =====================================================
-- TABLE: server_world_state (Collective Impact)
-- =====================================================

CREATE TABLE IF NOT EXISTS server_world_state (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    
    -- Voting system
    player_votes INTEGER DEFAULT 0,
    threshold_required INTEGER NOT NULL DEFAULT 1000,
    vote_weight_total INTEGER DEFAULT 0,
    
    -- Status
    status VARCHAR(20) DEFAULT 'pending',
    activated_at TIMESTAMP,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_server_state UNIQUE(server_id, state_key),
    CONSTRAINT ck_status CHECK (status IN ('pending', 'active', 'expired', 'cancelled'))
);

CREATE INDEX idx_server_world_state_server ON server_world_state(server_id);
CREATE INDEX idx_server_world_state_key ON server_world_state(state_key);
CREATE INDEX idx_server_world_state_status ON server_world_state(status);

COMMENT ON TABLE server_world_state IS 'Коллективное состояние сервера (все игроки видят)';
COMMENT ON COLUMN server_world_state.threshold_required IS 'Порог голосов для активации (default: 60% онлайна)';

-- =====================================================
-- TABLE: world_state_votes
-- =====================================================

CREATE TABLE IF NOT EXISTS world_state_votes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    server_id VARCHAR(50) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    character_id UUID NOT NULL,
    
    -- Vote
    vote_value JSONB NOT NULL,
    weight INTEGER DEFAULT 1,
    
    -- Metadata
    voted_via_quest VARCHAR(100),
    
    -- Timestamp
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_character_vote UNIQUE(server_id, state_key, character_id)
);

CREATE INDEX idx_world_votes_server_key ON world_state_votes(server_id, state_key);
CREATE INDEX idx_world_votes_character ON world_state_votes(character_id);

COMMENT ON TABLE world_state_votes IS 'Голоса игроков за изменение состояния сервера';
COMMENT ON COLUMN world_state_votes.weight IS 'Вес голоса (reputation-based: 1-10)';

-- =====================================================
-- TABLE: faction_world_state (Faction Impact)
-- =====================================================

CREATE TABLE IF NOT EXISTS faction_world_state (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    faction_id VARCHAR(100) NOT NULL,
    state_key VARCHAR(100) NOT NULL,
    state_value JSONB NOT NULL,
    
    -- Contribution tracking
    member_contributions INTEGER DEFAULT 0,
    top_contributors JSONB DEFAULT '[]'::jsonb,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_faction_state UNIQUE(server_id, faction_id, state_key)
);

CREATE INDEX idx_faction_world_state_server ON faction_world_state(server_id);
CREATE INDEX idx_faction_world_state_faction ON faction_world_state(faction_id);
CREATE INDEX idx_faction_world_state_key ON faction_world_state(state_key);

COMMENT ON TABLE faction_world_state IS 'Состояние мира по фракциям (видят члены фракции)';

-- =====================================================
-- TABLE: territory_control
-- =====================================================

CREATE TABLE IF NOT EXISTS territory_control (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(50) NOT NULL,
    territory_id VARCHAR(100) NOT NULL,
    
    -- Control
    controlling_faction VARCHAR(100),
    control_percentage INTEGER DEFAULT 0,
    contested BOOLEAN DEFAULT FALSE,
    
    -- Battle data
    last_battle_at TIMESTAMP,
    next_battle_at TIMESTAMP,
    
    -- History
    previous_controller VARCHAR(100),
    control_changed_at TIMESTAMP,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_territory UNIQUE(server_id, territory_id),
    CONSTRAINT ck_control_percentage CHECK (control_percentage >= 0 AND control_percentage <= 100)
);

CREATE INDEX idx_territory_control_server ON territory_control(server_id);
CREATE INDEX idx_territory_control_faction ON territory_control(controlling_faction) WHERE controlling_faction IS NOT NULL;
CREATE INDEX idx_territory_control_contested ON territory_control(contested) WHERE contested = TRUE;

COMMENT ON TABLE territory_control IS 'Контроль территорий фракциями';

COMMIT;

-- =====================================================
-- SAMPLE DATA
-- =====================================================

BEGIN;

-- Примеры server world state
INSERT INTO server_world_state (server_id, state_key, state_value, threshold_required, status)
VALUES 
    ('server-eu-01', 'fifth_war_winner', '{"faction": "pending"}'::jsonb, 1000, 'pending'),
    ('server-eu-01', 'blackwall_integrity', '{"value": 75}'::jsonb, 500, 'active'),
    ('server-eu-01', 'corpo_control_nc', '{"value": 85}'::jsonb, 800, 'active');

-- Примеры territory control
INSERT INTO territory_control (server_id, territory_id, controlling_faction, control_percentage, contested)
VALUES 
    ('server-eu-01', 'night_city_watson', 'Maelstrom', 70, FALSE),
    ('server-eu-01', 'night_city_heywood', 'Valentinos', 85, FALSE),
    ('server-eu-01', 'night_city_pacifica', 'Voodoo_Boys', 90, FALSE),
    ('server-eu-01', 'night_city_downtown', 'Arasaka', 60, TRUE);

COMMIT;

-- =====================================================
-- HELPER FUNCTIONS
-- =====================================================

-- Функция для вычисления веса голоса
CREATE OR REPLACE FUNCTION calculate_vote_weight(p_character_id UUID, p_faction_id VARCHAR)
RETURNS INTEGER AS $$
DECLARE
    reputation INTEGER;
    base_weight INTEGER := 1;
BEGIN
    -- Get reputation with faction
    -- SELECT reputation INTO reputation FROM character_reputation 
    -- WHERE character_id = p_character_id AND faction_id = p_faction_id;
    
    -- Вес голоса: 1-10 based on reputation
    -- reputation 0-20: weight 1
    -- reputation 20-40: weight 2
    -- ...
    -- reputation 80-100: weight 10
    
    RETURN GREATEST(1, LEAST(10, (COALESCE(reputation, 0) / 10) + 1));
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки достижения порога
CREATE OR REPLACE FUNCTION check_vote_threshold(p_server_id VARCHAR, p_state_key VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
    total_weight INTEGER;
    threshold INTEGER;
BEGIN
    SELECT vote_weight_total, threshold_required 
    INTO total_weight, threshold
    FROM server_world_state
    WHERE server_id = p_server_id AND state_key = p_state_key;
    
    RETURN (total_weight >= threshold);
END;
$$ LANGUAGE plpgsql;

COMMIT;

