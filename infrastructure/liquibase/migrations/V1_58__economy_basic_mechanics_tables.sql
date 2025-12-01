-- Issue: #140876080
-- Economy Basic Mechanics Database Schema
-- Создание таблиц для базовых экономических механик:
-- - Обзор экономики (economy_overview)
-- - Торговые гильдии (trading_guilds)
-- - Члены торговых гильдий (guild_members)
-- - Валюты (currencies)
-- - Ресурсы (resources)
-- - Влияние экономики на мир (world_economy_impact)
-- - Предметы в торговой сессии (trade_items)
-- Примечание: trade_sessions уже создана в V1_15

-- Создание схемы economy, если её нет (уже создана в V1_15, но для безопасности)
CREATE SCHEMA IF NOT EXISTS economy;

-- Таблица обзора экономической системы
CREATE TABLE IF NOT EXISTS economy.economy_overview (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    region_id UUID, -- Assuming regions table exists
    currency_id UUID REFERENCES economy.currencies(id) ON DELETE SET NULL,
    total_volume DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (total_volume >= 0),
    active_trades INTEGER NOT NULL DEFAULT 0 CHECK (active_trades >= 0),
    active_guilds INTEGER NOT NULL DEFAULT 0 CHECK (active_guilds >= 0),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(region_id, currency_id)
);

-- Индексы для economy_overview
CREATE INDEX IF NOT EXISTS idx_economy_overview_region_id ON economy.economy_overview(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economy_overview_currency_id ON economy.economy_overview(currency_id) WHERE currency_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economy_overview_updated_at ON economy.economy_overview(updated_at DESC);

-- Таблица торговых гильдий
CREATE TABLE IF NOT EXISTS economy.trading_guilds (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    leader_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    capital DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (capital >= 0),
    member_count INTEGER NOT NULL DEFAULT 0 CHECK (member_count >= 0),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для trading_guilds
CREATE INDEX IF NOT EXISTS idx_trading_guilds_leader_id ON economy.trading_guilds(leader_id);
CREATE INDEX IF NOT EXISTS idx_trading_guilds_name ON economy.trading_guilds(name);
CREATE INDEX IF NOT EXISTS idx_trading_guilds_capital ON economy.trading_guilds(capital DESC);

-- Таблица членов торговых гильдий
CREATE TABLE IF NOT EXISTS economy.guild_members (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES economy.trading_guilds(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL DEFAULT 'member' CHECK (role IN ('member', 'officer', 'leader')),
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    contribution DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (contribution >= 0),
    UNIQUE(guild_id, character_id)
);

-- Индексы для guild_members
CREATE INDEX IF NOT EXISTS idx_guild_members_guild_id ON economy.guild_members(guild_id, role);
CREATE INDEX IF NOT EXISTS idx_guild_members_character_id ON economy.guild_members(character_id);
CREATE INDEX IF NOT EXISTS idx_guild_members_role ON economy.guild_members(role);

-- Таблица валют
CREATE TABLE IF NOT EXISTS economy.currencies (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    region_id UUID, -- Assuming regions table exists
    exchange_rate DECIMAL(20,8) NOT NULL DEFAULT 1.0 CHECK (exchange_rate > 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для currencies
CREATE INDEX IF NOT EXISTS idx_currencies_code ON economy.currencies(code);
CREATE INDEX IF NOT EXISTS idx_currencies_region_id ON economy.currencies(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_currencies_is_active ON economy.currencies(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_currencies_exchange_rate ON economy.currencies(exchange_rate);

-- Таблица ресурсов
CREATE TABLE IF NOT EXISTS economy.resources (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    base_price DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (base_price >= 0),
    current_price DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (current_price >= 0),
    description TEXT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для resources
CREATE INDEX IF NOT EXISTS idx_resources_category ON economy.resources(category, name);
CREATE INDEX IF NOT EXISTS idx_resources_name ON economy.resources(name);
CREATE INDEX IF NOT EXISTS idx_resources_current_price ON economy.resources(current_price);

-- Таблица влияния экономики на мир
CREATE TABLE IF NOT EXISTS economy.world_economy_impact (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    region_id UUID, -- Assuming regions table exists
    faction_id UUID, -- Assuming factions table exists
    economic_power DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (economic_power >= 0),
    trade_volume DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (trade_volume >= 0),
    influence_level INTEGER NOT NULL DEFAULT 0 CHECK (influence_level >= 0 AND influence_level <= 100),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(region_id, faction_id)
);

-- Индексы для world_economy_impact
CREATE INDEX IF NOT EXISTS idx_world_economy_impact_region_id ON economy.world_economy_impact(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_world_economy_impact_faction_id ON economy.world_economy_impact(faction_id) WHERE faction_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_world_economy_impact_influence_level ON economy.world_economy_impact(influence_level DESC);
CREATE INDEX IF NOT EXISTS idx_world_economy_impact_economic_power ON economy.world_economy_impact(economic_power DESC);

-- Таблица предметов в торговой сессии
CREATE TABLE IF NOT EXISTS economy.trade_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    session_id UUID NOT NULL REFERENCES economy.trade_sessions(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    item_id UUID NOT NULL, -- References items or character_items, depending on design
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    slot_index INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для trade_items
CREATE INDEX IF NOT EXISTS idx_trade_items_session_id ON economy.trade_items(session_id, character_id);
CREATE INDEX IF NOT EXISTS idx_trade_items_character_id ON economy.trade_items(character_id);
CREATE INDEX IF NOT EXISTS idx_trade_items_item_id ON economy.trade_items(item_id);

-- Комментарии к таблицам
COMMENT ON TABLE economy.economy_overview IS 'Обзор экономической системы по регионам и валютам';
COMMENT ON TABLE economy.trading_guilds IS 'Торговые гильдии игроков';
COMMENT ON TABLE economy.guild_members IS 'Члены торговых гильдий';
COMMENT ON TABLE economy.currencies IS 'Валюты экономической системы';
COMMENT ON TABLE economy.resources IS 'Ресурсы экономической системы';
COMMENT ON TABLE economy.world_economy_impact IS 'Влияние экономики на игровой мир (регионы, фракции)';
COMMENT ON TABLE economy.trade_items IS 'Предметы в торговой сессии';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.economy_overview.total_volume IS 'Общий объем торговли';
COMMENT ON COLUMN economy.economy_overview.active_trades IS 'Количество активных торговых сессий';
COMMENT ON COLUMN economy.economy_overview.active_guilds IS 'Количество активных торговых гильдий';
COMMENT ON COLUMN economy.trading_guilds.capital IS 'Капитал торговой гильдии';
COMMENT ON COLUMN economy.trading_guilds.member_count IS 'Количество членов гильдии';
COMMENT ON COLUMN economy.guild_members.role IS 'Роль в гильдии: member, officer, leader';
COMMENT ON COLUMN economy.guild_members.contribution IS 'Вклад члена в гильдию';
COMMENT ON COLUMN economy.currencies.exchange_rate IS 'Курс обмена валюты';
COMMENT ON COLUMN economy.resources.base_price IS 'Базовая цена ресурса';
COMMENT ON COLUMN economy.resources.current_price IS 'Текущая цена ресурса';
COMMENT ON COLUMN economy.world_economy_impact.economic_power IS 'Экономическая мощь фракции в регионе';
COMMENT ON COLUMN economy.world_economy_impact.trade_volume IS 'Объем торговли фракции в регионе';
COMMENT ON COLUMN economy.world_economy_impact.influence_level IS 'Уровень влияния фракции (0-100)';


