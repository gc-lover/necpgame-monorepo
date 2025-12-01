-- Issue: #140875781
-- Combat Extended Mechanics Database Schema
-- Создание таблиц для расширенных механик боевой системы:
-- - Взлом в бою (hacking executions, networks)
-- - Активации имплантов в бою
-- - Продвинутая стрельба (advanced shooting stats)
-- - Лоадауты боевой системы
-- - Состояние всех механик в боевой сессии

-- Таблица выполнений взлома в бою
CREATE TABLE IF NOT EXISTS mvp_core.combat_hacking_executions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL,
    session_id UUID,
    hacking_type VARCHAR(100) NOT NULL,
    network_id UUID,
    target_id UUID,
    target_type VARCHAR(30) CHECK (target_type IN ('player', 'npc', 'device', 'infrastructure')),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    effects_applied JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для combat_hacking_executions
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_character_id ON mvp_core.combat_hacking_executions(character_id);
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_session_id ON mvp_core.combat_hacking_executions(session_id);
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_network_id ON mvp_core.combat_hacking_executions(network_id) WHERE network_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_target_id ON mvp_core.combat_hacking_executions(target_id) WHERE target_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_executed_at ON mvp_core.combat_hacking_executions(executed_at);

-- Таблица сетей для взлома
CREATE TABLE IF NOT EXISTS mvp_core.combat_hacking_networks (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    network_name VARCHAR(255) NOT NULL,
    network_type VARCHAR(30) NOT NULL CHECK (network_type IN ('enemy', 'device', 'infrastructure', 'combat_scenario')),
    access_level INTEGER NOT NULL DEFAULT 0,
    requirements JSONB,
    available_demons JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для combat_hacking_networks
CREATE INDEX IF NOT EXISTS idx_combat_hacking_networks_network_type ON mvp_core.combat_hacking_networks(network_type);
CREATE INDEX IF NOT EXISTS idx_combat_hacking_networks_access_level ON mvp_core.combat_hacking_networks(access_level);
CREATE INDEX IF NOT EXISTS idx_combat_hacking_networks_name ON mvp_core.combat_hacking_networks(network_name);

-- Таблица активаций имплантов в бою
CREATE TABLE IF NOT EXISTS mvp_core.combat_implant_activations (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL,
    session_id UUID,
    implant_id UUID NOT NULL,
    activated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    effects_applied JSONB,
    energy_used INTEGER NOT NULL DEFAULT 0,
    humanity_cost INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для combat_implant_activations
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_character_id ON mvp_core.combat_implant_activations(character_id);
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_session_id ON mvp_core.combat_implant_activations(session_id);
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_implant_id ON mvp_core.combat_implant_activations(implant_id);
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_activated_at ON mvp_core.combat_implant_activations(activated_at);

-- Таблица статистики продвинутой стрельбы
CREATE TABLE IF NOT EXISTS mvp_core.combat_advanced_shooting_stats (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL,
    session_id UUID,
    weapon_id UUID NOT NULL,
    aim_accuracy DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (aim_accuracy >= 0 AND aim_accuracy <= 100),
    recoil_control DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (recoil_control >= 0 AND recoil_control <= 100),
    shots_fired INTEGER NOT NULL DEFAULT 0,
    hits_landed INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для combat_advanced_shooting_stats
CREATE INDEX IF NOT EXISTS idx_combat_advanced_shooting_stats_character_id ON mvp_core.combat_advanced_shooting_stats(character_id);
CREATE INDEX IF NOT EXISTS idx_combat_advanced_shooting_stats_session_id ON mvp_core.combat_advanced_shooting_stats(session_id);
CREATE INDEX IF NOT EXISTS idx_combat_advanced_shooting_stats_weapon_id ON mvp_core.combat_advanced_shooting_stats(weapon_id);
CREATE INDEX IF NOT EXISTS idx_combat_advanced_shooting_stats_character_session ON mvp_core.combat_advanced_shooting_stats(character_id, session_id);

-- Таблица лоадаутов боевой системы
CREATE TABLE IF NOT EXISTS mvp_core.combat_loadouts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    weapons JSONB NOT NULL,
    abilities JSONB NOT NULL,
    implants JSONB NOT NULL,
    equipment JSONB NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для combat_loadouts
CREATE INDEX IF NOT EXISTS idx_combat_loadouts_character_id ON mvp_core.combat_loadouts(character_id);
CREATE INDEX IF NOT EXISTS idx_combat_loadouts_is_active ON mvp_core.combat_loadouts(is_active);
CREATE INDEX IF NOT EXISTS idx_combat_loadouts_character_active ON mvp_core.combat_loadouts(character_id, is_active) WHERE is_active = true;

-- Таблица состояния всех механик в боевой сессии
CREATE TABLE IF NOT EXISTS mvp_core.combat_mechanics_state (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    session_id UUID NOT NULL,
    character_id UUID NOT NULL,
    loadout_id UUID,
    active_implants JSONB,
    active_hacking_networks JSONB,
    shooting_config JSONB,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(session_id, character_id)
);

-- Индексы для combat_mechanics_state
CREATE INDEX IF NOT EXISTS idx_combat_mechanics_state_session_id ON mvp_core.combat_mechanics_state(session_id);
CREATE INDEX IF NOT EXISTS idx_combat_mechanics_state_character_id ON mvp_core.combat_mechanics_state(character_id);
CREATE INDEX IF NOT EXISTS idx_combat_mechanics_state_loadout_id ON mvp_core.combat_mechanics_state(loadout_id) WHERE loadout_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_combat_mechanics_state_session_character ON mvp_core.combat_mechanics_state(session_id, character_id);

-- Композитные индексы для оптимизации частых запросов
CREATE INDEX IF NOT EXISTS idx_combat_hacking_executions_character_session ON mvp_core.combat_hacking_executions(character_id, session_id);
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_character_session ON mvp_core.combat_implant_activations(character_id, session_id);

-- Комментарии к таблицам
COMMENT ON TABLE mvp_core.combat_hacking_executions IS 'Выполнения взлома в бою (hacking combat integration)';
COMMENT ON TABLE mvp_core.combat_hacking_networks IS 'Сети для взлома в боевых сценариях';
COMMENT ON TABLE mvp_core.combat_implant_activations IS 'Активации имплантов в бою с эффектами и затратами';
COMMENT ON TABLE mvp_core.combat_advanced_shooting_stats IS 'Статистика продвинутой стрельбы (прицеливание, отдача, точность)';
COMMENT ON TABLE mvp_core.combat_loadouts IS 'Лоадауты боевой системы (оружие, способности, импланты, экипировка)';
COMMENT ON TABLE mvp_core.combat_mechanics_state IS 'Состояние всех механик в боевой сессии (оркестрация)';

-- Комментарии к колонкам
COMMENT ON COLUMN mvp_core.combat_hacking_executions.target_type IS 'Тип цели взлома: player, npc, device, infrastructure';
COMMENT ON COLUMN mvp_core.combat_hacking_executions.effects_applied IS 'JSONB эффекты взлома: {damage, debuff, control, etc.}';
COMMENT ON COLUMN mvp_core.combat_hacking_networks.network_type IS 'Тип сети: enemy, device, infrastructure, combat_scenario';
COMMENT ON COLUMN mvp_core.combat_hacking_networks.available_demons IS 'JSONB доступные демоны для взлома';
COMMENT ON COLUMN mvp_core.combat_implant_activations.effects_applied IS 'JSONB эффекты импланта: {buffs, debuffs, abilities, etc.}';
COMMENT ON COLUMN mvp_core.combat_implant_activations.humanity_cost IS 'Стоимость человечности за активацию';
COMMENT ON COLUMN mvp_core.combat_advanced_shooting_stats.aim_accuracy IS 'Точность прицеливания (0-100%)';
COMMENT ON COLUMN mvp_core.combat_advanced_shooting_stats.recoil_control IS 'Контроль отдачи (0-100%)';
COMMENT ON COLUMN mvp_core.combat_loadouts.weapons IS 'JSONB оружие: [{weapon_id, attachments, mods}, ...]';
COMMENT ON COLUMN mvp_core.combat_loadouts.abilities IS 'JSONB способности: [{ability_id, level, mods}, ...]';
COMMENT ON COLUMN mvp_core.combat_loadouts.implants IS 'JSONB импланты: [{implant_id, level, mods}, ...]';
COMMENT ON COLUMN mvp_core.combat_loadouts.equipment IS 'JSONB экипировка: [{item_id, slot, mods}, ...]';
COMMENT ON COLUMN mvp_core.combat_mechanics_state.active_implants IS 'JSONB активные импланты: [{implant_id, effects, energy}, ...]';
COMMENT ON COLUMN mvp_core.combat_mechanics_state.active_hacking_networks IS 'JSONB активные сети: [{network_id, access_level, demons}, ...]';
COMMENT ON COLUMN mvp_core.combat_mechanics_state.shooting_config IS 'JSONB конфигурация стрельбы: {aim_sensitivity, recoil_pattern, etc.}';


