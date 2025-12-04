-- Issue: #140890882
-- Reputation System Database Schema
-- Создание таблиц для системы репутации:
-- - character_reputation (репутация персонажей с фракциями)
-- - reputation_history (история изменений репутации)
-- - reputation_recovery_tasks (задачи восстановления репутации)
-- - heat_history (история изменений Heat)
-- - faction_relations_matrix (матрица отношений между фракциями)
-- - reputation_effects_cache (кэш эффектов репутации)

-- Создание схемы social, если её нет (уже создана в V1_52, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'reputation_tier') THEN
        CREATE TYPE reputation_tier AS ENUM ('hated', 'hostile', 'unfriendly', 'neutral', 'friendly', 'honored', 'legendary');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'reputation_recovery_type') THEN
        CREATE TYPE reputation_recovery_type AS ENUM ('quest', 'bribe', 'service', 'time');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'reputation_recovery_status') THEN
        CREATE TYPE reputation_recovery_status AS ENUM ('active', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'faction_relation_type') THEN
        CREATE TYPE faction_relation_type AS ENUM ('allied', 'neutral', 'hostile');
    END IF;
END $$;

-- Таблица репутации персонажей с фракциями
CREATE TABLE IF NOT EXISTS social.character_reputation (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  -- FK characters
    faction_id UUID NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  discount_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (discount_percentage >= 0.00 AND discount_percentage <= 100.00),
  -- FK factions
    reputation_value INTEGER NOT NULL DEFAULT 0 CHECK (reputation_value >= -100 AND reputation_value <= 100),
  heat INTEGER NOT NULL DEFAULT 0 CHECK (heat >= 0),
  access_level INTEGER NOT NULL DEFAULT 0 CHECK (access_level >= 0),
  current_tier reputation_tier NOT NULL DEFAULT 'neutral',
  UNIQUE(character_id, faction_id)
);

-- Индексы для character_reputation
CREATE INDEX IF NOT EXISTS idx_character_reputation_character_id 
    ON social.character_reputation(character_id);
CREATE INDEX IF NOT EXISTS idx_character_reputation_faction_id 
    ON social.character_reputation(faction_id);
CREATE INDEX IF NOT EXISTS idx_character_reputation_tier 
    ON social.character_reputation(current_tier);
CREATE INDEX IF NOT EXISTS idx_character_reputation_value 
    ON social.character_reputation(reputation_value DESC);
CREATE INDEX IF NOT EXISTS idx_character_reputation_heat 
    ON social.character_reputation(heat DESC) WHERE heat > 0;

-- Таблица истории изменений репутации
CREATE TABLE IF NOT EXISTS social.reputation_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  -- FK characters
    faction_id UUID NOT NULL,
  change_reason VARCHAR(255),
  source_event VARCHAR(100),
  old_tier VARCHAR(50),
  new_tier VARCHAR(50),
  modifiers_applied JSONB DEFAULT '{}',
  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- FK factions
    change_amount INTEGER NOT NULL CHECK (change_amount >= -100 AND change_amount <= 100),
  old_value INTEGER NOT NULL CHECK (old_value >= -100 AND old_value <= 100),
  new_value INTEGER NOT NULL CHECK (new_value >= -100 AND new_value <= 100)
);

