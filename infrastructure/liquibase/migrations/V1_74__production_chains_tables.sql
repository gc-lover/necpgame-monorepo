-- Issue: #140890233
-- Production Chains System Database Schema
-- Создание таблиц для системы производственных цепочек:
-- - production_chains (производственные цепочки)
-- - production_orders (заказы на производство)
-- - production_stages (этапы производства)
-- - production_stations (производственные станции)
-- - production_licenses (лицензии на производство)
-- - player_production_licenses (лицензии игроков)
-- - production_accelerators (ускорители производства)
-- - production_legendary_quests (квестовые цепочки для легендарного крафта)

-- Создание схемы production, если её нет
CREATE SCHEMA IF NOT EXISTS production;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'production_order_status') THEN
        CREATE TYPE production_order_status AS ENUM ('pending', 'started', 'in_progress', 'completed', 'failed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'production_stage_type') THEN
        CREATE TYPE production_stage_type AS ENUM ('resource_extraction', 'processing', 'crafting', 'assembly');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'production_stage_status') THEN
        CREATE TYPE production_stage_status AS ENUM ('pending', 'in_progress', 'completed', 'failed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'production_station_type') THEN
        CREATE TYPE production_station_type AS ENUM ('smelter', 'processor', 'assembler', 'legendary_forge');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'production_accelerator_type') THEN
        CREATE TYPE production_accelerator_type AS ENUM ('efficiency_catalyst', 'quality_enhancer', 'material_optimizer');
    END IF;
END $$;

-- Таблица производственных цепочек
CREATE TABLE IF NOT EXISTS production.production_chains (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  required_licenses UUID[],
  description TEXT,
  name VARCHAR(255) NOT NULL,
  item_type VARCHAR(50) NOT NULL,
  stages JSONB NOT NULL DEFAULT '[]',
  -- FK production_licenses (массив UUID)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- массив этапов цепочки
    base_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  base_success_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (base_success_rate >= 0.00 AND base_success_rate <= 100.00),
  item_tier INTEGER NOT NULL CHECK (item_tier >= 1 AND item_tier <= 5),
  base_time_minutes INTEGER NOT NULL DEFAULT 0
);

-- Индексы для production_chains
CREATE INDEX IF NOT EXISTS idx_production_chains_item_tier_type 
    ON production.production_chains(item_tier, item_type);
CREATE INDEX IF NOT EXISTS idx_production_chains_name 
    ON production.production_chains(name);

-- Таблица заказов на производство
CREATE TABLE IF NOT EXISTS production.production_orders (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL,
  -- FK accounts
    chain_id UUID NOT NULL REFERENCES production.production_chains(id) ON DELETE RESTRICT,
  guild_id UUID,
  -- nullable
    station_id UUID REFERENCES production.production_stations(id) ON DELETE SET NULL,
  -- nullable
    started_at TIMESTAMP,
  -- nullable
    completed_at TIMESTAMP,
  -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  actual_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  -- nullable
    success_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (success_rate >= 0.00 AND success_rate <= 100.00),
  rush_cost_multiplier DECIMAL(3,2),
  bulk_efficiency DECIMAL(5,2),
  -- FK guilds (nullable)
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
  current_stage INTEGER NOT NULL DEFAULT 0,
  total_stages INTEGER NOT NULL DEFAULT 0,
  estimated_time_minutes INTEGER NOT NULL DEFAULT 0,
  actual_time_minutes INTEGER,
  is_rush BOOLEAN NOT NULL DEFAULT false,
  -- nullable
    is_bulk BOOLEAN NOT NULL DEFAULT false,
  status production_order_status NOT NULL DEFAULT 'pending'
);

-- Индексы для production_orders
CREATE INDEX IF NOT EXISTS idx_production_orders_player_status 
    ON production.production_orders(player_id, status);
CREATE INDEX IF NOT EXISTS idx_production_orders_chain_status 
    ON production.production_orders(chain_id, status);
CREATE INDEX IF NOT EXISTS idx_production_orders_guild_status 
    ON production.production_orders(guild_id, status) WHERE guild_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_production_orders_status_started 
    ON production.production_orders(status, started_at) WHERE started_at IS NOT NULL;

