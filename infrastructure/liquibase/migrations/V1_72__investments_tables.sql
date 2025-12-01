-- Issue: #140890222
-- Investments System Database Schema
-- Создание таблиц для системы инвестиций:
-- - investment_products (инвестиционные продукты)
-- - player_investment_positions (инвестиционные позиции игроков)
-- - investment_transactions (транзакции по инвестициям)
-- - portfolio_snapshots (снимки портфелей для аналитики)
-- - investment_risk_alerts (алерты по рискам инвестиций)

-- Создание схемы economy, если её нет
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_product_type') THEN
        CREATE TYPE investment_product_type AS ENUM ('stock', 'bond', 'real_estate', 'production_share', 'commodity_speculation');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_risk_level') THEN
        CREATE TYPE investment_risk_level AS ENUM ('low', 'medium', 'high', 'very_high');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_liquidity') THEN
        CREATE TYPE investment_liquidity AS ENUM ('high', 'medium', 'low');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_position_type') THEN
        CREATE TYPE investment_position_type AS ENUM ('long', 'short');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_position_status') THEN
        CREATE TYPE investment_position_status AS ENUM ('active', 'closed', 'suspended');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_transaction_type') THEN
        CREATE TYPE investment_transaction_type AS ENUM ('purchase', 'sale', 'dividend', 'interest', 'fee', 'tax');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_transaction_status') THEN
        CREATE TYPE investment_transaction_status AS ENUM ('pending', 'completed', 'failed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_risk_alert_type') THEN
        CREATE TYPE investment_risk_alert_type AS ENUM ('margin_call', 'kyc_required', 'high_risk', 'suitability_warning');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'investment_risk_alert_severity') THEN
        CREATE TYPE investment_risk_alert_severity AS ENUM ('low', 'medium', 'high', 'critical');
    END IF;
END $$;

-- Таблица инвестиционных продуктов
CREATE TABLE IF NOT EXISTS economy.investment_products (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_type investment_product_type NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    issuer_id UUID, -- FK corporations/factions (nullable)
    base_yield_percentage DECIMAL(5,2) NOT NULL,
    risk_level investment_risk_level NOT NULL,
    min_investment_amount DECIMAL(10,2) NOT NULL,
    max_investment_amount DECIMAL(10,2), -- nullable
    liquidity investment_liquidity NOT NULL,
    maturity_days INTEGER, -- nullable, для облигаций
    available BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для investment_products
CREATE INDEX IF NOT EXISTS idx_investment_products_product_type_available 
    ON economy.investment_products(product_type, available);
CREATE INDEX IF NOT EXISTS idx_investment_products_issuer_id 
    ON economy.investment_products(issuer_id) WHERE issuer_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_investment_products_risk_level 
    ON economy.investment_products(risk_level);

-- Таблица инвестиционных позиций игроков
CREATE TABLE IF NOT EXISTS economy.player_investment_positions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL, -- FK accounts
    product_id UUID NOT NULL REFERENCES economy.investment_products(id) ON DELETE RESTRICT,
    position_type investment_position_type, -- nullable
    amount DECIMAL(10,2) NOT NULL,
    purchase_price DECIMAL(10,2) NOT NULL,
    current_price DECIMAL(10,2) NOT NULL,
    current_value DECIMAL(10,2) NOT NULL,
    profit_loss DECIMAL(10,2) NOT NULL DEFAULT 0,
    profit_loss_percentage DECIMAL(5,2) NOT NULL DEFAULT 0,
    status investment_position_status NOT NULL DEFAULT 'active',
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP, -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для player_investment_positions
CREATE INDEX IF NOT EXISTS idx_player_investment_positions_player_status 
    ON economy.player_investment_positions(player_id, status);
CREATE INDEX IF NOT EXISTS idx_player_investment_positions_product_status 
    ON economy.player_investment_positions(product_id, status);
CREATE INDEX IF NOT EXISTS idx_player_investment_positions_purchased_at 
    ON economy.player_investment_positions(purchased_at DESC);

-- Таблица транзакций по инвестициям
CREATE TABLE IF NOT EXISTS economy.investment_transactions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL, -- FK accounts
    position_id UUID REFERENCES economy.player_investment_positions(id) ON DELETE SET NULL, -- nullable
    transaction_type investment_transaction_type NOT NULL,
    product_id UUID NOT NULL REFERENCES economy.investment_products(id) ON DELETE RESTRICT,
    amount DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2), -- nullable
    total DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    status investment_transaction_status NOT NULL DEFAULT 'pending',
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для investment_transactions
CREATE INDEX IF NOT EXISTS idx_investment_transactions_player_executed 
    ON economy.investment_transactions(player_id, executed_at DESC);
CREATE INDEX IF NOT EXISTS idx_investment_transactions_position_id 
    ON economy.investment_transactions(position_id) WHERE position_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_investment_transactions_type_status 
    ON economy.investment_transactions(transaction_type, status);

