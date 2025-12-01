-- Issue: #140890239
-- Resources Catalog System Database Schema Enhancement
-- Расширение существующей таблицы resources и создание дополнительных таблиц для каталога ресурсов:
-- - Расширение economy.resources (добавление полей для tier, rarity, stack_size, weight, sources, applications)
-- - resource_sources (источники добычи ресурсов)
-- - resource_applications (применение ресурсов)
-- - resource_mining_zones (зоны добычи ресурсов)
-- - resource_price_history (история цен ресурсов)

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'resource_rarity') THEN
        CREATE TYPE resource_rarity AS ENUM ('common', 'uncommon', 'rare', 'epic', 'legendary');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'resource_source_type') THEN
        CREATE TYPE resource_source_type AS ENUM ('loot', 'mining', 'processing', 'quest', 'vendor', 'dismantling', 'crafting', 'event');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'resource_application_type') THEN
        CREATE TYPE resource_application_type AS ENUM ('weapon_crafting', 'armor_crafting', 'cyberware_crafting', 'consumable_crafting', 'mod_crafting', 'upgrade', 'trade', 'quest');
    END IF;
END $$;

-- Расширение существующей таблицы resources
ALTER TABLE economy.resources 
    ADD COLUMN IF NOT EXISTS tier INTEGER CHECK (tier >= 1 AND tier <= 5),
    ADD COLUMN IF NOT EXISTS rarity resource_rarity,
    ADD COLUMN IF NOT EXISTS stack_size INTEGER DEFAULT 1 CHECK (stack_size > 0),
    ADD COLUMN IF NOT EXISTS weight DECIMAL(10,4) DEFAULT 0.0 CHECK (weight >= 0),
    ADD COLUMN IF NOT EXISTS min_price DECIMAL(20,2) CHECK (min_price >= 0),
    ADD COLUMN IF NOT EXISTS max_price DECIMAL(20,2) CHECK (max_price >= 0),
    ADD COLUMN IF NOT EXISTS vendor_price DECIMAL(20,2) CHECK (vendor_price >= 0),
    ADD COLUMN IF NOT EXISTS player_price DECIMAL(20,2) CHECK (player_price >= 0),
    ADD COLUMN IF NOT EXISTS sources JSONB DEFAULT '[]',
    ADD COLUMN IF NOT EXISTS applications JSONB DEFAULT '[]',
    ADD COLUMN IF NOT EXISTS is_tradeable BOOLEAN DEFAULT true,
    ADD COLUMN IF NOT EXISTS is_stackable BOOLEAN DEFAULT true,
    ADD COLUMN IF NOT EXISTS icon_path VARCHAR(255);

-- Индексы для расширенных полей resources
CREATE INDEX IF NOT EXISTS idx_resources_tier_rarity 
    ON economy.resources(tier, rarity) WHERE tier IS NOT NULL AND rarity IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resources_rarity 
    ON economy.resources(rarity) WHERE rarity IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resources_tier 
    ON economy.resources(tier) WHERE tier IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resources_is_tradeable 
    ON economy.resources(is_tradeable) WHERE is_tradeable = true;

