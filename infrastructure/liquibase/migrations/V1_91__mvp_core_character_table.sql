-- Issue: #389
-- MVP Core - Character Table
-- Создание базовой таблицы персонажей для системы управления персонажами

-- Создание схемы mvp_core, если её нет (уже создана в V1_0, но для безопасности)
CREATE SCHEMA IF NOT EXISTS mvp_core;

-- Таблица персонажей
CREATE TABLE IF NOT EXISTS mvp_core.character (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID,
  name VARCHAR(255) NOT NULL,
  class VARCHAR(50),
  origin VARCHAR(50),
  level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1),
  attributes JSONB DEFAULT '{}',
  skills JSONB DEFAULT '{}',
  stats JSONB DEFAULT '{}',
  current_zone VARCHAR(100),
  deleted BOOLEAN NOT NULL DEFAULT false,
  deleted_at TIMESTAMP,
  can_restore_until TIMESTAMP,
  last_played_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для character
CREATE INDEX IF NOT EXISTS idx_character_player_id ON mvp_core.character(player_id, deleted);
CREATE INDEX IF NOT EXISTS idx_character_name ON mvp_core.character(name);
CREATE INDEX IF NOT EXISTS idx_character_deleted ON mvp_core.character(deleted, can_restore_until) WHERE deleted = true;
CREATE INDEX IF NOT EXISTS idx_character_level ON mvp_core.character(level);
CREATE INDEX IF NOT EXISTS idx_character_class ON mvp_core.character(class) WHERE class IS NOT NULL;

-- Уникальный индекс для player_id + name (только для не удаленных)
CREATE UNIQUE INDEX IF NOT EXISTS uq_character_player_name ON mvp_core.character(player_id, name) WHERE deleted = false;

-- Комментарии к таблице
COMMENT ON TABLE mvp_core.character IS 'Персонажи игроков';
COMMENT ON COLUMN mvp_core.character.attributes IS 'Характеристики персонажа в формате JSONB';
COMMENT ON COLUMN mvp_core.character.skills IS 'Навыки персонажа в формате JSONB';
COMMENT ON COLUMN mvp_core.character.stats IS 'Статистика персонажа в формате JSONB';
COMMENT ON COLUMN mvp_core.character.deleted IS 'Флаг мягкого удаления';
COMMENT ON COLUMN mvp_core.character.can_restore_until IS 'До какого времени можно восстановить удаленного персонажа';

