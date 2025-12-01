-- Issue: #140876090
-- Stock Exchange System Database Schema
-- Создание таблиц для системы фондовой биржи:
-- - Корпорации (corporations)
-- - История цен акций (stock_prices)
-- - Портфели игроков (player_portfolios)
-- - Расписание дивидендов (dividend_schedules)
-- - Выплаты дивидендов (dividend_payments)
-- - Биржевые индексы (stock_indices)
-- - Состав индексов (index_constituents)
-- - История индексов (index_history)
-- - Влияние событий на акции (stock_events_impact)
-- - Алерты о нарушениях (compliance_alerts)
-- Примечание: stock_orders и stock_trades уже созданы в V1_51, но используют stock_symbol

-- Создание схемы economy, если её нет (уже создана в V1_15, но для безопасности)
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'stock_type') THEN
        CREATE TYPE stock_type AS ENUM ('Common', 'Preferred');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'dividend_type') THEN
        CREATE TYPE dividend_type AS ENUM ('QUARTERLY', 'ANNUAL');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'dividend_status') THEN
        CREATE TYPE dividend_status AS ENUM ('SCHEDULED', 'DECLARED', 'PAID');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'index_calculation_method') THEN
        CREATE TYPE index_calculation_method AS ENUM ('PRICE_WEIGHTED', 'MARKET_CAP_WEIGHTED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'compliance_severity') THEN
        CREATE TYPE compliance_severity AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'compliance_alert_status') THEN
        CREATE TYPE compliance_alert_status AS ENUM ('OPEN', 'INVESTIGATING', 'RESOLVED');
    END IF;
END $$;

