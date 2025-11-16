-- Migration: 002-create-quest-branches.sql
-- Version: 1.0.0
-- Date: 2025-11-07 00:23
-- Description: Создание таблицы quest_branches для хранения ветвей квестов

-- Dependencies: 001-expand-quests-table.sql

BEGIN;

-- =====================================================
-- CREATE quest_branches TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS quest_branches (
    id SERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL,
    branch_id VARCHAR(50) NOT NULL,
    branch_name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Условия активации ветви
    conditions JSONB DEFAULT '{}'::jsonb,
    
    -- Модификаторы для этой ветви
    reward_modifiers JSONB DEFAULT '{}'::jsonb,
    
    -- Репутация изменения для этой ветви
    reputation_changes JSONB DEFAULT '{}'::jsonb,
    
    -- Награды специфичные для ветви
    branch_rewards JSONB DEFAULT '{}'::jsonb,
    
    -- Последствия ветви
    sets_flags JSONB DEFAULT '[]'::jsonb,
    unsets_flags JSONB DEFAULT '[]'::jsonb,
    unlocks_quests JSONB DEFAULT '[]'::jsonb,
    locks_quests JSONB DEFAULT '[]'::jsonb,
    
    -- Влияние на мир
    world_state_changes JSONB DEFAULT '{}'::jsonb,
    
    -- Мета
    difficulty_modifier DECIMAL(3,2) DEFAULT 1.0,
    moral_weight VARCHAR(20),
    
    -- Служебные
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_branch_quest FOREIGN KEY (quest_id) 
        REFERENCES quests(id) ON DELETE CASCADE,
    CONSTRAINT uq_quest_branch UNIQUE(quest_id, branch_id),
    CONSTRAINT ck_difficulty_modifier CHECK (difficulty_modifier >= 0.1 AND difficulty_modifier <= 5.0),
    CONSTRAINT ck_moral_weight CHECK (moral_weight IN ('GOOD', 'EVIL', 'NEUTRAL', 'GREY'))
);

-- Индексы
CREATE INDEX idx_quest_branches_quest ON quest_branches(quest_id);
CREATE INDEX idx_quest_branches_moral ON quest_branches(moral_weight) WHERE moral_weight IS NOT NULL;

-- Комментарии
COMMENT ON TABLE quest_branches IS 'Ветви/пути прохождения квеста';
COMMENT ON COLUMN quest_branches.conditions IS 'JSONB: условия активации {flags: [], reputation: {}, choices: []}';
COMMENT ON COLUMN quest_branches.world_state_changes IS 'JSONB: изменения world state';
COMMENT ON COLUMN quest_branches.difficulty_modifier IS 'Модификатор сложности: 0.8 = легче, 1.2 = сложнее';

COMMIT;

-- =====================================================
-- SAMPLE DATA (примеры)
-- =====================================================

BEGIN;

-- Пример: MQ-2020-005 "Чистый канал" - 2 ветви
INSERT INTO quest_branches (quest_id, branch_id, branch_name, description, conditions, reputation_changes, sets_flags, unlocks_quests, locks_quests, moral_weight)
VALUES 
    -- NetWatch path
    ('MQ-2020-005', 'netwatch', 'Путь NetWatch', 'Поддержать NetWatch в борьбе за порядок в NET',
     '{"choice": "choose_netwatch"}'::jsonb,
     '{"NetWatch": 25, "Voodoo_Boys": -20}'::jsonb,
     '["netwatch_ally", "voodoo_enemy"]'::jsonb,
     '["SQ-2030-002", "SQ-2045-001"]'::jsonb,
     '["SQ-2030-VB-001", "SQ-2045-VB-002"]'::jsonb,
     'NEUTRAL'),
    
    -- Voodoo Boys path
    ('MQ-2020-005', 'voodoo', 'Путь Voodoo Boys', 'Поддержать Voodoo Boys в борьбе за свободу NET',
     '{"choice": "choose_voodoo"}'::jsonb,
     '{"Voodoo_Boys": 30, "NetWatch": -25}'::jsonb,
     '["voodoo_ally", "netwatch_enemy"]'::jsonb,
     '["SQ-2045-003", "SQ-2078-004"]'::jsonb,
     '["SQ-2030-NW-001", "SQ-2045-NW-002"]'::jsonb,
     'NEUTRAL');

-- Пример: MQ-2045-006 "Тёплые коридоры" - 2 ветви (КРИТИЧНО)
INSERT INTO quest_branches (quest_id, branch_id, branch_name, description, conditions, reputation_changes, sets_flags, unlocks_quests, locks_quests, moral_weight, world_state_changes)
VALUES 
    -- Support AI cults
    ('MQ-2045-006', 'support_cults', 'Поддержать культы', 'Поддержать AI-культы и трансгуманизм',
     '{"choice": "support_ai_cults"}'::jsonb,
     '{"Church_of_Digital_God": 50, "NetWatch": -30, "AI_Cults": 40}'::jsonb,
     '["ai_sympathizer", "cult_supporter"]'::jsonb,
     '["SQ-2078-004", "ending_transcendence_available"]'::jsonb,
     '["SQ-2060-NW-004", "ending_corporatocracy"]'::jsonb,
     'GREY',
     '{"ai_cults_legal": true, "transhumanism_accepted": true}'::jsonb),
    
    -- Ban AI cults
    ('MQ-2045-006', 'ban_cults', 'Запретить культы', 'Запретить AI-культы, защитить традиционное человечество',
     '{"choice": "ban_ai_cults"}'::jsonb,
     '{"NetWatch": 40, "Church_of_Digital_God": -50, "AI_Cults": -60}'::jsonb,
     '["ai_enemy", "traditionalist"]'::jsonb,
     '[]'::jsonb,
     '["SQ-2045-003", "SQ-2078-004", "ending_transcendence"]'::jsonb,
     'NEUTRAL',
     '{"ai_cults_banned": true, "traditionalism_enforced": true}'::jsonb);

COMMIT;

-- =====================================================
-- VERIFICATION
-- =====================================================

-- Проверка таблицы
SELECT * FROM quest_branches LIMIT 5;

-- Проверка связей
SELECT q.id, q.name, qb.branch_id, qb.branch_name
FROM quests q
LEFT JOIN quest_branches qb ON q.id = qb.quest_id
WHERE q.has_branches = TRUE;

-- =====================================================
-- ROLLBACK SCRIPT
-- =====================================================

-- Сохранить в: rollback/002-rollback-quest-branches.sql
/*
BEGIN;

DROP TABLE IF EXISTS quest_branches CASCADE;

COMMIT;
*/

