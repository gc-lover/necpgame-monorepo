-- Migration: 004-create-player-systems.sql
-- Version: 1.0.0
-- Date: 2025-11-07 00:25
-- Description: Создание таблиц для отслеживания выборов и флагов игрока

BEGIN;

-- =====================================================
-- TABLE: player_quest_choices (audit trail)
-- =====================================================

CREATE TABLE IF NOT EXISTS player_quest_choices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    quest_id VARCHAR(100) NOT NULL,
    node_id INTEGER NOT NULL,
    choice_id VARCHAR(50) NOT NULL,
    branch_id VARCHAR(50),
    
    -- Последствия примененные
    consequences_applied JSONB DEFAULT '{}'::jsonb,
    
    -- Timestamp
    chosen_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_player_choices_quest FOREIGN KEY (quest_id) 
        REFERENCES quests(id) ON DELETE CASCADE
);

-- Индексы
CREATE INDEX idx_player_choices_character ON player_quest_choices(character_id);
CREATE INDEX idx_player_choices_quest ON player_quest_choices(quest_id);
CREATE INDEX idx_player_choices_character_quest ON player_quest_choices(character_id, quest_id);
CREATE INDEX idx_player_choices_timestamp ON player_quest_choices(chosen_at);

-- Партиционирование по времени (опционально для больших данных)
-- CREATE TABLE player_quest_choices_y2025m01 PARTITION OF player_quest_choices
--     FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

COMMENT ON TABLE player_quest_choices IS 'История всех выборов игрока в квестах (audit trail)';

-- =====================================================
-- TABLE: player_flags
-- =====================================================

CREATE TABLE IF NOT EXISTS player_flags (
    id SERIAL PRIMARY KEY,
    character_id UUID NOT NULL,
    flag_key VARCHAR(100) NOT NULL,
    flag_value JSONB NOT NULL,
    
    -- Метаданные
    set_by_quest VARCHAR(100),
    set_by_event VARCHAR(100),
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_character_flag UNIQUE(character_id, flag_key)
);

-- Индексы
CREATE INDEX idx_player_flags_character ON player_flags(character_id);
CREATE INDEX idx_player_flags_character_key ON player_flags(character_id, flag_key);
CREATE INDEX idx_player_flags_quest ON player_flags(set_by_quest) WHERE set_by_quest IS NOT NULL;
CREATE INDEX idx_player_flags_expires ON player_flags(expires_at) WHERE expires_at IS NOT NULL;

COMMENT ON TABLE player_flags IS 'Флаги состояния игрока (для quest conditions)';
COMMENT ON COLUMN player_flags.expires_at IS 'NULL = permanent, timestamp = temporary flag';

-- =====================================================
-- TABLE: quest_objectives (динамические цели)
-- =====================================================

CREATE TABLE IF NOT EXISTS quest_objectives (
    id SERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL,
    objective_id VARCHAR(50) NOT NULL,
    objective_text TEXT NOT NULL,
    objective_type VARCHAR(30) NOT NULL,
    
    -- Target
    target_id VARCHAR(100),
    target_count INTEGER DEFAULT 1,
    
    -- Optional
    is_optional BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_objectives_quest FOREIGN KEY (quest_id) 
        REFERENCES quests(id) ON DELETE CASCADE,
    CONSTRAINT uq_quest_objective UNIQUE(quest_id, objective_id),
    CONSTRAINT ck_objective_type CHECK (objective_type IN 
        ('kill', 'collect', 'talk', 'hack', 'stealth', 'escort', 'defend', 'reach_location', 'craft', 'trade'))
);

-- Индексы
CREATE INDEX idx_quest_objectives_quest ON quest_objectives(quest_id);

-- =====================================================
-- TABLE: player_dialogue_progress
-- =====================================================

CREATE TABLE IF NOT EXISTS player_dialogue_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    quest_id VARCHAR(100) NOT NULL,
    current_node_id INTEGER NOT NULL,
    
    -- История пройденных узлов
    visited_nodes JSONB DEFAULT '[]'::jsonb,
    
    -- Timestamps
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_dialogue_progress_quest FOREIGN KEY (quest_id) 
        REFERENCES quests(id) ON DELETE CASCADE,
    CONSTRAINT uq_character_quest_dialogue UNIQUE(character_id, quest_id)
);

-- Индексы
CREATE INDEX idx_dialogue_progress_character ON player_dialogue_progress(character_id);

COMMENT ON TABLE player_dialogue_progress IS 'Прогресс игрока по dialogue tree квеста';

COMMIT;

-- =====================================================
-- SAMPLE DATA
-- =====================================================

BEGIN;

-- Примеры флагов
INSERT INTO player_flags (character_id, flag_key, flag_value, set_by_quest)
VALUES 
    ('00000000-0000-0000-0000-000000000001'::uuid, 'netwatch_ally', 'true'::jsonb, 'MQ-2020-005'),
    ('00000000-0000-0000-0000-000000000001'::uuid, 'voodoo_enemy', 'true'::jsonb, 'MQ-2020-005'),
    ('00000000-0000-0000-0000-000000000001'::uuid, 'ai_sympathizer', 'true'::jsonb, 'SQ-2045-003'),
    ('00000000-0000-0000-0000-000000000001'::uuid, 'revolutionary', 'false'::jsonb, 'MQ-2030-006');

COMMIT;

-- =====================================================
-- VERIFICATION
-- =====================================================

SELECT * FROM player_flags WHERE character_id = '00000000-0000-0000-0000-000000000001'::uuid;

