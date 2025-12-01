-- Issue: #140876086
-- Economy Equipment System Database Schema
-- Создание таблиц для системы оборудования:
-- - Каталог оборудования (equipment_catalog)
-- - Временная линия культового оборудования (iconic_equipment_timeline)
-- - Разблокировки культового оборудования (iconic_unlocks)
-- - Матрица характеристик оборудования (equipment_matrix)
-- - Процедурно сгенерированное оборудование (generated_equipment)

-- Создание схемы economy, если её нет (уже создана в V1_15, но для безопасности)
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'equipment_category') THEN
        CREATE TYPE equipment_category AS ENUM ('weapon', 'armor', 'cyberware', 'consumable');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'equipment_rarity') THEN
        CREATE TYPE equipment_rarity AS ENUM ('common', 'uncommon', 'rare', 'epic', 'legendary', 'iconic');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'iconic_availability') THEN
        CREATE TYPE iconic_availability AS ENUM ('always', 'seasonal', 'event', 'quest');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'iconic_unlock_method') THEN
        CREATE TYPE iconic_unlock_method AS ENUM ('quest', 'event', 'purchase', 'drop');
    END IF;
END $$;

-- Таблица каталога оборудования
CREATE TABLE IF NOT EXISTS economy.equipment_catalog (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category equipment_category NOT NULL,
    brand VARCHAR(50),
    rarity equipment_rarity NOT NULL DEFAULT 'common',
    stats JSONB NOT NULL DEFAULT '{}'::jsonb,
    signature TEXT,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для equipment_catalog
CREATE INDEX IF NOT EXISTS idx_equipment_catalog_category ON economy.equipment_catalog(category, brand, rarity);
CREATE INDEX IF NOT EXISTS idx_equipment_catalog_brand ON economy.equipment_catalog(brand, category);
CREATE INDEX IF NOT EXISTS idx_equipment_catalog_rarity ON economy.equipment_catalog(rarity, category);
CREATE INDEX IF NOT EXISTS idx_equipment_catalog_name ON economy.equipment_catalog(name);

-- Таблица временной линии культового оборудования
CREATE TABLE IF NOT EXISTS economy.iconic_equipment_timeline (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    equipment_id UUID NOT NULL REFERENCES economy.equipment_catalog(id) ON DELETE CASCADE,
    era_start INTEGER NOT NULL CHECK (era_start >= 2000 AND era_start <= 2100),
    era_end INTEGER CHECK (era_end IS NULL OR (era_end >= 2000 AND era_end <= 2100 AND era_end >= era_start)),
    unlock_conditions JSONB NOT NULL DEFAULT '{}'::jsonb,
    availability iconic_availability NOT NULL DEFAULT 'always',
    unlock_date TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(equipment_id)
);

-- Индексы для iconic_equipment_timeline
CREATE INDEX IF NOT EXISTS idx_iconic_equipment_timeline_equipment_id ON economy.iconic_equipment_timeline(equipment_id);
CREATE INDEX IF NOT EXISTS idx_iconic_equipment_timeline_era ON economy.iconic_equipment_timeline(era_start, era_end);
CREATE INDEX IF NOT EXISTS idx_iconic_equipment_timeline_availability ON economy.iconic_equipment_timeline(availability, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_iconic_equipment_timeline_unlock_date ON economy.iconic_equipment_timeline(unlock_date) WHERE unlock_date IS NOT NULL;

-- Таблица разблокировок культового оборудования
CREATE TABLE IF NOT EXISTS economy.iconic_unlocks (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    iconic_id UUID NOT NULL REFERENCES economy.iconic_equipment_timeline(id) ON DELETE CASCADE,
    unlocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unlock_method iconic_unlock_method NOT NULL,
    unlock_data JSONB DEFAULT '{}'::jsonb,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, iconic_id)
);

-- Индексы для iconic_unlocks
CREATE INDEX IF NOT EXISTS idx_iconic_unlocks_character_id ON economy.iconic_unlocks(character_id, unlocked_at DESC);
CREATE INDEX IF NOT EXISTS idx_iconic_unlocks_iconic_id ON economy.iconic_unlocks(iconic_id);
CREATE INDEX IF NOT EXISTS idx_iconic_unlocks_unlock_method ON economy.iconic_unlocks(unlock_method);

-- Таблица матрицы характеристик оборудования
CREATE TABLE IF NOT EXISTS economy.equipment_matrix (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    category equipment_category NOT NULL,
    rarity equipment_rarity NOT NULL,
    stat_pools JSONB NOT NULL DEFAULT '{}'::jsonb,
    modifiers JSONB NOT NULL DEFAULT '{}'::jsonb,
    version INTEGER NOT NULL DEFAULT 1 CHECK (version >= 1),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(brand, category, rarity, version)
);

-- Индексы для equipment_matrix
CREATE INDEX IF NOT EXISTS idx_equipment_matrix_brand_category ON economy.equipment_matrix(brand, category, rarity);
CREATE INDEX IF NOT EXISTS idx_equipment_matrix_category_rarity ON economy.equipment_matrix(category, rarity);
CREATE INDEX IF NOT EXISTS idx_equipment_matrix_version ON economy.equipment_matrix(version DESC);

-- Таблица процедурно сгенерированного оборудования
CREATE TABLE IF NOT EXISTS economy.generated_equipment (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    seed BIGINT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    category equipment_category NOT NULL,
    rarity equipment_rarity NOT NULL,
    stats JSONB NOT NULL DEFAULT '{}'::jsonb,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    character_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    item_id UUID, -- References character_items, if assigned
    UNIQUE(seed, brand, category)
);

-- Индексы для generated_equipment
CREATE INDEX IF NOT EXISTS idx_generated_equipment_seed ON economy.generated_equipment(seed, brand, category);
CREATE INDEX IF NOT EXISTS idx_generated_equipment_brand_category ON economy.generated_equipment(brand, category, rarity);
CREATE INDEX IF NOT EXISTS idx_generated_equipment_character_id ON economy.generated_equipment(character_id) WHERE character_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_generated_equipment_generated_at ON economy.generated_equipment(generated_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE economy.equipment_catalog IS 'Каталог оборудования (ручной срез ключевых предметов)';
COMMENT ON TABLE economy.iconic_equipment_timeline IS 'Временная линия культового оборудования';
COMMENT ON TABLE economy.iconic_unlocks IS 'Разблокировки культового оборудования персонажами';
COMMENT ON TABLE economy.equipment_matrix IS 'Матрица характеристик для процедурной генерации';
COMMENT ON TABLE economy.generated_equipment IS 'Процедурно сгенерированное оборудование';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.equipment_catalog.category IS 'Категория оборудования: weapon, armor, cyberware, consumable';
COMMENT ON COLUMN economy.equipment_catalog.rarity IS 'Редкость: common, uncommon, rare, epic, legendary, iconic';
COMMENT ON COLUMN economy.equipment_catalog.stats IS 'Характеристики предмета (JSONB)';
COMMENT ON COLUMN economy.equipment_catalog.signature IS 'Подпись предмета (уникальная характеристика)';
COMMENT ON COLUMN economy.iconic_equipment_timeline.era_start IS 'Год начала эпохи (2000-2100)';
COMMENT ON COLUMN economy.iconic_equipment_timeline.era_end IS 'Год конца эпохи (nullable, >= era_start)';
COMMENT ON COLUMN economy.iconic_equipment_timeline.unlock_conditions IS 'Условия разблокировки (JSONB)';
COMMENT ON COLUMN economy.iconic_equipment_timeline.availability IS 'Доступность: always, seasonal, event, quest';
COMMENT ON COLUMN economy.iconic_equipment_timeline.unlock_date IS 'Дата разблокировки (nullable)';
COMMENT ON COLUMN economy.iconic_unlocks.unlock_method IS 'Метод разблокировки: quest, event, purchase, drop';
COMMENT ON COLUMN economy.iconic_unlocks.unlock_data IS 'Данные разблокировки (JSONB)';
COMMENT ON COLUMN economy.equipment_matrix.stat_pools IS 'Пулы характеристик для генерации (JSONB)';
COMMENT ON COLUMN economy.equipment_matrix.modifiers IS 'Модификаторы характеристик (JSONB)';
COMMENT ON COLUMN economy.equipment_matrix.version IS 'Версия матрицы (>= 1)';
COMMENT ON COLUMN economy.generated_equipment.seed IS 'Seed для процедурной генерации';
COMMENT ON COLUMN economy.generated_equipment.stats IS 'Сгенерированные характеристики (JSONB)';
COMMENT ON COLUMN economy.generated_equipment.character_id IS 'ID персонажа, получившего предмет (nullable)';
COMMENT ON COLUMN economy.generated_equipment.item_id IS 'ID предмета в инвентаре (nullable)';