-- Таблица снимков портфелей для аналитики
CREATE TABLE IF NOT EXISTS economy.portfolio_snapshots (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL, -- FK accounts
    total_value DECIMAL(10,2) NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    total_profit_loss DECIMAL(10,2) NOT NULL,
    total_profit_loss_percentage DECIMAL(5,2) NOT NULL,
    distribution JSONB NOT NULL DEFAULT '{}', -- распределение по типам продуктов
    risk_score DECIMAL(3,2) NOT NULL DEFAULT 0.00, -- 0.00-1.00
    snapshot_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для portfolio_snapshots
CREATE INDEX IF NOT EXISTS idx_portfolio_snapshots_player_snapshot 
    ON economy.portfolio_snapshots(player_id, snapshot_at DESC);
CREATE INDEX IF NOT EXISTS idx_portfolio_snapshots_snapshot_at 
    ON economy.portfolio_snapshots(snapshot_at DESC);

-- Таблица алертов по рискам инвестиций
CREATE TABLE IF NOT EXISTS economy.investment_risk_alerts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL, -- FK accounts
    position_id UUID REFERENCES economy.player_investment_positions(id) ON DELETE CASCADE, -- nullable
    alert_type investment_risk_alert_type NOT NULL,
    severity investment_risk_alert_severity NOT NULL,
    message TEXT NOT NULL,
    resolved BOOLEAN NOT NULL DEFAULT false,
    resolved_at TIMESTAMP, -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для investment_risk_alerts
CREATE INDEX IF NOT EXISTS idx_investment_risk_alerts_player_resolved 
    ON economy.investment_risk_alerts(player_id, resolved);
CREATE INDEX IF NOT EXISTS idx_investment_risk_alerts_type_severity 
    ON economy.investment_risk_alerts(alert_type, severity);

-- Комментарии к таблицам
COMMENT ON TABLE economy.investment_products IS 'Инвестиционные продукты (акции, облигации, недвижимость, производственные доли, спекуляции)';
COMMENT ON TABLE economy.player_investment_positions IS 'Инвестиционные позиции игроков';
COMMENT ON TABLE economy.investment_transactions IS 'Транзакции по инвестициям (покупка, продажа, дивиденды, проценты, комиссии, налоги)';
COMMENT ON TABLE economy.portfolio_snapshots IS 'Снимки портфелей для аналитики и истории';
COMMENT ON TABLE economy.investment_risk_alerts IS 'Алерты по рискам инвестиций (margin call, KYC, high risk, suitability warning)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.investment_products.product_type IS 'Тип продукта: stock, bond, real_estate, production_share, commodity_speculation';
COMMENT ON COLUMN economy.investment_products.issuer_id IS 'ID эмитента (корпорации/фракции)';
COMMENT ON COLUMN economy.investment_products.base_yield_percentage IS 'Базовая доходность в процентах';
COMMENT ON COLUMN economy.investment_products.risk_level IS 'Уровень риска: low, medium, high, very_high';
COMMENT ON COLUMN economy.investment_products.min_investment_amount IS 'Минимальная сумма инвестиции';
COMMENT ON COLUMN economy.investment_products.max_investment_amount IS 'Максимальная сумма инвестиции (NULL = без ограничений)';
COMMENT ON COLUMN economy.investment_products.liquidity IS 'Ликвидность: high, medium, low';
COMMENT ON COLUMN economy.investment_products.maturity_days IS 'Срок погашения в днях (для облигаций)';
COMMENT ON COLUMN economy.player_investment_positions.position_type IS 'Тип позиции: long, short (NULL = long по умолчанию)';
COMMENT ON COLUMN economy.player_investment_positions.amount IS 'Количество единиц продукта';
COMMENT ON COLUMN economy.player_investment_positions.purchase_price IS 'Цена покупки за единицу';
COMMENT ON COLUMN economy.player_investment_positions.current_price IS 'Текущая цена за единицу';
COMMENT ON COLUMN economy.player_investment_positions.current_value IS 'Текущая стоимость позиции';
COMMENT ON COLUMN economy.player_investment_positions.profit_loss IS 'Прибыль/убыток в абсолютных значениях';
COMMENT ON COLUMN economy.player_investment_positions.profit_loss_percentage IS 'Прибыль/убыток в процентах';
COMMENT ON COLUMN economy.player_investment_positions.status IS 'Статус позиции: active, closed, suspended';
COMMENT ON COLUMN economy.investment_transactions.position_id IS 'ID позиции (NULL для транзакций без позиции)';
COMMENT ON COLUMN economy.investment_transactions.transaction_type IS 'Тип транзакции: purchase, sale, dividend, interest, fee, tax';
COMMENT ON COLUMN economy.investment_transactions.price IS 'Цена за единицу (NULL для дивидендов/процентов)';
COMMENT ON COLUMN economy.investment_transactions.total IS 'Общая сумма транзакции';
COMMENT ON COLUMN economy.investment_transactions.status IS 'Статус транзакции: pending, completed, failed, cancelled';
COMMENT ON COLUMN economy.portfolio_snapshots.distribution IS 'Распределение портфеля по типам продуктов в JSONB';
COMMENT ON COLUMN economy.portfolio_snapshots.risk_score IS 'Оценка риска портфеля (0.00-1.00)';
COMMENT ON COLUMN economy.investment_risk_alerts.position_id IS 'ID позиции (NULL для портфельных алертов)';
COMMENT ON COLUMN economy.investment_risk_alerts.alert_type IS 'Тип алерта: margin_call, kyc_required, high_risk, suitability_warning';
COMMENT ON COLUMN economy.investment_risk_alerts.severity IS 'Серьезность алерта: low, medium, high, critical';

