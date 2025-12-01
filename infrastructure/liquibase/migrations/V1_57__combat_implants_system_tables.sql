-- Issue: #140876072
-- Combat Implants System Database Schema
-- Создание таблиц для системы боевых имплантов:
-- - Каталог имплантов (implants_catalog)
-- - Установленные импланты персонажей (character_implants)
-- - История приобретений (implant_acquisitions)
-- - Состояние лимитов (implant_limits_state)
-- - Состояние киберпсихоза (cyberpsychosis_state)
-- - Синергии имплантов (implant_synergies)
-- Примечание: combat_implant_activations уже создана в V1_49

-- Создание схемы implant, если её нет
CREATE SCHEMA IF NOT EXISTS implant;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'implant_type') THEN
        CREATE TYPE implant_type AS ENUM ('combat', 'movement', 'os', 'visual');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'implant_rarity') THEN
        CREATE TYPE implant_rarity AS ENUM ('common', 'uncommon', 'rare', 'epic', 'legendary');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'implant_acquisition_type') THEN
        CREATE TYPE implant_acquisition_type AS ENUM ('purchase', 'loot', 'quest', 'crafting');
    END IF;
END $$;

-- Таблица каталога имплантов
CREATE TABLE IF NOT EXISTS implant.implants_catalog (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type implant_type NOT NULL,
    category VARCHAR(100),
    rarity implant_rarity NOT NULL DEFAULT 'common',
    effects JSONB NOT NULL DEFAULT '{}'::jsonb,
    energy_cost INTEGER NOT NULL DEFAULT 0 CHECK (energy_cost >= 0),
    humanity_cost INTEGER NOT NULL DEFAULT 0 CHECK (humanity_cost >= 0),
    slot_type VARCHAR(100) NOT NULL,
    compatibility JSONB DEFAULT '{}'::jsonb,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для implants_catalog
CREATE INDEX IF NOT EXISTS idx_implants_catalog_type ON implant.implants_catalog(type, category);
CREATE INDEX IF NOT EXISTS idx_implants_catalog_rarity ON implant.implants_catalog(rarity);
CREATE INDEX IF NOT EXISTS idx_implants_catalog_slot_type ON implant.implants_catalog(slot_type);
CREATE INDEX IF NOT EXISTS idx_implants_catalog_energy_cost ON implant.implants_catalog(energy_cost);
CREATE INDEX IF NOT EXISTS idx_implants_catalog_humanity_cost ON implant.implants_catalog(humanity_cost);

-- Таблица установленных имплантов персонажей
CREATE TABLE IF NOT EXISTS implant.character_implants (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    implant_id UUID NOT NULL REFERENCES implant.implants_catalog(id) ON DELETE CASCADE,
    installed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    upgrade_level INTEGER NOT NULL DEFAULT 1 CHECK (upgrade_level >= 1),
    slot VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, slot)
);

-- Индексы для character_implants
CREATE INDEX IF NOT EXISTS idx_character_implants_character_id ON implant.character_implants(character_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_character_implants_implant_id ON implant.character_implants(implant_id);
CREATE INDEX IF NOT EXISTS idx_character_implants_slot ON implant.character_implants(character_id, slot);
CREATE INDEX IF NOT EXISTS idx_character_implants_upgrade_level ON implant.character_implants(upgrade_level);

-- Таблица истории приобретений имплантов
CREATE TABLE IF NOT EXISTS implant.implant_acquisitions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    implant_id UUID NOT NULL REFERENCES implant.implants_catalog(id) ON DELETE CASCADE,
    acquisition_type implant_acquisition_type NOT NULL,
    cost JSONB DEFAULT '{}'::jsonb,
    acquired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для implant_acquisitions
CREATE INDEX IF NOT EXISTS idx_implant_acquisitions_character_id ON implant.implant_acquisitions(character_id, acquired_at DESC);
CREATE INDEX IF NOT EXISTS idx_implant_acquisitions_implant_id ON implant.implant_acquisitions(implant_id);
CREATE INDEX IF NOT EXISTS idx_implant_acquisitions_acquisition_type ON implant.implant_acquisitions(acquisition_type);

-- Таблица состояния лимитов имплантов
CREATE TABLE IF NOT EXISTS implant.implant_limits_state (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    total_energy_used INTEGER NOT NULL DEFAULT 0 CHECK (total_energy_used >= 0),
    max_energy INTEGER NOT NULL DEFAULT 100 CHECK (max_energy > 0),
    total_humanity_lost INTEGER NOT NULL DEFAULT 0 CHECK (total_humanity_lost >= 0),
    max_humanity INTEGER NOT NULL DEFAULT 100 CHECK (max_humanity > 0),
    slots_used JSONB DEFAULT '{}'::jsonb,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id)
);

-- Индексы для implant_limits_state
CREATE INDEX IF NOT EXISTS idx_implant_limits_state_character_id ON implant.implant_limits_state(character_id);
CREATE INDEX IF NOT EXISTS idx_implant_limits_state_energy ON implant.implant_limits_state(total_energy_used, max_energy);
CREATE INDEX IF NOT EXISTS idx_implant_limits_state_humanity ON implant.implant_limits_state(total_humanity_lost, max_humanity);

-- Таблица состояния киберпсихоза
CREATE TABLE IF NOT EXISTS implant.cyberpsychosis_state (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    current_level INTEGER NOT NULL DEFAULT 0 CHECK (current_level >= 0 AND current_level <= 100),
    threshold_level INTEGER NOT NULL DEFAULT 50 CHECK (threshold_level >= 0 AND threshold_level <= 100),
    effects_active JSONB DEFAULT '{}'::jsonb,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id)
);