-- Таблица этапов производства
CREATE TABLE IF NOT EXISTS production.production_stages (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID NOT NULL REFERENCES production.production_orders(id) ON DELETE CASCADE,
  required_resources JSONB NOT NULL DEFAULT '{}',
  consumed_resources JSONB,
  -- nullable
    result JSONB,
  -- nullable
    started_at TIMESTAMP,
  -- nullable
    completed_at TIMESTAMP,
  -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  stage_number INTEGER NOT NULL,
  -- nullable
    success BOOLEAN,
  stage_type production_stage_type NOT NULL,
  status production_stage_status NOT NULL DEFAULT 'pending'
);

-- Индексы для production_stages
CREATE INDEX IF NOT EXISTS idx_production_stages_order_stage 
    ON production.production_stages(order_id, stage_number);
CREATE INDEX IF NOT EXISTS idx_production_stages_status 
    ON production.production_stages(status);

-- Таблица производственных станций игроков
CREATE TABLE IF NOT EXISTS production.production_stations (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL,
  location VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  efficiency_bonus DECIMAL(5,2) NOT NULL DEFAULT 0.00,
  level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1),
  capacity INTEGER NOT NULL DEFAULT 1 CHECK (capacity > 0),
  current_orders INTEGER NOT NULL DEFAULT 0 CHECK (current_orders >= 0),
  -- FK accounts
    station_type production_station_type NOT NULL
);

-- Индексы для production_stations
CREATE INDEX IF NOT EXISTS idx_production_stations_player_type 
    ON production.production_stations(player_id, station_type);
CREATE INDEX IF NOT EXISTS idx_production_stations_type_level 
    ON production.production_stations(station_type, level);

-- Таблица лицензий на производство
CREATE TABLE IF NOT EXISTS production.production_licenses (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  description TEXT,
  name VARCHAR(255) NOT NULL,
  item_type VARCHAR(50) NOT NULL,
  для временных лицензий
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  item_tier INTEGER NOT NULL CHECK (item_tier >= 1 AND item_tier <= 5),
  duration_days INTEGER,
  -- nullable
);

-- Индексы для production_licenses
CREATE INDEX IF NOT EXISTS idx_production_licenses_item_tier_type 
    ON production.production_licenses(item_tier, item_type);

-- Таблица лицензий игроков
CREATE TABLE IF NOT EXISTS production.player_production_licenses (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL, -- FK accounts
    license_id UUID NOT NULL REFERENCES production.production_licenses(id) ON DELETE RESTRICT,
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP, -- nullable
    active BOOLEAN NOT NULL DEFAULT true
);

-- Индексы для player_production_licenses
CREATE INDEX IF NOT EXISTS idx_player_production_licenses_player_active 
    ON production.player_production_licenses(player_id, active);
CREATE INDEX IF NOT EXISTS idx_player_production_licenses_license_active 
    ON production.player_production_licenses(license_id, active);
CREATE INDEX IF NOT EXISTS idx_player_production_licenses_expires_at 
    ON production.player_production_licenses(expires_at) WHERE expires_at IS NOT NULL;

-- Таблица ускорителей производства
CREATE TABLE IF NOT EXISTS production.production_accelerators (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID NOT NULL REFERENCES production.production_orders(id) ON DELETE CASCADE,
  effect JSONB NOT NULL DEFAULT '{}',
  -- описание эффекта
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  accelerator_type production_accelerator_type NOT NULL
);

-- Индексы для production_accelerators
CREATE INDEX IF NOT EXISTS idx_production_accelerators_order_id 
    ON production.production_accelerators(order_id);
CREATE INDEX IF NOT EXISTS idx_production_accelerators_expires_at 
    ON production.production_accelerators(expires_at);

-- Таблица квестовых цепочек для легендарного крафта
CREATE TABLE IF NOT EXISTS production.production_legendary_quests (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  item_id UUID,
  description TEXT,
  name VARCHAR(255) NOT NULL,
  -- FK items (nullable)
    quest_chain JSONB NOT NULL DEFAULT '[]',
  required_components JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  base_success_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (base_success_rate >= 0.00 AND base_success_rate <= 100.00),
  required_mastery INTEGER NOT NULL DEFAULT 0 CHECK (required_mastery >= 0),
  -- массив квестов
    required_blueprint BOOLEAN NOT NULL DEFAULT false,
  legendary_forge_required BOOLEAN NOT NULL DEFAULT false
);

