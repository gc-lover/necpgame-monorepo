-- Issue: #140875781
-- Combat Session System Database Schema
-- Создание таблиц для системы боевых сессий:
-- - combat_sessions (боевые сессии)
-- - combat_participants (участники боевых сессий)
-- - combat_logs (логи действий в бою)
-- - combat_rewards (награды за боевые сессии)

-- Создание схемы mvp_core, если её нет (уже создана в V1_0, но для безопасности)
CREATE SCHEMA IF NOT EXISTS mvp_core;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_session_type') THEN
        CREATE TYPE combat_session_type AS ENUM ('pve', 'pvp', 'raid', 'arena');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_session_status') THEN
        CREATE TYPE combat_session_status AS ENUM ('created', 'active', 'paused', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_participant_type') THEN
        CREATE TYPE combat_participant_type AS ENUM ('player', 'npc', 'enemy');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_participant_status') THEN
        CREATE TYPE combat_participant_status AS ENUM ('alive', 'defeated', 'escaped');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_action_type') THEN
        CREATE TYPE combat_action_type AS ENUM ('attack', 'ability', 'defend', 'item', 'move');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'combat_reward_type') THEN
        CREATE TYPE combat_reward_type AS ENUM ('experience', 'loot', 'currency', 'quest_progress');
    END IF;
END $$;

-- Таблица боевых сессий
CREATE TABLE IF NOT EXISTS mvp_core.combat_sessions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  turn_order JSONB DEFAULT '[]'::jsonb,
  settings JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  started_at TIMESTAMP,
  ended_at TIMESTAMP,
  current_turn INTEGER DEFAULT 0 CHECK (current_turn >= 0),
  session_type combat_session_type NOT NULL,
  status combat_session_status NOT NULL DEFAULT 'created',
  CONSTRAINT chk_combat_sessions_dates CHECK (
        (started_at IS NULL AND ended_at IS NULL) OR
        (started_at IS NOT NULL AND (ended_at IS NULL OR ended_at >= started_at))
    )
);