-- Индексы для cyberpsychosis_state
CREATE INDEX IF NOT EXISTS idx_cyberpsychosis_state_character_id ON implant.cyberpsychosis_state(character_id);
CREATE INDEX IF NOT EXISTS idx_cyberpsychosis_state_current_level ON implant.cyberpsychosis_state(current_level, threshold_level);
CREATE INDEX IF NOT EXISTS idx_cyberpsychosis_state_threshold ON implant.cyberpsychosis_state(character_id) WHERE current_level >= threshold_level;

-- Таблица синергий имплантов
CREATE TABLE IF NOT EXISTS implant.implant_synergies (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    synergy_id UUID NOT NULL,
    active_implants JSONB NOT NULL DEFAULT '[]'::jsonb,
    bonus_effects JSONB NOT NULL DEFAULT '{}'::jsonb,
    activated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для implant_synergies
CREATE INDEX IF NOT EXISTS idx_implant_synergies_character_id ON implant.implant_synergies(character_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_implant_synergies_synergy_id ON implant.implant_synergies(synergy_id);
CREATE INDEX IF NOT EXISTS idx_implant_synergies_activated_at ON implant.implant_synergies(activated_at DESC);

-- Обновление combat_implant_activations для связи с implants_catalog
ALTER TABLE mvp_core.combat_implant_activations
ADD COLUMN IF NOT EXISTS implant_catalog_id UUID REFERENCES implant.implants_catalog(id) ON DELETE SET NULL;

-- Индекс для обновленного поля
CREATE INDEX IF NOT EXISTS idx_combat_implant_activations_implant_catalog_id ON mvp_core.combat_implant_activations(implant_catalog_id) WHERE implant_catalog_id IS NOT NULL;

-- Комментарии к таблицам
COMMENT ON TABLE implant.implants_catalog IS 'Каталог имплантов (combat, movement, os, visual)';
COMMENT ON TABLE implant.character_implants IS 'Установленные импланты персонажей';
COMMENT ON TABLE implant.implant_acquisitions IS 'История приобретений имплантов (покупка, лут, квест, крафт)';
COMMENT ON TABLE implant.implant_limits_state IS 'Состояние лимитов имплантов (энергия, человечность, слоты)';
COMMENT ON TABLE implant.cyberpsychosis_state IS 'Состояние киберпсихоза персонажа от имплантов';
COMMENT ON TABLE implant.implant_synergies IS 'Активные синергии имплантов персонажа';

-- Комментарии к колонкам
COMMENT ON COLUMN implant.implants_catalog.type IS 'Тип импланта: combat, movement, os, visual';
COMMENT ON COLUMN implant.implants_catalog.rarity IS 'Редкость импланта: common, uncommon, rare, epic, legendary';
COMMENT ON COLUMN implant.implants_catalog.effects IS 'Эффекты импланта (JSONB)';
COMMENT ON COLUMN implant.implants_catalog.energy_cost IS 'Стоимость энергии импланта';
COMMENT ON COLUMN implant.implants_catalog.humanity_cost IS 'Стоимость человечности импланта';
COMMENT ON COLUMN implant.implants_catalog.slot_type IS 'Тип слота для импланта';
COMMENT ON COLUMN implant.implants_catalog.compatibility IS 'Совместимость с другими имплантами (JSONB)';
COMMENT ON COLUMN implant.character_implants.upgrade_level IS 'Уровень улучшения импланта (>= 1)';
COMMENT ON COLUMN implant.character_implants.slot IS 'Слот, в который установлен имплант';
COMMENT ON COLUMN implant.character_implants.is_active IS 'Флаг активности импланта';
COMMENT ON COLUMN implant.implant_acquisitions.acquisition_type IS 'Тип приобретения: purchase, loot, quest, crafting';
COMMENT ON COLUMN implant.implant_acquisitions.cost IS 'Стоимость приобретения (JSONB)';
COMMENT ON COLUMN implant.implant_limits_state.total_energy_used IS 'Общая использованная энергия';
COMMENT ON COLUMN implant.implant_limits_state.max_energy IS 'Максимальная энергия';
COMMENT ON COLUMN implant.implant_limits_state.total_humanity_lost IS 'Общая потерянная человечность';
COMMENT ON COLUMN implant.implant_limits_state.max_humanity IS 'Максимальная человечность';
COMMENT ON COLUMN implant.implant_limits_state.slots_used IS 'Использованные слоты (JSONB)';
COMMENT ON COLUMN implant.cyberpsychosis_state.current_level IS 'Текущий уровень киберпсихоза (0-100)';
COMMENT ON COLUMN implant.cyberpsychosis_state.threshold_level IS 'Пороговый уровень киберпсихоза (0-100)';
COMMENT ON COLUMN implant.cyberpsychosis_state.effects_active IS 'Активные эффекты киберпсихоза (JSONB)';
COMMENT ON COLUMN implant.implant_synergies.synergy_id IS 'ID синергии';
COMMENT ON COLUMN implant.implant_synergies.active_implants IS 'Активные импланты в синергии (JSONB)';
COMMENT ON COLUMN implant.implant_synergies.bonus_effects IS 'Бонусные эффекты синергии (JSONB)';
COMMENT ON COLUMN mvp_core.combat_implant_activations.implant_catalog_id IS 'ID импланта из implants_catalog';