-- Индексы для production_legendary_quests
CREATE INDEX IF NOT EXISTS idx_production_legendary_quests_item_id 
    ON production.production_legendary_quests(item_id) WHERE item_id IS NOT NULL;

-- Комментарии к таблицам
COMMENT ON TABLE production.production_chains IS 'Производственные цепочки для создания предметов';
COMMENT ON TABLE production.production_orders IS 'Заказы на производство от игроков';
COMMENT ON TABLE production.production_stages IS 'Этапы производства (добыча сырья, переработка, крафт, сборка)';
COMMENT ON TABLE production.production_stations IS 'Производственные станции игроков (плавильня, процессор, сборщик, легендарная кузня)';
COMMENT ON TABLE production.production_licenses IS 'Лицензии на производство предметов разных тиров и типов';
COMMENT ON TABLE production.player_production_licenses IS 'Лицензии игроков на производство';
COMMENT ON TABLE production.production_accelerators IS 'Ускорители производства (Efficiency Catalyst, Quality Enhancer, Material Optimizer)';
COMMENT ON TABLE production.production_legendary_quests IS 'Квестовые цепочки для легендарного крафта';

-- Комментарии к колонкам
COMMENT ON COLUMN production.production_chains.item_tier IS 'Тиер предмета (1-5)';
COMMENT ON COLUMN production.production_chains.item_type IS 'Тип предмета (weapon, cyberdeck, consumable, mod, etc.)';
COMMENT ON COLUMN production.production_chains.stages IS 'Массив этапов цепочки в JSONB';
COMMENT ON COLUMN production.production_chains.base_success_rate IS 'Базовая вероятность успеха в процентах (0.00-100.00)';
COMMENT ON COLUMN production.production_chains.required_licenses IS 'Массив UUID лицензий, необходимых для производства';
COMMENT ON COLUMN production.production_orders.current_stage IS 'Текущий этап производства (0 = не начато)';
COMMENT ON COLUMN production.production_orders.success_rate IS 'Вероятность успеха заказа в процентах (0.00-100.00)';
COMMENT ON COLUMN production.production_orders.is_rush IS 'Является ли заказ rush-заказом (ускоренным)';
COMMENT ON COLUMN production.production_orders.rush_cost_multiplier IS 'Множитель стоимости для rush-заказа';
COMMENT ON COLUMN production.production_orders.is_bulk IS 'Является ли заказ массовым производством';
COMMENT ON COLUMN production.production_orders.bulk_efficiency IS 'Эффективность массового производства (экономия времени/ресурсов)';
COMMENT ON COLUMN production.production_stages.stage_type IS 'Тип этапа: resource_extraction, processing, crafting, assembly';
COMMENT ON COLUMN production.production_stages.required_resources IS 'Требуемые ресурсы для этапа в JSONB';
COMMENT ON COLUMN production.production_stages.consumed_resources IS 'Потребленные ресурсы в JSONB';
COMMENT ON COLUMN production.production_stages.result IS 'Результат этапа в JSONB';
COMMENT ON COLUMN production.production_stations.station_type IS 'Тип станции: smelter, processor, assembler, legendary_forge';
COMMENT ON COLUMN production.production_stations.efficiency_bonus IS 'Бонус эффективности станции в процентах';
COMMENT ON COLUMN production.production_stations.current_orders IS 'Текущее количество заказов на станции';
COMMENT ON COLUMN production.production_licenses.duration_days IS 'Длительность лицензии в днях (NULL = постоянная)';
COMMENT ON COLUMN production.player_production_licenses.expires_at IS 'Время истечения лицензии (NULL = постоянная)';
COMMENT ON COLUMN production.production_accelerators.accelerator_type IS 'Тип ускорителя: efficiency_catalyst, quality_enhancer, material_optimizer';
COMMENT ON COLUMN production.production_accelerators.effect IS 'Описание эффекта ускорителя в JSONB';
COMMENT ON COLUMN production.production_legendary_quests.quest_chain IS 'Массив квестов для легендарного крафта в JSONB';
COMMENT ON COLUMN production.production_legendary_quests.required_mastery IS 'Требуемый уровень мастерства';
COMMENT ON COLUMN production.production_legendary_quests.legendary_forge_required IS 'Требуется ли легендарная кузня';

