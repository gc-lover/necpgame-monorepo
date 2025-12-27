-- Issue: #140899116
-- liquibase formatted sql

--changeset backend:quest-branching-jsonb dbms:postgresql
--comment: Add branching logic support to quest system using JSONB approach

BEGIN;

-- Add branching_logic column to quest_definitions
ALTER TABLE gameplay.quest_definitions
ADD COLUMN IF NOT EXISTS branching_logic JSONB;

COMMENT ON COLUMN gameplay.quest_definitions.branching_logic IS 'JSON structure for quest branching logic - contains nodes, transitions, and choice definitions';

-- Add branching_state column to player_quest_progress (assuming this table exists)
-- If table does not exist, this will be handled by separate migration
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'gameplay' AND table_name = 'player_quest_progress') THEN
        ALTER TABLE gameplay.player_quest_progress
        ADD COLUMN IF NOT EXISTS branching_state JSONB;

        COMMENT ON COLUMN gameplay.player_quest_progress.branching_state IS 'Current branching state for player - tracks choices and progress through branching logic';
    ELSE
        RAISE NOTICE 'Table gameplay.player_quest_progress does not exist yet. Branching state column will be added when the table is created.';
    END IF;
END $$;

-- Performance indexes for JSONB branching queries
CREATE INDEX IF NOT EXISTS idx_quest_definitions_branching_logic
ON gameplay.quest_definitions USING GIN (branching_logic)
WHERE branching_logic IS NOT NULL;

-- Index for branching state queries (only if table exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'gameplay' AND table_name = 'player_quest_progress') THEN
        CREATE INDEX IF NOT EXISTS idx_player_quest_progress_branching_state
        ON gameplay.player_quest_progress USING GIN (branching_state)
        WHERE branching_state IS NOT NULL;
    END IF;
END $$;

-- Function to validate branching logic JSON structure
CREATE OR REPLACE FUNCTION validate_quest_branching_logic(branching_json JSONB)
RETURNS BOOLEAN AS $$
BEGIN
    -- Basic validation: check if required fields exist
    IF branching_json IS NULL THEN
        RETURN TRUE; -- NULL is allowed (no branching)
    END IF;

    -- Check for required structure
    IF NOT (branching_json ? 'version' AND branching_json ? 'nodes') THEN
        RETURN FALSE;
    END IF;

    -- Validate version format
    IF NOT (branching_json->>'version' ~ '^\d+\.\d+$') THEN
        RETURN FALSE;
    END IF;

    -- Check if nodes is an object
    IF jsonb_typeof(branching_json->'nodes') != 'object' THEN
        RETURN FALSE;
    END IF;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Add check constraint for branching logic validation
ALTER TABLE gameplay.quest_definitions
DROP CONSTRAINT IF EXISTS chk_quest_definitions_branching_logic;

ALTER TABLE gameplay.quest_definitions
ADD CONSTRAINT chk_quest_definitions_branching_logic
CHECK (validate_quest_branching_logic(branching_logic));

-- Function to extract branching node IDs for indexing
CREATE OR REPLACE FUNCTION extract_branching_node_ids(branching_json JSONB)
RETURNS TEXT[] AS $$
DECLARE
    node_keys TEXT[];
BEGIN
    IF branching_json IS NULL OR NOT (branching_json ? 'nodes') THEN
        RETURN ARRAY[]::TEXT[];
    END IF;

    SELECT array_agg(key)
    INTO node_keys
    FROM jsonb_object_keys(branching_json->'nodes') AS key;

    RETURN node_keys;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

COMMIT;

--changeset backend:quest-branching-sample-data dbms:postgresql
--comment: Sample branching quest data for testing

-- Insert sample branching quest for testing
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, status, level_min, level_max,
    rewards, objectives, branching_logic, created_at, updated_at
) VALUES (
    'quest-branching-demo-001',
    'quest-branching-demo',
    'Демонстрация ветвления квестов',
    'Квест с альтернативными путями выполнения - демонстрация системы ветвления',
    'active',
    1, 50,
    '{"xp": 1000, "items": ["sword_common", "shield_common"]}'::jsonb,
    '["Выбрать путь воина или мага"]'::jsonb,
    '{
        "version": "1.0",
        "entry_point": "choice_1",
        "nodes": {
            "choice_1": {
                "type": "choice",
                "title": "Выберите класс",
                "description": "Ваше решение определит дальнейший путь развития",
                "options": [
                    {
                        "id": "warrior_path",
                        "title": "Путь Воина",
                        "description": "Сила и защита - путь настоящего бойца",
                        "requirements": {},
                        "rewards": {"xp": 500, "items": ["sword_iron"]},
                        "next_node": "warrior_quest"
                    },
                    {
                        "id": "mage_path",
                        "title": "Путь Мага",
                        "description": "Магия и знания - путь мудреца",
                        "requirements": {},
                        "rewards": {"xp": 500, "items": ["staff_oak"]},
                        "next_node": "mage_quest"
                    }
                ]
            },
            "warrior_quest": {
                "type": "quest",
                "title": "Испытание Воина",
                "description": "Докажите свою силу в бою",
                "objectives": ["Убить 10 монстров", "Найти сокровище в пещере"],
                "rewards": {"xp": 2000, "items": ["armor_iron"]},
                "next_node": null
            },
            "mage_quest": {
                "type": "quest",
                "title": "Испытание Мага",
                "description": "Докажите свою мудрость в магии",
                "objectives": ["Собрать редкие реагенты", "Создать лечебное зелье"],
                "rewards": {"xp": 2000, "items": ["robe_mage"]},
                "next_node": null
            }
        }
    }'::jsonb,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (quest_id) DO NOTHING;

-- BACKEND NOTE: This migration implements the JSONB branching approach from the analysis
-- Issue: #140899116
-- Performance: GIN indexes ensure fast JSONB queries for MMORPG scale
-- Validation: Check constraint ensures data integrity
-- Scalability: JSONB allows flexible branching structures without schema changes
