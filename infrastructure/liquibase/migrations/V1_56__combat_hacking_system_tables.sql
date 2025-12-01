-- Issue: #140876070
-- Combat Hacking System Database Schema
-- Создание таблиц для системы боевого взлома:
-- - Типы взлома (hacking_types)
-- - Сети для взлома (hacking_networks - дополнение к combat_hacking_networks)
-- - Состояние перегрева (hacking_overheat_state)
-- - Доступ к сетям (hacking_network_access)
-- Примечание: combat_hacking_executions и combat_hacking_networks уже созданы в V1_49

-- Создание схемы hacking, если её нет
CREATE SCHEMA IF NOT EXISTS hacking;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hacking_type_category') THEN
        CREATE TYPE hacking_type_category AS ENUM ('enemy', 'device', 'infrastructure', 'combat_scenario');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hacking_network_type') THEN
        CREATE TYPE hacking_network_type AS ENUM ('local', 'corporate', 'city', 'personal');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hacking_access_method') THEN
        CREATE TYPE hacking_access_method AS ENUM ('remote', 'physical', 'hybrid');
    END IF;
END $$;

-- Таблица типов взлома
CREATE TABLE IF NOT EXISTS hacking.hacking_types (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL UNIQUE,
    category hacking_type_category NOT NULL,
    class_requirement VARCHAR(50),
    skill_requirement JSONB,
    overheat_cost INTEGER NOT NULL DEFAULT 0 CHECK (overheat_cost >= 0),
    cooldown_duration INTEGER NOT NULL DEFAULT 0 CHECK (cooldown_duration >= 0),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для hacking_types
CREATE INDEX IF NOT EXISTS idx_hacking_types_category ON hacking.hacking_types(category);
CREATE INDEX IF NOT EXISTS idx_hacking_types_class_requirement ON hacking.hacking_types(class_requirement) WHERE class_requirement IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_hacking_types_overheat_cost ON hacking.hacking_types(overheat_cost);

-- Таблица сетей для взлома (детальная структура)
CREATE TABLE IF NOT EXISTS hacking.hacking_networks (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    network_name VARCHAR(255) NOT NULL UNIQUE,
    network_type hacking_network_type NOT NULL,
    security_level INTEGER NOT NULL DEFAULT 0 CHECK (security_level >= 0 AND security_level <= 100),
    access_method hacking_access_method NOT NULL DEFAULT 'remote',
    protection_levels JSONB,
    available_demons JSONB,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для hacking_networks
CREATE INDEX IF NOT EXISTS idx_hacking_networks_network_type ON hacking.hacking_networks(network_type);
CREATE INDEX IF NOT EXISTS idx_hacking_networks_security_level ON hacking.hacking_networks(security_level);
CREATE INDEX IF NOT EXISTS idx_hacking_networks_access_method ON hacking.hacking_networks(access_method);

-- Таблица состояния перегрева
CREATE TABLE IF NOT EXISTS hacking.hacking_overheat_state (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    session_id UUID REFERENCES mvp_core.combat_sessions(id) ON DELETE CASCADE,
    current_heat INTEGER NOT NULL DEFAULT 0 CHECK (current_heat >= 0),
    max_heat INTEGER NOT NULL DEFAULT 100 CHECK (max_heat > 0),
    is_overheated BOOLEAN NOT NULL DEFAULT false,
    cooling_applied INTEGER NOT NULL DEFAULT 0 CHECK (cooling_applied >= 0),
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, session_id)
);

-- Индексы для hacking_overheat_state
CREATE INDEX IF NOT EXISTS idx_hacking_overheat_state_character_id ON hacking.hacking_overheat_state(character_id, is_overheated);
CREATE INDEX IF NOT EXISTS idx_hacking_overheat_state_session_id ON hacking.hacking_overheat_state(session_id) WHERE session_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_hacking_overheat_state_overheated ON hacking.hacking_overheat_state(is_overheated, current_heat DESC) WHERE is_overheated = true;

-- Таблица доступа к сетям
CREATE TABLE IF NOT EXISTS hacking.hacking_network_access (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    network_id UUID NOT NULL REFERENCES hacking.hacking_networks(id) ON DELETE CASCADE,
    access_level INTEGER NOT NULL DEFAULT 0 CHECK (access_level >= 0),
    access_method hacking_access_method NOT NULL DEFAULT 'remote',
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, network_id)
);

