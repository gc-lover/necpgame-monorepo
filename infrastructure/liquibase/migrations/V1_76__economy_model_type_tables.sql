-- Issue: #140890244
-- Economy Model Type System Database Schema
-- Создание таблиц для типа экономической модели:
-- - economy_model_config (конфигурация экономической модели)
-- - market_types (типы рынков: глобальный, региональный, фракционный)
-- - market_access_rules (правила доступа к рынкам)
-- - pricing_model_config (конфигурация модели ценообразования)
-- - economy_governance_rules (правила управления экономикой)

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economy_model_type') THEN
        CREATE TYPE economy_model_type AS ENUM ('player_driven', 'npc_driven', 'hybrid');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'market_type') THEN
        CREATE TYPE market_type AS ENUM ('global', 'regional', 'faction');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'pricing_control_type') THEN
        CREATE TYPE pricing_control_type AS ENUM ('system_base', 'player_driven', 'hybrid');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'governance_entity_type') THEN
        CREATE TYPE governance_entity_type AS ENUM ('player', 'npc', 'faction', 'system');
    END IF;
END $$;

-- Таблица конфигурации экономической модели
CREATE TABLE IF NOT EXISTS economy.economy_model_config (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    model_type economy_model_type NOT NULL DEFAULT 'hybrid',
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    base_supply_npc_controlled BOOLEAN NOT NULL DEFAULT true,
    strategic_markets_player_controlled BOOLEAN NOT NULL DEFAULT true,
    regional_modifiers_faction_controlled BOOLEAN NOT NULL DEFAULT true,
    price_volatility_factor DECIMAL(5,2) NOT NULL DEFAULT 1.00 CHECK (price_volatility_factor >= 0.00 AND price_volatility_factor <= 10.00),
    demand_supply_impact DECIMAL(5,2) NOT NULL DEFAULT 0.50 CHECK (demand_supply_impact >= 0.00 AND demand_supply_impact <= 1.00),
    rare_items_player_only BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для economy_model_config
CREATE INDEX IF NOT EXISTS idx_economy_model_config_model_type 
    ON economy.economy_model_config(model_type);
CREATE INDEX IF NOT EXISTS idx_economy_model_config_is_active 
    ON economy.economy_model_config(is_active) WHERE is_active = true;

-- Таблица типов рынков
CREATE TABLE IF NOT EXISTS economy.market_types (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    market_type market_type NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    region_id UUID, -- FK regions (nullable, для региональных рынков)
    faction_id UUID, -- FK factions (nullable, для фракционных рынков)
    is_global BOOLEAN NOT NULL DEFAULT false,
    base_tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (base_tax_rate >= 0.00 AND base_tax_rate <= 100.00),
    player_tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (player_tax_rate >= 0.00 AND player_tax_rate <= 100.00),
    npc_tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (npc_tax_rate >= 0.00 AND npc_tax_rate <= 100.00),
    min_reputation_level INTEGER DEFAULT 0 CHECK (min_reputation_level >= 0),
    requires_alliance BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(market_type, region_id, faction_id)
);

-- Индексы для market_types
CREATE INDEX IF NOT EXISTS idx_market_types_market_type 
    ON economy.market_types(market_type);
CREATE INDEX IF NOT EXISTS idx_market_types_region_id 
    ON economy.market_types(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_market_types_faction_id 
    ON economy.market_types(faction_id) WHERE faction_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_market_types_is_active 
    ON economy.market_types(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_market_types_is_global 
    ON economy.market_types(is_global) WHERE is_global = true;

-- Таблица правил доступа к рынкам
CREATE TABLE IF NOT EXISTS economy.market_access_rules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    market_type_id UUID NOT NULL REFERENCES economy.market_types(id) ON DELETE CASCADE,
    access_type VARCHAR(50) NOT NULL, -- 'reputation', 'alliance', 'guild', 'level', 'quest'
    requirement_value INTEGER, -- nullable, значение требования (уровень репутации, уровень игрока, etc.)
    requirement_item_id UUID, -- nullable, FK items (для требований по предметам)
    requirement_quest_id UUID, -- nullable, FK quests (для требований по квестам)
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для market_access_rules
CREATE INDEX IF NOT EXISTS idx_market_access_rules_market_type_id 
    ON economy.market_access_rules(market_type_id);
CREATE INDEX IF NOT EXISTS idx_market_access_rules_access_type 
    ON economy.market_access_rules(access_type);
CREATE INDEX IF NOT EXISTS idx_market_access_rules_is_active 
    ON economy.market_access_rules(is_active) WHERE is_active = true;

-- Таблица конфигурации модели ценообразования
CREATE TABLE IF NOT EXISTS economy.pricing_model_config (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    market_type_id UUID REFERENCES economy.market_types(id) ON DELETE SET NULL, -- nullable, для глобальных настроек
    item_category VARCHAR(50), -- nullable, категория предметов
    item_tier INTEGER, -- nullable, тиер предметов (1-5)
    pricing_control_type pricing_control_type NOT NULL DEFAULT 'hybrid',
    base_price_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.00 CHECK (base_price_multiplier >= 0.00 AND base_price_multiplier <= 10.00),
    demand_impact_factor DECIMAL(5,2) NOT NULL DEFAULT 0.30 CHECK (demand_impact_factor >= 0.00 AND demand_impact_factor <= 1.00),
    supply_impact_factor DECIMAL(5,2) NOT NULL DEFAULT 0.30 CHECK (supply_impact_factor >= 0.00 AND supply_impact_factor <= 1.00),
    event_impact_factor DECIMAL(5,2) NOT NULL DEFAULT 0.20 CHECK (event_impact_factor >= 0.00 AND event_impact_factor <= 1.00),
    min_price_modifier DECIMAL(5,2) NOT NULL DEFAULT 0.50 CHECK (min_price_modifier >= 0.00 AND min_price_modifier <= 1.00),
    max_price_modifier DECIMAL(5,2) NOT NULL DEFAULT 2.00 CHECK (max_price_modifier >= 1.00 AND max_price_modifier <= 10.00),
    player_control_percentage DECIMAL(5,2) NOT NULL DEFAULT 50.00 CHECK (player_control_percentage >= 0.00 AND player_control_percentage <= 100.00),
    npc_control_percentage DECIMAL(5,2) NOT NULL DEFAULT 50.00 CHECK (npc_control_percentage >= 0.00 AND npc_control_percentage <= 100.00),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для pricing_model_config
CREATE INDEX IF NOT EXISTS idx_pricing_model_config_market_type_id 
    ON economy.pricing_model_config(market_type_id) WHERE market_type_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_pricing_model_config_item_category_tier 
    ON economy.pricing_model_config(item_category, item_tier) 
    WHERE item_category IS NOT NULL AND item_tier IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_pricing_model_config_pricing_control_type 
    ON economy.pricing_model_config(pricing_control_type);
CREATE INDEX IF NOT EXISTS idx_pricing_model_config_is_active 
    ON economy.pricing_model_config(is_active) WHERE is_active = true;

-- Таблица правил управления экономикой
CREATE TABLE IF NOT EXISTS economy.economy_governance_rules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    governance_entity_type governance_entity_type NOT NULL,
    entity_id UUID, -- nullable, ID сущности (player_id, faction_id, etc.)
    rule_type VARCHAR(50) NOT NULL, -- 'price_control', 'supply_control', 'tax_control', 'access_control'
    rule_scope VARCHAR(50) NOT NULL, -- 'global', 'regional', 'faction', 'market', 'item'
    scope_id UUID, -- nullable, ID области действия (region_id, market_type_id, item_id, etc.)
    rule_config JSONB NOT NULL DEFAULT '{}', -- конфигурация правила
    priority INTEGER NOT NULL DEFAULT 0 CHECK (priority >= 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    effective_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    effective_until TIMESTAMP, -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для economy_governance_rules
CREATE INDEX IF NOT EXISTS idx_economy_governance_rules_entity_type_id 
    ON economy.economy_governance_rules(governance_entity_type, entity_id) 
    WHERE entity_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economy_governance_rules_rule_type_scope 
    ON economy.economy_governance_rules(rule_type, rule_scope);
CREATE INDEX IF NOT EXISTS idx_economy_governance_rules_scope_id 
    ON economy.economy_governance_rules(scope_id) WHERE scope_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economy_governance_rules_is_active_effective 
    ON economy.economy_governance_rules(is_active, effective_from, effective_until) 
    WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_economy_governance_rules_priority 
    ON economy.economy_governance_rules(priority DESC);

-- Комментарии к таблицам
COMMENT ON TABLE economy.economy_model_config IS 'Конфигурация экономической модели (player-driven, NPC-driven, hybrid)';
COMMENT ON TABLE economy.market_types IS 'Типы рынков: глобальный, региональный, фракционный';
COMMENT ON TABLE economy.market_access_rules IS 'Правила доступа к рынкам (репутация, союзы, уровень, квесты)';
COMMENT ON TABLE economy.pricing_model_config IS 'Конфигурация модели ценообразования для разных рынков и категорий предметов';
COMMENT ON TABLE economy.economy_governance_rules IS 'Правила управления экономикой (контроль цен, предложения, налогов, доступа)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.economy_model_config.model_type IS 'Тип экономической модели: player_driven, npc_driven, hybrid';
COMMENT ON COLUMN economy.economy_model_config.base_supply_npc_controlled IS 'Базовое снабжение контролируется NPC';
COMMENT ON COLUMN economy.economy_model_config.strategic_markets_player_controlled IS 'Стратегические рынки контролируются игроками';
COMMENT ON COLUMN economy.economy_model_config.regional_modifiers_faction_controlled IS 'Региональные модификаторы контролируются фракциями';
COMMENT ON COLUMN economy.economy_model_config.price_volatility_factor IS 'Фактор волатильности цен (0.00-10.00)';
COMMENT ON COLUMN economy.economy_model_config.demand_supply_impact IS 'Влияние спроса и предложения на цены (0.00-1.00)';
COMMENT ON COLUMN economy.market_types.market_type IS 'Тип рынка: global, regional, faction';
COMMENT ON COLUMN economy.market_types.base_tax_rate IS 'Базовая ставка налога в процентах (0.00-100.00)';
COMMENT ON COLUMN economy.market_types.player_tax_rate IS 'Ставка налога для игроков в процентах (0.00-100.00)';
COMMENT ON COLUMN economy.market_types.npc_tax_rate IS 'Ставка налога для NPC в процентах (0.00-100.00)';
COMMENT ON COLUMN economy.market_types.min_reputation_level IS 'Минимальный уровень репутации для доступа';
COMMENT ON COLUMN economy.market_types.requires_alliance IS 'Требуется ли союз для доступа';
COMMENT ON COLUMN economy.market_access_rules.access_type IS 'Тип требования доступа: reputation, alliance, guild, level, quest';
COMMENT ON COLUMN economy.market_access_rules.requirement_value IS 'Значение требования (уровень репутации, уровень игрока, etc.)';
COMMENT ON COLUMN economy.pricing_model_config.pricing_control_type IS 'Тип контроля ценообразования: system_base, player_driven, hybrid';
COMMENT ON COLUMN economy.pricing_model_config.base_price_multiplier IS 'Множитель базовой цены (0.00-10.00)';
COMMENT ON COLUMN economy.pricing_model_config.demand_impact_factor IS 'Фактор влияния спроса на цену (0.00-1.00)';
COMMENT ON COLUMN economy.pricing_model_config.supply_impact_factor IS 'Фактор влияния предложения на цену (0.00-1.00)';
COMMENT ON COLUMN economy.pricing_model_config.event_impact_factor IS 'Фактор влияния событий на цену (0.00-1.00)';
COMMENT ON COLUMN economy.pricing_model_config.min_price_modifier IS 'Модификатор минимальной цены (0.00-1.00 от базовой)';
COMMENT ON COLUMN economy.pricing_model_config.max_price_modifier IS 'Модификатор максимальной цены (1.00-10.00 от базовой)';
COMMENT ON COLUMN economy.pricing_model_config.player_control_percentage IS 'Процент контроля игроков над ценами (0.00-100.00)';
COMMENT ON COLUMN economy.pricing_model_config.npc_control_percentage IS 'Процент контроля NPC над ценами (0.00-100.00)';
COMMENT ON COLUMN economy.economy_governance_rules.governance_entity_type IS 'Тип сущности управления: player, npc, faction, system';
COMMENT ON COLUMN economy.economy_governance_rules.rule_type IS 'Тип правила: price_control, supply_control, tax_control, access_control';
COMMENT ON COLUMN economy.economy_governance_rules.rule_scope IS 'Область действия правила: global, regional, faction, market, item';
COMMENT ON COLUMN economy.economy_governance_rules.rule_config IS 'Конфигурация правила в JSONB';
COMMENT ON COLUMN economy.economy_governance_rules.priority IS 'Приоритет правила (чем выше, тем важнее)';

