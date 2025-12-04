-- Issue: #140890193
-- Currency Exchange System Database Schema
-- Создание таблиц для системы валютной биржи:
-- - currency_exchange_rates (курсы валютных пар)
-- - currency_exchange_rate_history (история курсов)
-- - currency_exchange_orders (ордера на обмен валют)
-- - currency_exchange_trades (исполненные сделки)
-- - currency_exchange_risk_limits (лимиты рисков для игроков)

-- Создание схемы economy, если её нет
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'currency_order_type') THEN
        CREATE TYPE currency_order_type AS ENUM ('instant', 'limit');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'currency_order_side') THEN
        CREATE TYPE currency_order_side AS ENUM ('buy', 'sell');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'currency_order_status') THEN
        CREATE TYPE currency_order_status AS ENUM ('pending', 'active', 'partially_filled', 'filled', 'cancelled', 'expired', 'blocked');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'currency_risk_limit_type') THEN
        CREATE TYPE currency_risk_limit_type AS ENUM ('daily_volume', 'transaction_count', 'aml_limit');
    END IF;
END $$;

-- Таблица курсов валютных пар
CREATE TABLE IF NOT EXISTS economy.currency_exchange_rates (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  currency_pair VARCHAR(10) NOT NULL UNIQUE,
  base_currency VARCHAR(10) NOT NULL,
  quote_currency VARCHAR(10) NOT NULL,
  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rate DECIMAL(20,8) NOT NULL,
  bid_rate DECIMAL(20,8) NOT NULL,
  ask_rate DECIMAL(20,8) NOT NULL,
  spread DECIMAL(10,4) NOT NULL DEFAULT 0.0000,
  daily_volume DECIMAL(20,2) NOT NULL DEFAULT 0.00,
  volatility DECIMAL(5,2) NOT NULL DEFAULT 0.00,
  CHECK (base_currency != quote_currency),
  CHECK (bid_rate <= ask_rate),
  CHECK (spread >= 0.0000)
);

-- Индексы для currency_exchange_rates
CREATE INDEX IF NOT EXISTS idx_currency_exchange_rates_pair_updated 
    ON economy.currency_exchange_rates(currency_pair, last_updated DESC);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_rates_base_quote 
    ON economy.currency_exchange_rates(base_currency, quote_currency);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_rates_last_updated 
    ON economy.currency_exchange_rates(last_updated DESC);

-- Таблица истории курсов валютных пар
CREATE TABLE IF NOT EXISTS economy.currency_exchange_rate_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  currency_pair VARCHAR(10) NOT NULL,
  recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rate DECIMAL(20,8) NOT NULL,
  bid_rate DECIMAL(20,8) NOT NULL,
  ask_rate DECIMAL(20,8) NOT NULL,
  spread DECIMAL(10,4) NOT NULL DEFAULT 0.0000,
  volume DECIMAL(20,2) NOT NULL DEFAULT 0.00
);