-- Индексы для hacking_network_access
CREATE INDEX IF NOT EXISTS idx_hacking_network_access_character_id ON hacking.hacking_network_access(character_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_hacking_network_access_network_id ON hacking.hacking_network_access(network_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_hacking_network_access_expires_at ON hacking.hacking_network_access(expires_at) WHERE expires_at IS NOT NULL AND expires_at > NOW();

-- Обновление combat_hacking_executions для связи с hacking_types и hacking_networks
ALTER TABLE mvp_core.combat_hacking_executions
ADD COLUMN IF NOT EXISTS hacking_type_id UUID REFERENCES hacking.hacking_types(id) ON DELETE SET NULL,
ADD COLUMN IF NOT EXISTS hacking_network_id UUID REFERENCES hacking.hacking_networks(id) ON DELETE SET NULL,
ADD COLUMN IF NOT EXISTS overheat_generated INTEGER NOT NULL DEFAULT 0 CHECK (overheat_generated >= 0);

-- Индексы для обновленных полей
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_hacking_type_id ON mvp_core.combat_hacking_executions(hacking_type_id) WHERE hacking_type_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_hacking_network_id ON mvp_core.combat_hacking_executions(hacking_network_id) WHERE hacking_network_id IS NOT NULL;

-- Комментарии к таблицам
COMMENT ON TABLE hacking.hacking_types IS 'Типы взлома (враги, устройства, инфраструктура, боевой сценарий)';
COMMENT ON TABLE hacking.hacking_networks IS 'Сети для взлома (локальные, корпоративные, городские, персональные)';
COMMENT ON TABLE hacking.hacking_overheat_state IS 'Состояние перегрева системы взлома персонажа';
COMMENT ON TABLE hacking.hacking_network_access IS 'Доступ персонажей к сетям для взлома';

-- Комментарии к колонкам
COMMENT ON COLUMN hacking.hacking_types.category IS 'Категория типа взлома: enemy, device, infrastructure, combat_scenario';
COMMENT ON COLUMN hacking.hacking_types.class_requirement IS 'Требуемый класс для использования типа взлома';
COMMENT ON COLUMN hacking.hacking_types.skill_requirement IS 'Требования к навыкам (JSONB)';
COMMENT ON COLUMN hacking.hacking_types.overheat_cost IS 'Стоимость перегрева при использовании';
COMMENT ON COLUMN hacking.hacking_types.cooldown_duration IS 'Длительность кулдауна в секундах';
COMMENT ON COLUMN hacking.hacking_networks.network_type IS 'Тип сети: local, corporate, city, personal';
COMMENT ON COLUMN hacking.hacking_networks.security_level IS 'Уровень защиты сети (0-100)';
COMMENT ON COLUMN hacking.hacking_networks.access_method IS 'Метод доступа: remote, physical, hybrid';
COMMENT ON COLUMN hacking.hacking_networks.protection_levels IS 'Уровни защиты сети (JSONB)';
COMMENT ON COLUMN hacking.hacking_networks.available_demons IS 'Доступные демоны для взлома (JSONB)';
COMMENT ON COLUMN hacking.hacking_overheat_state.current_heat IS 'Текущий уровень нагрева';
COMMENT ON COLUMN hacking.hacking_overheat_state.max_heat IS 'Максимальный уровень нагрева';
COMMENT ON COLUMN hacking.hacking_overheat_state.is_overheated IS 'Флаг перегрева системы';
COMMENT ON COLUMN hacking.hacking_overheat_state.cooling_applied IS 'Количество примененного охлаждения';
COMMENT ON COLUMN hacking.hacking_network_access.access_level IS 'Уровень доступа к сети';
COMMENT ON COLUMN hacking.hacking_network_access.access_method IS 'Метод доступа: remote, physical, hybrid';
COMMENT ON COLUMN hacking.hacking_network_access.expires_at IS 'Время истечения доступа (nullable)';
COMMENT ON COLUMN mvp_core.combat_hacking_executions.hacking_type_id IS 'ID типа взлома из hacking_types';
COMMENT ON COLUMN mvp_core.combat_hacking_executions.hacking_network_id IS 'ID сети из hacking_networks';
COMMENT ON COLUMN mvp_core.combat_hacking_executions.overheat_generated IS 'Количество сгенерированного перегрева';


