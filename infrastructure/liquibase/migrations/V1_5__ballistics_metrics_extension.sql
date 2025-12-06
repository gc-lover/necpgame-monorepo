-- Issue: #41, #1357
-- Ballistics Metrics Extension
-- Создание таблиц для системы баллистики и метрик стрельбы:
-- - ballistics_profiles (профили баллистики оружия)
-- - shooting_telemetry (телеметрия стрельбы)

-- Создание схемы gameplay, если её нет
CREATE SCHEMA IF NOT EXISTS gameplay;

-- Таблица профилей баллистики оружия
CREATE TABLE IF NOT EXISTS gameplay.ballistics_profiles (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  weapon_id UUID NOT NULL,
  optimal_range INTEGER NOT NULL DEFAULT 50 CHECK (optimal_range > 0),
  max_range INTEGER NOT NULL DEFAULT 200 CHECK (max_range > optimal_range),
  damage_falloff JSONB NOT NULL DEFAULT '{}',
  deviation_cone JSONB NOT NULL DEFAULT '{}',
  ricochet_enabled BOOLEAN NOT NULL DEFAULT false,
  curved_shot_enabled BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_ballistics_profiles_range CHECK (max_range > optimal_range)
);

-- Индексы для ballistics_profiles
CREATE INDEX IF NOT EXISTS idx_ballistics_profiles_weapon_id ON gameplay.ballistics_profiles(weapon_id);
CREATE INDEX IF NOT EXISTS idx_ballistics_profiles_range ON gameplay.ballistics_profiles(optimal_range, max_range);
CREATE INDEX IF NOT EXISTS idx_ballistics_profiles_ricochet ON gameplay.ballistics_profiles(ricochet_enabled) WHERE ricochet_enabled = true;
CREATE INDEX IF NOT EXISTS idx_ballistics_profiles_curved_shot ON gameplay.ballistics_profiles(curved_shot_enabled) WHERE curved_shot_enabled = true;

-- Таблица телеметрии стрельбы
CREATE TABLE IF NOT EXISTS gameplay.shooting_telemetry (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  weapon_id UUID NOT NULL,
  shots_fired INTEGER NOT NULL DEFAULT 0 CHECK (shots_fired >= 0),
  hits INTEGER NOT NULL DEFAULT 0 CHECK (hits >= 0),
  headshots INTEGER NOT NULL DEFAULT 0 CHECK (headshots >= 0),
  accuracy DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (accuracy >= 0 AND accuracy <= 100),
  avg_ttk DECIMAL(5,2) DEFAULT 0.0 CHECK (avg_ttk >= 0),
  recoil_control DECIMAL(5,2) DEFAULT 0.0 CHECK (recoil_control >= 0 AND recoil_control <= 100),
  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_shooting_telemetry_hits CHECK (hits <= shots_fired),
  CONSTRAINT chk_shooting_telemetry_headshots CHECK (headshots <= hits)
);

-- Индексы для shooting_telemetry
CREATE INDEX IF NOT EXISTS idx_shooting_telemetry_character_id ON gameplay.shooting_telemetry(character_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_shooting_telemetry_weapon_id ON gameplay.shooting_telemetry(weapon_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_shooting_telemetry_timestamp ON gameplay.shooting_telemetry(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_shooting_telemetry_accuracy ON gameplay.shooting_telemetry(accuracy DESC) WHERE accuracy > 50;
CREATE INDEX IF NOT EXISTS idx_shooting_telemetry_character_weapon ON gameplay.shooting_telemetry(character_id, weapon_id, timestamp DESC);

COMMENT ON TABLE gameplay.ballistics_profiles IS 'Профили баллистики оружия: оптимальная и максимальная дальность, падение урона, конус отклонения, поддержка рикошетов и закрученных выстрелов';
COMMENT ON TABLE gameplay.shooting_telemetry IS 'Телеметрия стрельбы: статистика выстрелов, попаданий, точности, TTK и контроля отдачи';

COMMIT;