-- Индексы для currency_exchange_rate_history
CREATE INDEX IF NOT EXISTS idx_currency_exchange_rate_history_pair_recorded 
    ON economy.currency_exchange_rate_history(currency_pair, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_rate_history_recorded 
    ON economy.currency_exchange_rate_history(recorded_at DESC);

-- Таблица ордеров на обмен валют
CREATE TABLE IF NOT EXISTS economy.currency_exchange_orders (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  currency_pair VARCHAR(10) NOT NULL,
  base_currency VARCHAR(10) NOT NULL,
  quote_currency VARCHAR(10) NOT NULL,
  expires_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  executed_at TIMESTAMP,
  amount DECIMAL(20,2) NOT NULL CHECK (amount > 0),
  limit_rate DECIMAL(20,8),
  executed_amount DECIMAL(20,2) NOT NULL DEFAULT 0.00 CHECK (executed_amount >= 0),
  fee DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (fee >= 0),
  fee_discount DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (fee_discount >= 0 AND fee_discount <= 100.00),
  ttl_seconds INTEGER CHECK (ttl_seconds IS NULL OR ttl_seconds > 0),
  order_type currency_order_type NOT NULL,
  side currency_order_side NOT NULL,
  status currency_order_status NOT NULL DEFAULT 'pending',
  CHECK (executed_amount <= amount),
  CHECK (
        (order_type = 'limit' AND limit_rate IS NOT NULL) OR
        (order_type = 'instant' AND limit_rate IS NULL)
    )
);

-- Индексы для currency_exchange_orders
CREATE INDEX IF NOT EXISTS idx_currency_exchange_orders_player_status 
    ON economy.currency_exchange_orders(player_id, status);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_orders_pair_status_created 
    ON economy.currency_exchange_orders(currency_pair, status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_orders_status_expires 
    ON economy.currency_exchange_orders(status, expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_currency_exchange_orders_pair_type_side_status 
    ON economy.currency_exchange_orders(currency_pair, order_type, side, status) WHERE status IN ('active', 'pending');

-- Таблица исполненных сделок
CREATE TABLE IF NOT EXISTS economy.currency_exchange_trades (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  buy_order_id UUID NOT NULL REFERENCES economy.currency_exchange_orders(id) ON DELETE CASCADE,
  sell_order_id UUID NOT NULL REFERENCES economy.currency_exchange_orders(id) ON DELETE CASCADE,
  currency_pair VARCHAR(10) NOT NULL,
  base_currency VARCHAR(10) NOT NULL,
  quote_currency VARCHAR(10) NOT NULL,
  executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(20,2) NOT NULL CHECK (amount > 0),
  rate DECIMAL(20,8) NOT NULL CHECK (rate > 0),
  total DECIMAL(20,2) NOT NULL CHECK (total > 0),
  fee DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (fee >= 0),
  CHECK (buy_order_id != sell_order_id)
);

-- Индексы для currency_exchange_trades
CREATE INDEX IF NOT EXISTS idx_currency_exchange_trades_buy_order 
    ON economy.currency_exchange_trades(buy_order_id);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_trades_sell_order 
    ON economy.currency_exchange_trades(sell_order_id);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_trades_pair_executed 
    ON economy.currency_exchange_trades(currency_pair, executed_at DESC);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_trades_executed_at 
    ON economy.currency_exchange_trades(executed_at DESC);

-- Таблица лимитов рисков для игроков
CREATE TABLE IF NOT EXISTS economy.currency_exchange_risk_limits (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  period_start TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  period_end TIMESTAMP NOT NULL,
  reset_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  limit_value DECIMAL(20,2) NOT NULL CHECK (limit_value >= 0),
  current_value DECIMAL(20,2) NOT NULL DEFAULT 0.00 CHECK (current_value >= 0),
  limit_type currency_risk_limit_type NOT NULL,
  CHECK (current_value <= limit_value),
  CHECK (period_start < period_end),
  CHECK (reset_at >= period_end)
);

-- Индексы для currency_exchange_risk_limits
CREATE INDEX IF NOT EXISTS idx_currency_exchange_risk_limits_player_type_period 
    ON economy.currency_exchange_risk_limits(player_id, limit_type, period_start DESC);
CREATE INDEX IF NOT EXISTS idx_currency_exchange_risk_limits_reset_at 
    ON economy.currency_exchange_risk_limits(reset_at) WHERE reset_at > CURRENT_TIMESTAMP;

-- Комментарии к таблицам
COMMENT ON TABLE economy.currency_exchange_rates IS 'Курсы валютных пар (текущие курсы, спреды, волатильность)';
COMMENT ON TABLE economy.currency_exchange_rate_history IS 'История курсов валютных пар (для графиков и аналитики)';
COMMENT ON TABLE economy.currency_exchange_orders IS 'Ордера на обмен валют (instant и limit ордера)';
COMMENT ON TABLE economy.currency_exchange_trades IS 'Исполненные сделки (история обменов)';
COMMENT ON TABLE economy.currency_exchange_risk_limits IS 'Лимиты рисков для игроков (AML, дневные лимиты, количество транзакций)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.currency_exchange_rates.currency_pair IS 'Валютная пара (например, "EDDY/USD", "USD/EUR")';
COMMENT ON COLUMN economy.currency_exchange_rates.base_currency IS 'Базовая валюта (валюта, которую покупают/продают)';
COMMENT ON COLUMN economy.currency_exchange_rates.quote_currency IS 'Котируемая валюта (валюта, в которой выражается цена)';
COMMENT ON COLUMN economy.currency_exchange_rates.rate IS 'Средний курс валютной пары';
COMMENT ON COLUMN economy.currency_exchange_rates.bid_rate IS 'Курс покупки (bid)';
COMMENT ON COLUMN economy.currency_exchange_rates.ask_rate IS 'Курс продажи (ask)';
COMMENT ON COLUMN economy.currency_exchange_rates.spread IS 'Спред (разница между ask и bid)';
COMMENT ON COLUMN economy.currency_exchange_rates.daily_volume IS 'Дневной объем торговли';
COMMENT ON COLUMN economy.currency_exchange_rates.volatility IS 'Волатильность курса (в процентах)';
COMMENT ON COLUMN economy.currency_exchange_orders.order_type IS 'Тип ордера: instant (мгновенный), limit (лимитный)';
COMMENT ON COLUMN economy.currency_exchange_orders.side IS 'Сторона ордера: buy (покупка), sell (продажа)';
COMMENT ON COLUMN economy.currency_exchange_orders.amount IS 'Сумма обмена в базовой валюте';
COMMENT ON COLUMN economy.currency_exchange_orders.limit_rate IS 'Лимитный курс (для limit ордеров)';
COMMENT ON COLUMN economy.currency_exchange_orders.executed_amount IS 'Исполненная сумма (для частичного исполнения)';
COMMENT ON COLUMN economy.currency_exchange_orders.status IS 'Статус ордера: pending, active, partially_filled, filled, cancelled, expired, blocked';
COMMENT ON COLUMN economy.currency_exchange_orders.fee IS 'Комиссия за обмен';
COMMENT ON COLUMN economy.currency_exchange_orders.fee_discount IS 'Скидка на комиссию (в процентах, 0-100)';
COMMENT ON COLUMN economy.currency_exchange_orders.ttl_seconds IS 'Время жизни ордера в секундах (для limit ордеров)';
COMMENT ON COLUMN economy.currency_exchange_orders.expires_at IS 'Время истечения ордера (для limit ордеров)';
COMMENT ON COLUMN economy.currency_exchange_trades.buy_order_id IS 'ID ордера на покупку';
COMMENT ON COLUMN economy.currency_exchange_trades.sell_order_id IS 'ID ордера на продажу';
COMMENT ON COLUMN economy.currency_exchange_trades.amount IS 'Сумма сделки в базовой валюте';
COMMENT ON COLUMN economy.currency_exchange_trades.rate IS 'Курс исполнения сделки';
COMMENT ON COLUMN economy.currency_exchange_trades.total IS 'Общая сумма сделки в котируемой валюте';
COMMENT ON COLUMN economy.currency_exchange_trades.fee IS 'Комиссия за сделку';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.limit_type IS 'Тип лимита: daily_volume, transaction_count, aml_limit';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.limit_value IS 'Максимальное значение лимита';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.current_value IS 'Текущее значение лимита';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.period_start IS 'Начало периода лимита';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.period_end IS 'Конец периода лимита';
COMMENT ON COLUMN economy.currency_exchange_risk_limits.reset_at IS 'Время сброса лимита';


