-- Migration: 003-create-dialogue-system.sql
-- Version: 1.0.0
-- Date: 2025-11-07 00:24
-- Description: Создание таблиц для системы диалогов

-- Dependencies: 001-expand-quests-table.sql

BEGIN;

-- =====================================================
-- TABLE: dialogue_nodes
-- =====================================================

CREATE TABLE IF NOT EXISTS dialogue_nodes (
    id SERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL,
    node_id INTEGER NOT NULL,
    
    -- NPC и локация
    npc_id VARCHAR(100) NOT NULL,
    npc_name VARCHAR(200) NOT NULL,
    location_id VARCHAR(100),
    
    -- Текст диалога
    dialogue_text TEXT NOT NULL,
    emotion VARCHAR(50) DEFAULT 'neutral',
    voice_line_id VARCHAR(100),
    
    -- Условия для отображения узла
    required_flags JSONB DEFAULT '[]'::jsonb,
    required_reputation JSONB DEFAULT '{}'::jsonb,
    blocked_flags JSONB DEFAULT '[]'::jsonb,
    
    -- Тип узла
    node_type VARCHAR(20) NOT NULL,
    
    -- Для combat узлов
    triggers_combat BOOLEAN DEFAULT FALSE,
    combat_encounter_id VARCHAR(100),
    
    -- Служебное
    is_critical_path BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_dialogue_quest FOREIGN KEY (quest_id) 
        REFERENCES quests(id) ON DELETE CASCADE,
    CONSTRAINT uq_quest_node UNIQUE(quest_id, node_id),
    CONSTRAINT ck_node_type CHECK (node_type IN ('dialogue', 'choice', 'skill_check', 'combat', 'end'))
);

-- Индексы
CREATE INDEX idx_dialogue_nodes_quest ON dialogue_nodes(quest_id);
CREATE INDEX idx_dialogue_nodes_type ON dialogue_nodes(node_type);
CREATE INDEX idx_dialogue_nodes_npc ON dialogue_nodes(npc_id);
CREATE INDEX idx_dialogue_nodes_critical ON dialogue_nodes(is_critical_path) WHERE is_critical_path = TRUE;

COMMENT ON TABLE dialogue_nodes IS 'Узлы диалогового дерева квеста';

-- =====================================================
-- TABLE: dialogue_choices
-- =====================================================

CREATE TABLE IF NOT EXISTS dialogue_choices (
    id SERIAL PRIMARY KEY,
    node_id INTEGER NOT NULL,
    choice_id VARCHAR(50) NOT NULL,
    choice_text TEXT NOT NULL,
    
    -- След узел
    next_node_id INTEGER,
    
    -- Условия доступности выбора
    required_class VARCHAR(50),
    required_origin VARCHAR(50),
    required_flags JSONB DEFAULT '[]'::jsonb,
    required_reputation JSONB DEFAULT '{}'::jsonb,
    required_items JSONB DEFAULT '[]'::jsonb,
    
    -- Skill check для выбора
    skill_check_skill VARCHAR(50),
    skill_check_dc INTEGER,
    skill_check_advantage JSONB DEFAULT '[]'::jsonb,
    skill_check_disadvantage JSONB DEFAULT '[]'::jsonb,
    
    -- Последствия выбора
    reputation_changes JSONB DEFAULT '{}'::jsonb,
    sets_flags JSONB DEFAULT '[]'::jsonb,
    unsets_flags JSONB DEFAULT '[]'::jsonb,
    gives_items JSONB DEFAULT '[]'::jsonb,
    removes_items JSONB DEFAULT '[]'::jsonb,
    
    -- Мета
    is_timed BOOLEAN DEFAULT FALSE,
    time_limit_seconds INTEGER,
    display_order INTEGER DEFAULT 0,
    
    -- Служебное
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_choice_node FOREIGN KEY (node_id) 
        REFERENCES dialogue_nodes(id) ON DELETE CASCADE,
    CONSTRAINT uq_node_choice UNIQUE(node_id, choice_id)
);

-- Индексы
CREATE INDEX idx_dialogue_choices_node ON dialogue_choices(node_id);
CREATE INDEX idx_dialogue_choices_class ON dialogue_choices(required_class) WHERE required_class IS NOT NULL;

COMMENT ON TABLE dialogue_choices IS 'Варианты выбора в узлах диалога';

COMMIT;

-- =====================================================
-- SAMPLE DATA
-- =====================================================

BEGIN;

-- Пример dialogue tree для MQ-2020-001 "Первые шаги"
INSERT INTO dialogue_nodes (quest_id, node_id, npc_id, npc_name, dialogue_text, emotion, node_type, is_critical_path)
VALUES 
    ('MQ-2020-001', 1, 'marco_fix', 'Marco "Fix" Sanchez', 
     'Эй, choom! Первый раз в Night City после всего этого дерьма?', 
     'friendly', 'dialogue', TRUE),
    
    ('MQ-2020-001', 2, 'marco_fix', 'Marco "Fix" Sanchez',
     'Город изменился. Тебе нужна помощь. Что скажешь?',
     'neutral', 'choice', TRUE),
    
    ('MQ-2020-001', 10, 'marco_fix', 'Marco "Fix" Sanchez',
     'Отлично! Добро пожаловать в команду. Первое задание...',
     'happy', 'dialogue', TRUE),
    
    ('MQ-2020-001', 99, 'marco_fix', 'Marco "Fix" Sanchez',
     'Твоё дело. Но в этом городе без связей долго не протянешь.',
     'disappointed', 'end', FALSE);

-- Выборы для node_id 2
INSERT INTO dialogue_choices (node_id, choice_id, choice_text, next_node_id, reputation_changes, sets_flags, display_order)
SELECT 
    dn.id, 'A1', '[Принять] Да, помощь не помешает.', 10,
    '{"Fixers": 10}'::jsonb,
    '["marco_ally"]'::jsonb,
    1
FROM dialogue_nodes dn
WHERE dn.quest_id = 'MQ-2020-001' AND dn.node_id = 2;

INSERT INTO dialogue_choices (node_id, choice_id, choice_text, next_node_id, reputation_changes, display_order)
SELECT 
    dn.id, 'A2', '[Отказать] Справлюсь сам.', 99,
    '{"Fixers": -5}'::jsonb,
    2
FROM dialogue_nodes dn
WHERE dn.quest_id = 'MQ-2020-001' AND dn.node_id = 2;

COMMIT;

-- =====================================================
-- VERIFICATION
-- =====================================================

-- Проверка dialogue tree
SELECT dn.quest_id, dn.node_id, dn.npc_name, dn.dialogue_text, dn.node_type
FROM dialogue_nodes dn
WHERE dn.quest_id = 'MQ-2020-001'
ORDER BY dn.node_id;

-- Проверка choices
SELECT dc.choice_id, dc.choice_text, dc.next_node_id
FROM dialogue_choices dc
JOIN dialogue_nodes dn ON dc.node_id = dn.id
WHERE dn.quest_id = 'MQ-2020-001';