-- Таблица корпораций на бирже
CREATE TABLE IF NOT EXISTS economy.corporations (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    sector VARCHAR(100),
    stock_type stock_type NOT NULL DEFAULT 'Common',
    total_shares BIGINT NOT NULL DEFAULT 0 CHECK (total_shares >= 0),
    ipo_date TIMESTAMP,
    delisted_at TIMESTAMP,
    faction_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для corporations
CREATE INDEX IF NOT EXISTS idx_corporations_symbol ON economy.corporations(symbol);
CREATE INDEX IF NOT EXISTS idx_corporations_sector ON economy.corporations(sector);
CREATE INDEX IF NOT EXISTS idx_corporations_faction_id ON economy.corporations(faction_id) WHERE faction_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_corporations_delisted_at ON economy.corporations(delisted_at) WHERE delisted_at IS NULL;

-- Таблица истории цен акций
CREATE TABLE IF NOT EXISTS economy.stock_prices (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    price DECIMAL(20, 4) NOT NULL CHECK (price > 0),
    volume BIGINT NOT NULL DEFAULT 0 CHECK (volume >= 0),
    high DECIMAL(20, 4) NOT NULL CHECK (high > 0),
    low DECIMAL(20, 4) NOT NULL CHECK (low > 0),
    open DECIMAL(20, 4) NOT NULL CHECK (open > 0),
    close DECIMAL(20, 4) NOT NULL CHECK (close > 0),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    event_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для stock_prices
CREATE INDEX IF NOT EXISTS idx_stock_prices_corporation_id ON economy.stock_prices(corporation_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_stock_prices_timestamp ON economy.stock_prices(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_stock_prices_event_id ON economy.stock_prices(event_id) WHERE event_id IS NOT NULL;

-- Таблица портфелей игроков
CREATE TABLE IF NOT EXISTS economy.player_portfolios (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    quantity BIGINT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    average_buy_price DECIMAL(20, 4) NOT NULL DEFAULT 0.0 CHECK (average_buy_price >= 0),
    total_invested DECIMAL(20, 4) NOT NULL DEFAULT 0.0 CHECK (total_invested >= 0),
    total_dividends_received DECIMAL(20, 4) NOT NULL DEFAULT 0.0 CHECK (total_dividends_received >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, corporation_id)
);

-- Индексы для player_portfolios
CREATE INDEX IF NOT EXISTS idx_player_portfolios_character_id ON economy.player_portfolios(character_id);
CREATE INDEX IF NOT EXISTS idx_player_portfolios_corporation_id ON economy.player_portfolios(corporation_id);

-- Таблица расписания дивидендов
CREATE TABLE IF NOT EXISTS economy.dividend_schedules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    dividend_type dividend_type NOT NULL,
    amount_per_share DECIMAL(20, 4) NOT NULL CHECK (amount_per_share > 0),
    declaration_date DATE NOT NULL,
    ex_dividend_date DATE NOT NULL,
    record_date DATE NOT NULL,
    payment_date DATE NOT NULL,
    status dividend_status NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для dividend_schedules
CREATE INDEX IF NOT EXISTS idx_dividend_schedules_corporation_id ON economy.dividend_schedules(corporation_id, status);
CREATE INDEX IF NOT EXISTS idx_dividend_schedules_payment_date ON economy.dividend_schedules(payment_date, status);
CREATE INDEX IF NOT EXISTS idx_dividend_schedules_status ON economy.dividend_schedules(status);

-- Таблица выплат дивидендов
CREATE TABLE IF NOT EXISTS economy.dividend_payments (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    dividend_schedule_id UUID NOT NULL REFERENCES economy.dividend_schedules(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    shares_owned BIGINT NOT NULL CHECK (shares_owned > 0),
    dividend_amount DECIMAL(20, 4) NOT NULL CHECK (dividend_amount > 0),
    tax_amount DECIMAL(20, 4) NOT NULL DEFAULT 0.0 CHECK (tax_amount >= 0),
    net_amount DECIMAL(20, 4) NOT NULL CHECK (net_amount > 0),
    reinvested BOOLEAN NOT NULL DEFAULT false,
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для dividend_payments
CREATE INDEX IF NOT EXISTS idx_dividend_payments_dividend_schedule_id ON economy.dividend_payments(dividend_schedule_id);
CREATE INDEX IF NOT EXISTS idx_dividend_payments_character_id ON economy.dividend_payments(character_id, paid_at DESC);
CREATE INDEX IF NOT EXISTS idx_dividend_payments_corporation_id ON economy.dividend_payments(corporation_id);

-- Таблица биржевых индексов
CREATE TABLE IF NOT EXISTS economy.stock_indices (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    calculation_method index_calculation_method NOT NULL,
    base_value DECIMAL(20, 4) NOT NULL DEFAULT 1000.0 CHECK (base_value > 0),
    current_value DECIMAL(20, 4) NOT NULL DEFAULT 1000.0 CHECK (current_value > 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для stock_indices
CREATE INDEX IF NOT EXISTS idx_stock_indices_symbol ON economy.stock_indices(symbol);

-- Таблица состава индексов
CREATE TABLE IF NOT EXISTS economy.index_constituents (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    index_id UUID NOT NULL REFERENCES economy.stock_indices(id) ON DELETE CASCADE,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    weight DECIMAL(10, 6) NOT NULL CHECK (weight >= 0 AND weight <= 1),
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    removed_at TIMESTAMP,
    UNIQUE(index_id, corporation_id, removed_at)
);

-- Индексы для index_constituents
CREATE INDEX IF NOT EXISTS idx_index_constituents_index_id ON economy.index_constituents(index_id, removed_at) WHERE removed_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_index_constituents_corporation_id ON economy.index_constituents(corporation_id);

-- Таблица истории индексов
CREATE TABLE IF NOT EXISTS economy.index_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    index_id UUID NOT NULL REFERENCES economy.stock_indices(id) ON DELETE CASCADE,
    value DECIMAL(20, 4) NOT NULL CHECK (value > 0),
    change DECIMAL(20, 4) NOT NULL,
    change_percent DECIMAL(10, 4) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для index_history
CREATE INDEX IF NOT EXISTS idx_index_history_index_id ON economy.index_history(index_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_index_history_timestamp ON economy.index_history(timestamp DESC);

-- Таблица влияния событий на акции
CREATE TABLE IF NOT EXISTS economy.stock_events_impact (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    corporation_id UUID NOT NULL REFERENCES economy.corporations(id) ON DELETE CASCADE,
    impact_percent DECIMAL(10, 4) NOT NULL,
    base_price DECIMAL(20, 4) NOT NULL CHECK (base_price > 0),
    new_price DECIMAL(20, 4) NOT NULL CHECK (new_price > 0),
    duration_hours INTEGER NOT NULL CHECK (duration_hours > 0),
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для stock_events_impact
CREATE INDEX IF NOT EXISTS idx_stock_events_impact_event_id ON economy.stock_events_impact(event_id);
CREATE INDEX IF NOT EXISTS idx_stock_events_impact_corporation_id ON economy.stock_events_impact(corporation_id, started_at DESC);
CREATE INDEX IF NOT EXISTS idx_stock_events_impact_ended_at ON economy.stock_events_impact(ended_at) WHERE ended_at IS NULL;

-- Таблица алертов о нарушениях
CREATE TABLE IF NOT EXISTS economy.compliance_alerts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    alert_type VARCHAR(50) NOT NULL,
    severity compliance_severity NOT NULL,
    description TEXT,
    trade_id UUID,
    status compliance_alert_status NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

-- Индексы для compliance_alerts
CREATE INDEX IF NOT EXISTS idx_compliance_alerts_character_id ON economy.compliance_alerts(character_id, status);
CREATE INDEX IF NOT EXISTS idx_compliance_alerts_status ON economy.compliance_alerts(status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_compliance_alerts_severity ON economy.compliance_alerts(severity, status) WHERE status != 'RESOLVED';

-- Комментарии к таблицам
COMMENT ON TABLE economy.corporations IS 'Корпорации на бирже';
COMMENT ON TABLE economy.stock_prices IS 'История цен акций';
COMMENT ON TABLE economy.player_portfolios IS 'Портфели игроков (акции в собственности)';
COMMENT ON TABLE economy.dividend_schedules IS 'Расписание дивидендов';
COMMENT ON TABLE economy.dividend_payments IS 'Выплаты дивидендов игрокам';
COMMENT ON TABLE economy.stock_indices IS 'Биржевые индексы';
COMMENT ON TABLE economy.index_constituents IS 'Состав индексов (корпорации в индексе)';
COMMENT ON TABLE economy.index_history IS 'История значений индексов';
COMMENT ON TABLE economy.stock_events_impact IS 'Влияние событий на акции';
COMMENT ON TABLE economy.compliance_alerts IS 'Алерты о нарушениях правил торговли';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.corporations.symbol IS 'Символ корпорации на бирже (уникальный)';
COMMENT ON COLUMN economy.corporations.stock_type IS 'Тип акций: Common или Preferred';
COMMENT ON COLUMN economy.corporations.total_shares IS 'Общее количество акций';
COMMENT ON COLUMN economy.corporations.delisted_at IS 'Дата исключения из листинга (NULL если активна)';
COMMENT ON COLUMN economy.stock_prices.event_id IS 'ID события, повлиявшего на цену (nullable)';
COMMENT ON COLUMN economy.player_portfolios.average_buy_price IS 'Средняя цена покупки';
COMMENT ON COLUMN economy.player_portfolios.total_invested IS 'Общая сумма инвестиций';
COMMENT ON COLUMN economy.player_portfolios.total_dividends_received IS 'Общая сумма полученных дивидендов';
COMMENT ON COLUMN economy.dividend_schedules.dividend_type IS 'Тип дивидендов: QUARTERLY или ANNUAL';
COMMENT ON COLUMN economy.dividend_schedules.ex_dividend_date IS 'Дата ex-dividend (после которой дивиденды не начисляются)';
COMMENT ON COLUMN economy.dividend_schedules.record_date IS 'Дата записи (кто получает дивиденды)';
COMMENT ON COLUMN economy.dividend_payments.reinvested IS 'Флаг реинвестирования дивидендов';
COMMENT ON COLUMN economy.stock_indices.calculation_method IS 'Метод расчета: PRICE_WEIGHTED или MARKET_CAP_WEIGHTED';
COMMENT ON COLUMN economy.index_constituents.weight IS 'Вес корпорации в индексе (0-1)';
COMMENT ON COLUMN economy.index_constituents.removed_at IS 'Дата удаления из индекса (NULL если активна)';
COMMENT ON COLUMN economy.stock_events_impact.impact_percent IS 'Процент влияния события на цену';
COMMENT ON COLUMN economy.stock_events_impact.duration_hours IS 'Длительность влияния в часах';
COMMENT ON COLUMN economy.compliance_alerts.severity IS 'Серьезность нарушения: LOW, MEDIUM, HIGH, CRITICAL';
COMMENT ON COLUMN economy.compliance_alerts.status IS 'Статус алерта: OPEN, INVESTIGATING, RESOLVED';