-- Индексы для combat_sessions
CREATE INDEX IF NOT EXISTS idx_combat_sessions_status ON mvp_core.combat_sessions(status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_combat_sessions_type_status ON mvp_core.combat_sessions(session_type, status);
CREATE INDEX IF NOT EXISTS idx_combat_sessions_started_at ON mvp_core.combat_sessions(started_at DESC) WHERE started_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_sessions_active ON mvp_core.combat_sessions(status, created_at DESC) WHERE status IN ('created', 'active', 'paused');

-- Таблица участников боевых сессий
CREATE TABLE IF NOT EXISTS mvp_core.combat_participants (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  session_id UUID NOT NULL,
  participant_id UUID NOT NULL,
  position JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  team INTEGER DEFAULT 0 CHECK (team >= 0),
  initiative INTEGER DEFAULT 0 CHECK (initiative >= 0),
  health INTEGER NOT NULL DEFAULT 100 CHECK (health >= 0),
  max_health INTEGER NOT NULL DEFAULT 100 CHECK (max_health > 0),
  participant_type combat_participant_type NOT NULL,
  status combat_participant_status NOT NULL DEFAULT 'alive',
  CONSTRAINT fk_combat_participants_session FOREIGN KEY (session_id) REFERENCES mvp_core.combat_sessions(id) ON DELETE CASCADE,
  CONSTRAINT chk_combat_participants_health CHECK (health <= max_health)
);

-- Индексы для combat_participants
CREATE INDEX IF NOT EXISTS idx_combat_participants_session_id ON mvp_core.combat_participants(session_id, status);
CREATE INDEX IF NOT EXISTS idx_combat_participants_participant ON mvp_core.combat_participants(participant_id, participant_type);
CREATE INDEX IF NOT EXISTS idx_combat_participants_team ON mvp_core.combat_participants(session_id, team);
CREATE INDEX IF NOT EXISTS idx_combat_participants_initiative ON mvp_core.combat_participants(session_id, initiative DESC);
CREATE INDEX IF NOT EXISTS idx_combat_participants_status ON mvp_core.combat_participants(status) WHERE status != 'alive';

-- Таблица логов действий в бою
CREATE TABLE IF NOT EXISTS mvp_core.combat_logs (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  session_id UUID NOT NULL,
  actor_id UUID NOT NULL,
  target_id UUID,
  damage_type VARCHAR(50),
  effects_applied JSONB DEFAULT '[]'::jsonb,
  result JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  turn_number INTEGER NOT NULL DEFAULT 0 CHECK (turn_number >= 0),
  damage_dealt INTEGER DEFAULT 0 CHECK (damage_dealt >= 0),
  action_type combat_action_type NOT NULL,
  CONSTRAINT fk_combat_logs_session FOREIGN KEY (session_id) REFERENCES mvp_core.combat_sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_combat_logs_actor FOREIGN KEY (actor_id) REFERENCES mvp_core.combat_participants(id) ON DELETE CASCADE,
  CONSTRAINT fk_combat_logs_target FOREIGN KEY (target_id) REFERENCES mvp_core.combat_participants(id) ON DELETE SET NULL
);

-- Индексы для combat_logs
CREATE INDEX IF NOT EXISTS idx_combat_logs_session_id ON mvp_core.combat_logs(session_id, turn_number);
CREATE INDEX IF NOT EXISTS idx_combat_logs_actor_id ON mvp_core.combat_logs(actor_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_combat_logs_target_id ON mvp_core.combat_logs(target_id, created_at DESC) WHERE target_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_logs_action_type ON mvp_core.combat_logs(action_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_combat_logs_turn_number ON mvp_core.combat_logs(session_id, turn_number, created_at);

-- Таблица наград за боевые сессии
CREATE TABLE IF NOT EXISTS mvp_core.combat_rewards (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  session_id UUID NOT NULL,
  participant_id UUID NOT NULL,
  reward_data JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  distributed BOOLEAN NOT NULL DEFAULT false,
  reward_type combat_reward_type NOT NULL,
  CONSTRAINT fk_combat_rewards_session FOREIGN KEY (session_id) REFERENCES mvp_core.combat_sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_combat_rewards_participant FOREIGN KEY (participant_id) REFERENCES mvp_core.combat_participants(id) ON DELETE CASCADE
);

-- Индексы для combat_rewards
CREATE INDEX IF NOT EXISTS idx_combat_rewards_session_id ON mvp_core.combat_rewards(session_id, distributed);
CREATE INDEX IF NOT EXISTS idx_combat_rewards_participant_id ON mvp_core.combat_rewards(participant_id);
CREATE INDEX IF NOT EXISTS idx_combat_rewards_distributed ON mvp_core.combat_rewards(distributed, created_at DESC) WHERE distributed = false;
CREATE INDEX IF NOT EXISTS idx_combat_rewards_type ON mvp_core.combat_rewards(reward_type, distributed) WHERE distributed = false;

-- Комментарии к таблицам
COMMENT ON TABLE mvp_core.combat_sessions IS 'Боевые сессии (PvE, PvP, Raid, Arena)';
COMMENT ON TABLE mvp_core.combat_participants IS 'Участники боевых сессий (игроки, NPC, враги)';
COMMENT ON TABLE mvp_core.combat_logs IS 'Логи действий в бою';
COMMENT ON TABLE mvp_core.combat_rewards IS 'Награды за боевые сессии';

-- Комментарии к колонкам
COMMENT ON COLUMN mvp_core.combat_sessions.session_type IS 'Тип сессии: pve, pvp, raid, arena';
COMMENT ON COLUMN mvp_core.combat_sessions.status IS 'Статус сессии: created, active, paused, completed, cancelled';
COMMENT ON COLUMN mvp_core.combat_sessions.current_turn IS 'Текущий номер хода';
COMMENT ON COLUMN mvp_core.combat_sessions.turn_order IS 'Порядок ходов (JSONB массив participant_id)';
COMMENT ON COLUMN mvp_core.combat_sessions.settings IS 'Настройки боя (JSONB)';
COMMENT ON COLUMN mvp_core.combat_participants.participant_type IS 'Тип участника: player, npc, enemy';
COMMENT ON COLUMN mvp_core.combat_participants.participant_id IS 'ID участника (character_id или npc_id)';
COMMENT ON COLUMN mvp_core.combat_participants.team IS 'Номер команды';
COMMENT ON COLUMN mvp_core.combat_participants.initiative IS 'Инициатива (для определения порядка ходов)';
COMMENT ON COLUMN mvp_core.combat_participants.position IS 'Позиция участника (JSONB координаты)';
COMMENT ON COLUMN mvp_core.combat_logs.action_type IS 'Тип действия: attack, ability, defend, item, move';
COMMENT ON COLUMN mvp_core.combat_logs.damage_dealt IS 'Нанесенный урон';
COMMENT ON COLUMN mvp_core.combat_logs.effects_applied IS 'Примененные эффекты (JSONB)';
COMMENT ON COLUMN mvp_core.combat_logs.result IS 'Результат действия (JSONB)';
COMMENT ON COLUMN mvp_core.combat_rewards.reward_type IS 'Тип награды: experience, loot, currency, quest_progress';
COMMENT ON COLUMN mvp_core.combat_rewards.reward_data IS 'Данные награды (JSONB)';
COMMENT ON COLUMN mvp_core.combat_rewards.distributed IS 'Распределена ли награда';

