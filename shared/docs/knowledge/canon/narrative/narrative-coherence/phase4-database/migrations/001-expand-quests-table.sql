-- Migration: 001-expand-quests-table.sql
-- Version: 1.0.0
-- Date: 2025-11-07 00:22
-- Author: AI Manager
-- Description: Расширение базовой таблицы quests для поддержки ветвления

-- Dependencies: базовая таблица quests должна существовать

-- =====================================================
-- PART 1: ALTER EXISTING TABLE
-- =====================================================

BEGIN;

-- Добавляем новые колонки в quests
ALTER TABLE quests 
ADD COLUMN IF NOT EXISTS category VARCHAR(50),
ADD COLUMN IF NOT EXISTS difficulty VARCHAR(20),
ADD COLUMN IF NOT EXISTS min_level INTEGER NOT NULL DEFAULT 1,
ADD COLUMN IF NOT EXISTS max_level INTEGER,
ADD COLUMN IF NOT EXISTS required_quests JSONB,
ADD COLUMN IF NOT EXISTS required_flags JSONB,
ADD COLUMN IF NOT EXISTS required_reputation JSONB,
ADD COLUMN IF NOT EXISTS required_class VARCHAR(50),
ADD COLUMN IF NOT EXISTS required_origin VARCHAR(50),
ADD COLUMN IF NOT EXISTS has_branches BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS dialogue_tree_root INTEGER,
ADD COLUMN IF NOT EXISTS objectives JSONB NOT NULL DEFAULT '[]'::jsonb,
ADD COLUMN IF NOT EXISTS reward_items JSONB,
ADD COLUMN IF NOT EXISTS reward_reputation JSONB,
ADD COLUMN IF NOT EXISTS era VARCHAR(20) NOT NULL DEFAULT '2090-2093',
ADD COLUMN IF NOT EXISTS region VARCHAR(100),
ADD COLUMN IF NOT EXISTS estimated_duration INTEGER,
ADD COLUMN IF NOT EXISTS is_repeatable BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS cooldown_hours INTEGER,
ADD COLUMN IF NOT EXISTS max_concurrent_players INTEGER,
ADD COLUMN IF NOT EXISTS tags JSONB,
ADD COLUMN IF NOT EXISTS related_quests JSONB,
ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE,
ADD COLUMN IF NOT EXISTS version INTEGER DEFAULT 1,
ADD COLUMN IF NOT EXISTS created_by VARCHAR(100);

-- Обновляем тип type для новых значений
ALTER TABLE quests ALTER COLUMN type TYPE VARCHAR(50);

-- Удаляем старые колонки если они были VARCHAR и заменяем на JSONB
-- (только если они существовали и были строками)
-- reward_items был VARCHAR(1000), теперь JSONB
-- Это уже добавлено выше как JSONB

-- Создаем индексы для производительности
CREATE INDEX IF NOT EXISTS idx_quests_type ON quests(type);
CREATE INDEX IF NOT EXISTS idx_quests_level ON quests(min_level, max_level);
CREATE INDEX IF NOT EXISTS idx_quests_era ON quests(era);
CREATE INDEX IF NOT EXISTS idx_quests_region ON quests(region);
CREATE INDEX IF NOT EXISTS idx_quests_tags ON quests USING GIN(tags);
CREATE INDEX IF NOT EXISTS idx_quests_active ON quests(is_active) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_quests_class ON quests(required_class) WHERE required_class IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_quests_repeatable ON quests(is_repeatable) WHERE is_repeatable = TRUE;

-- Добавляем constraint для giver_npc
-- ALTER TABLE quests ADD CONSTRAINT fk_giver_npc 
-- FOREIGN KEY (giver_npc_id) REFERENCES npcs(id);
-- NOTE: Раскомментировать когда таблица npcs будет готова

COMMIT;

-- =====================================================
-- PART 2: DATA MIGRATION (если нужно)
-- =====================================================

BEGIN;

-- Миграция старых данных reward_items (VARCHAR → JSONB)
-- Если старая колонка была VARCHAR, конвертируем
-- UPDATE quests SET reward_items_new = reward_items::jsonb WHERE reward_items IS NOT NULL;

-- Устанавливаем defaults для существующих записей
UPDATE quests SET 
    min_level = COALESCE(level, 1),
    era = COALESCE(era, '2090-2093'),
    objectives = COALESCE(objectives, '[]'::jsonb),
    tags = COALESCE(tags, '[]'::jsonb),
    is_active = COALESCE(is_active, TRUE),
    version = COALESCE(version, 1)
WHERE min_level IS NULL OR era IS NULL;

COMMIT;

-- =====================================================
-- VERIFICATION
-- =====================================================

-- Проверка структуры таблицы
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'quests'
ORDER BY ordinal_position;

-- Проверка индексов
SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'quests';

-- =====================================================
-- ROLLBACK SCRIPT
-- =====================================================

-- Сохранить в файл: rollback/001-rollback-expand-quests.sql
/*
BEGIN;

-- Удалить новые индексы
DROP INDEX IF EXISTS idx_quests_type;
DROP INDEX IF EXISTS idx_quests_level;
DROP INDEX IF EXISTS idx_quests_era;
DROP INDEX IF EXISTS idx_quests_region;
DROP INDEX IF EXISTS idx_quests_tags;
DROP INDEX IF EXISTS idx_quests_active;
DROP INDEX IF EXISTS idx_quests_class;
DROP INDEX IF EXISTS idx_quests_repeatable;

-- Удалить новые колонки
ALTER TABLE quests 
DROP COLUMN IF EXISTS category,
DROP COLUMN IF EXISTS difficulty,
DROP COLUMN IF EXISTS min_level,
DROP COLUMN IF EXISTS max_level,
DROP COLUMN IF EXISTS required_quests,
DROP COLUMN IF EXISTS required_flags,
DROP COLUMN IF EXISTS required_reputation,
DROP COLUMN IF EXISTS required_class,
DROP COLUMN IF EXISTS required_origin,
DROP COLUMN IF EXISTS has_branches,
DROP COLUMN IF EXISTS dialogue_tree_root,
DROP COLUMN IF EXISTS objectives,
DROP COLUMN IF EXISTS reward_items_new,
DROP COLUMN IF EXISTS reward_reputation,
DROP COLUMN IF EXISTS era,
DROP COLUMN IF EXISTS region,
DROP COLUMN IF EXISTS estimated_duration,
DROP COLUMN IF EXISTS is_repeatable,
DROP COLUMN IF EXISTS cooldown_hours,
DROP COLUMN IF EXISTS max_concurrent_players,
DROP COLUMN IF EXISTS tags,
DROP COLUMN IF EXISTS related_quests,
DROP COLUMN IF EXISTS is_active,
DROP COLUMN IF EXISTS version,
DROP COLUMN IF EXISTS created_by;

COMMIT;
*/