-- Индексы для reputation_history
CREATE INDEX IF NOT EXISTS idx_reputation_history_character_faction 
    ON social.reputation_history(character_id, faction_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_reputation_history_timestamp 
    ON social.reputation_history(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_reputation_history_source_event 
    ON social.reputation_history(source_event) WHERE source_event IS NOT NULL;

-- Таблица задач восстановления репутации
CREATE TABLE IF NOT EXISTS social.reputation_recovery_tasks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  -- FK characters
    faction_id UUID NOT NULL,
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- FK factions
    target_reputation INTEGER NOT NULL CHECK (target_reputation >= -100 AND target_reputation <= 100),
  current_progress INTEGER NOT NULL DEFAULT 0 CHECK (current_progress >= 0),
  recovery_type reputation_recovery_type NOT NULL,
  status reputation_recovery_status NOT NULL DEFAULT 'active'
);

-- Индексы для reputation_recovery_tasks
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_tasks_character_faction 
    ON social.reputation_recovery_tasks(character_id, faction_id, status);
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_tasks_status 
    ON social.reputation_recovery_tasks(status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_tasks_type 
    ON social.reputation_recovery_tasks(recovery_type);

-- Таблица истории изменений Heat
CREATE TABLE IF NOT EXISTS social.heat_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  change_reason VARCHAR(255),
  source_event VARCHAR(100),
  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- FK characters
    heat_change INTEGER NOT NULL CHECK (heat_change >= -1000 AND heat_change <= 1000),
  old_heat INTEGER NOT NULL DEFAULT 0 CHECK (old_heat >= 0),
  new_heat INTEGER NOT NULL DEFAULT 0 CHECK (new_heat >= 0)
);

-- Индексы для heat_history
CREATE INDEX IF NOT EXISTS idx_heat_history_character_id 
    ON social.heat_history(character_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_heat_history_timestamp 
    ON social.heat_history(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_heat_history_source_event 
    ON social.heat_history(source_event) WHERE source_event IS NOT NULL;

-- Таблица матрицы отношений между фракциями
CREATE TABLE IF NOT EXISTS social.faction_relations_matrix (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  faction_a_id UUID NOT NULL,
  -- FK factions
    faction_b_id UUID NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (modifier >= -100.00 AND modifier <= 100.00),
  -- FK factions
    relation_type faction_relation_type NOT NULL DEFAULT 'neutral',
  CONSTRAINT chk_faction_order CHECK (faction_a_id < faction_b_id),
  UNIQUE(faction_a_id, faction_b_id)
);

-- Индексы для faction_relations_matrix
CREATE INDEX IF NOT EXISTS idx_faction_relations_matrix_faction_a 
    ON social.faction_relations_matrix(faction_a_id);
CREATE INDEX IF NOT EXISTS idx_faction_relations_matrix_faction_b 
    ON social.faction_relations_matrix(faction_b_id);
CREATE INDEX IF NOT EXISTS idx_faction_relations_matrix_type 
    ON social.faction_relations_matrix(relation_type);

-- Таблица кэша эффектов репутации
CREATE TABLE IF NOT EXISTS social.reputation_effects_cache (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL, -- FK characters
    faction_id UUID NOT NULL, -- FK factions
    effects JSONB NOT NULL DEFAULT '{}', -- эффекты репутации (скидки, доступ, бонусы)
    cached_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    UNIQUE(character_id, faction_id)
);

-- Индексы для reputation_effects_cache
CREATE INDEX IF NOT EXISTS idx_reputation_effects_cache_character_faction 
    ON social.reputation_effects_cache(character_id, faction_id);
CREATE INDEX IF NOT EXISTS idx_reputation_effects_cache_expires_at 
    ON social.reputation_effects_cache(expires_at) WHERE expires_at < CURRENT_TIMESTAMP;

-- Комментарии к таблицам
COMMENT ON TABLE social.character_reputation IS 'Репутация персонажей с фракциями (7 уровней: hated, hostile, unfriendly, neutral, friendly, honored, legendary)';
COMMENT ON TABLE social.reputation_history IS 'История изменений репутации для аудита и анализа';
COMMENT ON TABLE social.reputation_recovery_tasks IS 'Задачи восстановления репутации (квесты, взятки, услуги, время)';
COMMENT ON TABLE social.heat_history IS 'История изменений Heat (внимание фракций)';
COMMENT ON TABLE social.faction_relations_matrix IS 'Матрица отношений между фракциями (союзники, нейтральные, враждебные)';
COMMENT ON TABLE social.reputation_effects_cache IS 'Кэш эффектов репутации (скидки, доступ, бонусы)';

-- Комментарии к колонкам
COMMENT ON COLUMN social.character_reputation.reputation_value IS 'Значение репутации (-100 до 100)';
COMMENT ON COLUMN social.character_reputation.current_tier IS 'Текущий уровень репутации: hated, hostile, unfriendly, neutral, friendly, honored, legendary';
COMMENT ON COLUMN social.character_reputation.heat IS 'Heat (внимание фракции, 0+)';
COMMENT ON COLUMN social.character_reputation.access_level IS 'Уровень доступа (0+)';
COMMENT ON COLUMN social.character_reputation.discount_percentage IS 'Процент скидки (0.00-100.00)';
COMMENT ON COLUMN social.reputation_history.change_amount IS 'Изменение репутации (-100 до 100)';
COMMENT ON COLUMN social.reputation_history.modifiers_applied IS 'Примененные модификаторы в JSONB';
COMMENT ON COLUMN social.reputation_history.old_tier IS 'Старый уровень репутации';
COMMENT ON COLUMN social.reputation_history.new_tier IS 'Новый уровень репутации';
COMMENT ON COLUMN social.reputation_recovery_tasks.target_reputation IS 'Целевое значение репутации (-100 до 100)';
COMMENT ON COLUMN social.reputation_recovery_tasks.current_progress IS 'Текущий прогресс восстановления (0+)';
COMMENT ON COLUMN social.reputation_recovery_tasks.recovery_type IS 'Тип восстановления: quest, bribe, service, time';
COMMENT ON COLUMN social.heat_history.heat_change IS 'Изменение Heat (-1000 до 1000)';
COMMENT ON COLUMN social.faction_relations_matrix.relation_type IS 'Тип отношения: allied, neutral, hostile';
COMMENT ON COLUMN social.faction_relations_matrix.modifier IS 'Модификатор отношения (-100.00 до 100.00)';
COMMENT ON COLUMN social.reputation_effects_cache.effects IS 'Эффекты репутации в JSONB (скидки, доступ, бонусы)';
COMMENT ON COLUMN social.reputation_effects_cache.expires_at IS 'Время истечения кэша';