-- Таблица источников добычи ресурсов
CREATE TABLE IF NOT EXISTS economy.resource_sources (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    resource_id UUID NOT NULL REFERENCES economy.resources(id) ON DELETE CASCADE,
    source_type resource_source_type NOT NULL,
    source_name VARCHAR(255) NOT NULL,
    source_id UUID, -- nullable, ID источника (NPC, локация, квест, etc.)
    drop_chance DECIMAL(5,2) CHECK (drop_chance >= 0.00 AND drop_chance <= 100.00),
    min_quantity INTEGER DEFAULT 1 CHECK (min_quantity > 0),
    max_quantity INTEGER DEFAULT 1 CHECK (max_quantity >= min_quantity),
    level_requirement INTEGER DEFAULT 0 CHECK (level_requirement >= 0),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для resource_sources
CREATE INDEX IF NOT EXISTS idx_resource_sources_resource_id 
    ON economy.resource_sources(resource_id);
CREATE INDEX IF NOT EXISTS idx_resource_sources_source_type 
    ON economy.resource_sources(source_type);
CREATE INDEX IF NOT EXISTS idx_resource_sources_source_id 
    ON economy.resource_sources(source_id) WHERE source_id IS NOT NULL;

-- Таблица применения ресурсов
CREATE TABLE IF NOT EXISTS economy.resource_applications (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    resource_id UUID NOT NULL REFERENCES economy.resources(id) ON DELETE CASCADE,
    application_type resource_application_type NOT NULL,
    target_item_type VARCHAR(50), -- nullable, тип предмета (weapon, armor, cyberware, etc.)
    target_item_tier INTEGER, -- nullable, тиер предмета (1-5)
    quantity_required INTEGER DEFAULT 1 CHECK (quantity_required > 0),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для resource_applications
CREATE INDEX IF NOT EXISTS idx_resource_applications_resource_id 
    ON economy.resource_applications(resource_id);
CREATE INDEX IF NOT EXISTS idx_resource_applications_application_type 
    ON economy.resource_applications(application_type);
CREATE INDEX IF NOT EXISTS idx_resource_applications_target_item 
    ON economy.resource_applications(target_item_type, target_item_tier) 
    WHERE target_item_type IS NOT NULL AND target_item_tier IS NOT NULL;

-- Таблица зон добычи ресурсов
CREATE TABLE IF NOT EXISTS economy.resource_mining_zones (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    resource_id UUID NOT NULL REFERENCES economy.resources(id) ON DELETE CASCADE,
    zone_name VARCHAR(255) NOT NULL,
    region_id UUID, -- FK regions (nullable)
    location_description TEXT,
    risk_level VARCHAR(20), -- low, medium, high, very_high
    spawn_rate DECIMAL(5,2) CHECK (spawn_rate >= 0.00 AND spawn_rate <= 100.00),
    respawn_time_minutes INTEGER DEFAULT 60 CHECK (respawn_time_minutes > 0),
    level_requirement INTEGER DEFAULT 0 CHECK (level_requirement >= 0),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для resource_mining_zones
CREATE INDEX IF NOT EXISTS idx_resource_mining_zones_resource_id 
    ON economy.resource_mining_zones(resource_id);
CREATE INDEX IF NOT EXISTS idx_resource_mining_zones_region_id 
    ON economy.resource_mining_zones(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resource_mining_zones_is_active 
    ON economy.resource_mining_zones(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_resource_mining_zones_risk_level 
    ON economy.resource_mining_zones(risk_level);

-- Таблица истории цен ресурсов
CREATE TABLE IF NOT EXISTS economy.resource_price_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    resource_id UUID NOT NULL REFERENCES economy.resources(id) ON DELETE CASCADE,
    price DECIMAL(20,2) NOT NULL CHECK (price >= 0),
    price_type VARCHAR(20) NOT NULL, -- 'base', 'current', 'vendor', 'player'
    region_id UUID, -- FK regions (nullable)
    event_id UUID, -- FK economic_events (nullable)
    supply_factor DECIMAL(5,2), -- nullable, фактор предложения (0.00-2.00)
    demand_factor DECIMAL(5,2), -- nullable, фактор спроса (0.00-2.00)
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для resource_price_history
CREATE INDEX IF NOT EXISTS idx_resource_price_history_resource_recorded 
    ON economy.resource_price_history(resource_id, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_resource_price_history_price_type 
    ON economy.resource_price_history(price_type);
CREATE INDEX IF NOT EXISTS idx_resource_price_history_region_id 
    ON economy.resource_price_history(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resource_price_history_event_id 
    ON economy.resource_price_history(event_id) WHERE event_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_resource_price_history_recorded_at 
    ON economy.resource_price_history(recorded_at DESC);

-- Комментарии к новым колонкам в resources
COMMENT ON COLUMN economy.resources.tier IS 'Тиер ресурса (1-5)';
COMMENT ON COLUMN economy.resources.rarity IS 'Редкость ресурса: common, uncommon, rare, epic, legendary';
COMMENT ON COLUMN economy.resources.stack_size IS 'Максимальный размер стека';
COMMENT ON COLUMN economy.resources.weight IS 'Вес единицы ресурса';
COMMENT ON COLUMN economy.resources.min_price IS 'Минимальная цена ресурса';
COMMENT ON COLUMN economy.resources.max_price IS 'Максимальная цена ресурса';
COMMENT ON COLUMN economy.resources.vendor_price IS 'Цена у продавца';
COMMENT ON COLUMN economy.resources.player_price IS 'Цена при торговле между игроками';
COMMENT ON COLUMN economy.resources.sources IS 'Источники добычи в JSONB (массив)';
COMMENT ON COLUMN economy.resources.applications IS 'Применение ресурса в JSONB (массив)';
COMMENT ON COLUMN economy.resources.is_tradeable IS 'Можно ли торговать ресурсом';
COMMENT ON COLUMN economy.resources.is_stackable IS 'Можно ли складывать ресурсы в стек';
COMMENT ON COLUMN economy.resources.icon_path IS 'Путь к иконке ресурса';

-- Комментарии к таблицам
COMMENT ON TABLE economy.resource_sources IS 'Источники добычи ресурсов (лут, добыча, переработка, квесты, продавцы, разборка, крафт, события)';
COMMENT ON TABLE economy.resource_applications IS 'Применение ресурсов (крафт оружия, брони, кибердеков, расходников, модов, улучшения, торговля, квесты)';
COMMENT ON TABLE economy.resource_mining_zones IS 'Зоны добычи ресурсов (Watson, Corpo Plaza, Badlands, Pacifica и др.)';
COMMENT ON TABLE economy.resource_price_history IS 'История цен ресурсов для динамического ценообразования';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.resource_sources.source_type IS 'Тип источника: loot, mining, processing, quest, vendor, dismantling, crafting, event';
COMMENT ON COLUMN economy.resource_sources.drop_chance IS 'Вероятность выпадения в процентах (0.00-100.00)';
COMMENT ON COLUMN economy.resource_sources.min_quantity IS 'Минимальное количество при выпадении';
COMMENT ON COLUMN economy.resource_sources.max_quantity IS 'Максимальное количество при выпадении';
COMMENT ON COLUMN economy.resource_applications.application_type IS 'Тип применения: weapon_crafting, armor_crafting, cyberware_crafting, consumable_crafting, mod_crafting, upgrade, trade, quest';
COMMENT ON COLUMN economy.resource_applications.quantity_required IS 'Количество ресурса, необходимое для применения';
COMMENT ON COLUMN economy.resource_mining_zones.risk_level IS 'Уровень риска зоны: low, medium, high, very_high';
COMMENT ON COLUMN economy.resource_mining_zones.spawn_rate IS 'Частота появления ресурса в зоне (0.00-100.00)';
COMMENT ON COLUMN economy.resource_mining_zones.respawn_time_minutes IS 'Время возрождения ресурса в минутах';
COMMENT ON COLUMN economy.resource_price_history.price_type IS 'Тип цены: base, current, vendor, player';
COMMENT ON COLUMN economy.resource_price_history.supply_factor IS 'Фактор предложения (0.00-2.00, влияет на цену)';
COMMENT ON COLUMN economy.resource_price_history.demand_factor IS 'Фактор спроса (0.00-2.00, влияет на цену)';

