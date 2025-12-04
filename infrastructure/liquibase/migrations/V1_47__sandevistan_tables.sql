-- Issue: #140875766
-- Sandevistan Temporal Overdrive Database Schema
-- Создание таблиц для системы Sandevistan: активации, батчи действий, Temporal Marks, перегрев, контрплей, Perception Drag

-- Таблица активаций Sandevistan
CREATE TABLE IF NOT EXISTS sandevistan_activations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  session_id UUID,
  phase VARCHAR(20) NOT NULL CHECK (phase IN ('preparation', 'active', 'recovery')),
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  active_phase_started_at TIMESTAMP,
  recovery_phase_started_at TIMESTAMP,
  ended_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  action_budget_remaining INTEGER NOT NULL DEFAULT 0,
  action_budget_max INTEGER NOT NULL DEFAULT 3,
  heat_stacks INTEGER NOT NULL DEFAULT 0 CHECK (heat_stacks >= 0 AND heat_stacks <= 4),
  is_active BOOLEAN NOT NULL DEFAULT true
);

-- Индексы для sandevistan_activations
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_character_id ON sandevistan_activations(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_session_id ON sandevistan_activations(session_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_is_active ON sandevistan_activations(is_active);
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_phase ON sandevistan_activations(phase);
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_started_at ON sandevistan_activations(started_at);

-- Таблица батчей действий в MicroTick Window
CREATE TABLE IF NOT EXISTS sandevistan_action_batches (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  activation_id UUID NOT NULL REFERENCES sandevistan_activations(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  actions JSONB NOT NULL,
  processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  tick_number BIGINT NOT NULL,
  actions_count INTEGER NOT NULL CHECK (actions_count >= 1 AND actions_count <= 3)
);

-- Индексы для sandevistan_action_batches
CREATE INDEX IF NOT EXISTS idx_sandevistan_action_batches_activation_id ON sandevistan_action_batches(activation_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_action_batches_character_id ON sandevistan_action_batches(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_action_batches_tick_number ON sandevistan_action_batches(tick_number);
CREATE INDEX IF NOT EXISTS idx_sandevistan_action_batches_processed_at ON sandevistan_action_batches(processed_at);

-- Таблица Temporal Marks целей
CREATE TABLE IF NOT EXISTS sandevistan_temporal_marks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  activation_id UUID NOT NULL REFERENCES sandevistan_activations(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  target_id UUID NOT NULL,
  target_type VARCHAR(20) NOT NULL CHECK (target_type IN ('player', 'npc', 'enemy')),
  effect_applied JSONB,
  marked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  applied_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  damage_dealt INTEGER
);

-- Индексы для sandevistan_temporal_marks
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_activation_id ON sandevistan_temporal_marks(activation_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_character_id ON sandevistan_temporal_marks(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_target_id ON sandevistan_temporal_marks(target_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_target_type ON sandevistan_temporal_marks(target_type);
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_applied_at ON sandevistan_temporal_marks(applied_at) WHERE applied_at IS NOT NULL;

-- Таблица состояния перегрева
CREATE TABLE IF NOT EXISTS sandevistan_heat_state (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  overstress_triggered_at TIMESTAMP,
  cooldown_until TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  current_stacks INTEGER NOT NULL DEFAULT 0 CHECK (current_stacks >= 0 AND current_stacks <= 4),
  max_stacks INTEGER NOT NULL DEFAULT 4,
  overstress_triggered BOOLEAN NOT NULL DEFAULT false,
  UNIQUE(character_id)
);

-- Индексы для sandevistan_heat_state
CREATE INDEX IF NOT EXISTS idx_sandevistan_heat_state_character_id ON sandevistan_heat_state(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_heat_state_current_stacks ON sandevistan_heat_state(current_stacks);
CREATE INDEX IF NOT EXISTS idx_sandevistan_heat_state_overstress_triggered ON sandevistan_heat_state(overstress_triggered);
CREATE INDEX IF NOT EXISTS idx_sandevistan_heat_state_cooldown_until ON sandevistan_heat_state(cooldown_until) WHERE cooldown_until IS NOT NULL;

-- Таблица логов контрплея
CREATE TABLE IF NOT EXISTS sandevistan_counterplay_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  activation_id UUID NOT NULL REFERENCES sandevistan_activations(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  applied_by UUID NOT NULL,
  counterplay_type VARCHAR(30) NOT NULL CHECK (counterplay_type IN ('emp', 'chrono-jammer', 'hacking', 'crowd-control')),
  effect_applied JSONB,
  applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для sandevistan_counterplay_logs
CREATE INDEX IF NOT EXISTS idx_sandevistan_counterplay_logs_activation_id ON sandevistan_counterplay_logs(activation_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_counterplay_logs_character_id ON sandevistan_counterplay_logs(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_counterplay_logs_counterplay_type ON sandevistan_counterplay_logs(counterplay_type);
CREATE INDEX IF NOT EXISTS idx_sandevistan_counterplay_logs_applied_by ON sandevistan_counterplay_logs(applied_by);
CREATE INDEX IF NOT EXISTS idx_sandevistan_counterplay_logs_applied_at ON sandevistan_counterplay_logs(applied_at);

-- Таблица состояния Perception Drag для противников
CREATE TABLE IF NOT EXISTS sandevistan_perception_drag_state (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  active_activation_id UUID NOT NULL REFERENCES sandevistan_activations(id) ON DELETE CASCADE,
  applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reaction_debuff_percent DECIMAL(5,2) NOT NULL DEFAULT 15.00 CHECK (reaction_debuff_percent >= 0 AND reaction_debuff_percent <= 100),
  drag_delay_ms INTEGER NOT NULL CHECK (drag_delay_ms >= 0 AND drag_delay_ms <= 120),
  is_active BOOLEAN NOT NULL DEFAULT true
);

-- Индексы для sandevistan_perception_drag_state
CREATE INDEX IF NOT EXISTS idx_sandevistan_perception_drag_state_character_id ON sandevistan_perception_drag_state(character_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_perception_drag_state_active_activation_id ON sandevistan_perception_drag_state(active_activation_id);
CREATE INDEX IF NOT EXISTS idx_sandevistan_perception_drag_state_is_active ON sandevistan_perception_drag_state(is_active);
CREATE INDEX IF NOT EXISTS idx_sandevistan_perception_drag_state_expires_at ON sandevistan_perception_drag_state(expires_at);

-- Композитные индексы для оптимизации частых запросов
CREATE INDEX IF NOT EXISTS idx_sandevistan_activations_character_active ON sandevistan_activations(character_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_sandevistan_temporal_marks_activation_applied ON sandevistan_temporal_marks(activation_id, applied_at) WHERE applied_at IS NULL;

-- Комментарии к таблицам
COMMENT ON TABLE sandevistan_activations IS 'Активации Sandevistan Temporal Overdrive с управлением фазами (подготовка, активная, рекуперация)';
COMMENT ON TABLE sandevistan_action_batches IS 'Батчи действий в MicroTick Window (до 3 действий за тик)';
COMMENT ON TABLE sandevistan_temporal_marks IS 'Temporal Marks целей для delayed burst эффектов (до 3 целей)';
COMMENT ON TABLE sandevistan_heat_state IS 'Состояние перегрева Sandevistan (heat stacks 0-4, overstress)';
COMMENT ON TABLE sandevistan_counterplay_logs IS 'Логи контрплея (EMP, Chrono-Jammer, hacking, crowd-control)';
COMMENT ON TABLE sandevistan_perception_drag_state IS 'Состояние Perception Drag для противников (клиентский debuff восприятия)';

-- Комментарии к колонкам
COMMENT ON COLUMN sandevistan_activations.phase IS 'Фаза активации: preparation (300ms), active (4s), recovery (6s)';
COMMENT ON COLUMN sandevistan_activations.action_budget_remaining IS 'Оставшийся Action Priority Budget (максимум 3 действия за тик)';
COMMENT ON COLUMN sandevistan_activations.heat_stacks IS 'Текущий уровень перегрева (0-4, при 4 - overstress)';
COMMENT ON COLUMN sandevistan_action_batches.actions IS 'JSONB массив действий: [{type, target_id, position, timestamp}, ...]';
COMMENT ON COLUMN sandevistan_temporal_marks.effect_applied IS 'JSONB эффект: {damage, neuroshock_duration, aim_sway}';
COMMENT ON COLUMN sandevistan_heat_state.overstress_triggered IS 'Флаг срабатывания overstress (потеря 30% HP, неконтролируемый рывок)';
COMMENT ON COLUMN sandevistan_perception_drag_state.drag_delay_ms IS 'Клиентская задержка отображения анимаций (до 120 мс)';
COMMENT ON COLUMN sandevistan_perception_drag_state.reaction_debuff_percent IS 'Серверный debuff реакции (+15% к глобальному кулдауну)';


