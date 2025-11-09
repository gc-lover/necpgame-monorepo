-- Extended Quest Tables for AAA MMORPG
-- Version: 1.0.0
-- Date: 2025-11-06 23:30

-- РАСШИРЕНИЕ базовой таблицы quests
ALTER TABLE quests ADD COLUMN IF NOT EXISTS has_branches BOOLEAN DEFAULT FALSE;
ALTER TABLE quests ADD COLUMN IF NOT EXISTS dialogue_tree_root INTEGER;
ALTER TABLE quests ADD COLUMN IF NOT EXISTS era VARCHAR(20);
ALTER TABLE quests ADD COLUMN IF NOT EXISTS required_quests JSONB;
ALTER TABLE quests ADD COLUMN IF NOT EXISTS required_flags JSONB;

-- НОВЫЕ ТАБЛИЦЫ для ветвления (см. phase1-inventory.md для полной структуры)

-- 1. Quest Branches
CREATE TABLE IF NOT EXISTS quest_branches (
    id SERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL REFERENCES quests(id),
    branch_id VARCHAR(50) NOT NULL,
    branch_name VARCHAR(200),
    conditions JSONB,
    sets_flags JSONB,
    unlocks_quests JSONB,
    locks_quests JSONB,
    UNIQUE(quest_id, branch_id)
);

-- 2. Dialogue Nodes
CREATE TABLE IF NOT EXISTS dialogue_nodes (
    id SERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL REFERENCES quests(id),
    node_id INTEGER NOT NULL,
    npc_id VARCHAR(100),
    dialogue_text TEXT NOT NULL,
    node_type VARCHAR(20) NOT NULL,
    required_flags JSONB,
    UNIQUE(quest_id, node_id)
);

-- 3. Player Quest Choices (audit)
CREATE TABLE IF NOT EXISTS player_quest_choices (
    id UUID PRIMARY KEY,
    character_id UUID NOT NULL,
    quest_id VARCHAR(100) NOT NULL,
    choice_id VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    consequence_applied JSONB
);

-- 4. Player Flags
CREATE TABLE IF NOT EXISTS player_flags (
    id SERIAL PRIMARY KEY,
    character_id UUID NOT NULL,
    flag_key VARCHAR(100) NOT NULL,
    flag_value JSONB NOT NULL,
    set_by_quest VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    UNIQUE(character_id, flag_key)
);

-- ИНДЕКСЫ для производительности
CREATE INDEX idx_quest_branches_quest ON quest_branches(quest_id);
CREATE INDEX idx_dialogue_nodes_quest ON dialogue_nodes(quest_id);
CREATE INDEX idx_player_choices_character ON player_quest_choices(character_id);
CREATE INDEX idx_player_flags_character ON player_flags(character_id);

-- История изменений:
-- v1.0.0 (2025-11-06 23:30) - Расширенные таблицы для квестов

