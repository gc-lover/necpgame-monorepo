-- Issue: #140890164
-- Economy Analytics System Database Schema
-- Создание таблиц для системы экономической аналитики:
-- - analytics_chart_data (данные для графиков)
-- - analytics_indicators (технические индикаторы)
-- - analytics_sentiment (анализ настроений)
-- - analytics_portfolio_snapshots (снимки портфеля)
-- - analytics_alerts (оповещения игроков)
-- - analytics_settings (настройки аналитики)

-- Создание схемы analytics, если её нет
CREATE SCHEMA IF NOT EXISTS analytics;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'chart_type') THEN
        CREATE TYPE chart_type AS ENUM ('line', 'candlestick', 'ohlc', 'area', 'volume');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'indicator_signal') THEN
        CREATE TYPE indicator_signal AS ENUM ('buy', 'sell', 'neutral');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'alert_type') THEN
        CREATE TYPE alert_type AS ENUM ('price', 'event');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'alert_condition') THEN
        CREATE TYPE alert_condition AS ENUM ('above', 'below', 'equals', 'change_percentage');
    END IF;
END $$;

-- Таблица данных для графиков
CREATE TABLE IF NOT EXISTS analytics.analytics_chart_data (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  symbol VARCHAR(50) NOT NULL,
  time_frame VARCHAR(20) NOT NULL CHECK (time_frame IN ('1m', '5m', '15m', '1h', '4h', '1d', '1w')),
  data JSONB NOT NULL DEFAULT '{}'::jsonb,
  timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  open DECIMAL(20,8),
  high DECIMAL(20,8),
  low DECIMAL(20,8),
  close DECIMAL(20,8) NOT NULL,
  volume DECIMAL(20,2),
  chart_type chart_type NOT NULL
);

-- Индексы для analytics_chart_data
CREATE INDEX IF NOT EXISTS idx_analytics_chart_data_symbol_type_frame_timestamp 
    ON analytics.analytics_chart_data(symbol, chart_type, time_frame, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_chart_data_timestamp 
    ON analytics.analytics_chart_data(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_chart_data_symbol_timestamp 
    ON analytics.analytics_chart_data(symbol, timestamp DESC);

-- Таблица технических индикаторов
CREATE TABLE IF NOT EXISTS analytics.analytics_indicators (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  symbol VARCHAR(50) NOT NULL,
  indicator_type VARCHAR(50) NOT NULL,
  parameters JSONB NOT NULL DEFAULT '{}'::jsonb,
  timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  value DECIMAL(20,8) NOT NULL,
  signal indicator_signal
);

-- Индексы для analytics_indicators
CREATE INDEX IF NOT EXISTS idx_analytics_indicators_symbol_type_timestamp 
    ON analytics.analytics_indicators(symbol, indicator_type, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_indicators_timestamp 
    ON analytics.analytics_indicators(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_indicators_symbol_timestamp 
    ON analytics.analytics_indicators(symbol, timestamp DESC);

-- Таблица анализа настроений
CREATE TABLE IF NOT EXISTS analytics.analytics_sentiment (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  symbol VARCHAR(50),
  timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fear_greed_index DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (fear_greed_index >= 0.00 AND fear_greed_index <= 100.00),
  sentiment_score DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (sentiment_score >= -100.00 AND sentiment_score <= 100.00),
  bullish_signals INTEGER NOT NULL DEFAULT 0,
  bearish_signals INTEGER NOT NULL DEFAULT 0
);

-- Индексы для analytics_sentiment
CREATE INDEX IF NOT EXISTS idx_analytics_sentiment_symbol_timestamp 
    ON analytics.analytics_sentiment(symbol, timestamp DESC) WHERE symbol IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_analytics_sentiment_timestamp 
    ON analytics.analytics_sentiment(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_sentiment_symbol_null_timestamp 
    ON analytics.analytics_sentiment(timestamp DESC) WHERE symbol IS NULL;

-- Таблица снимков портфеля для аналитики
CREATE TABLE IF NOT EXISTS analytics.analytics_portfolio_snapshots (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_value DECIMAL(20,2) NOT NULL DEFAULT 0.00,
  total_cost DECIMAL(20,2) NOT NULL DEFAULT 0.00,
  total_return DECIMAL(20,2) NOT NULL DEFAULT 0.00,
  total_return_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
  sharpe_ratio DECIMAL(5,2),
  win_rate DECIMAL(5,2) CHECK (win_rate IS NULL OR (win_rate >= 0.00 AND win_rate <= 100.00)),
  profit_factor DECIMAL(5,2)
);

-- Индексы для analytics_portfolio_snapshots
CREATE INDEX IF NOT EXISTS idx_analytics_portfolio_snapshots_player_timestamp 
    ON analytics.analytics_portfolio_snapshots(player_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_analytics_portfolio_snapshots_timestamp 
    ON analytics.analytics_portfolio_snapshots(timestamp DESC);

-- Таблица оповещений игроков
CREATE TABLE IF NOT EXISTS analytics.analytics_alerts (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  symbol VARCHAR(50),
  event_type VARCHAR(50),
  triggered_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  target_value DECIMAL(20,8),
  triggered BOOLEAN NOT NULL DEFAULT false,
  active BOOLEAN NOT NULL DEFAULT true,
  alert_type alert_type NOT NULL,
  condition alert_condition NOT NULL,
  CHECK (
        (alert_type = 'price' AND symbol IS NOT NULL AND event_type IS NULL) OR
        (alert_type = 'event' AND event_type IS NOT NULL)
    )
);

-- Индексы для analytics_alerts
CREATE INDEX IF NOT EXISTS idx_analytics_alerts_player_active 
    ON analytics.analytics_alerts(player_id, active) WHERE active = true;
CREATE INDEX IF NOT EXISTS idx_analytics_alerts_symbol_active_triggered 
    ON analytics.analytics_alerts(symbol, active, triggered) WHERE symbol IS NOT NULL AND active = true;
CREATE INDEX IF NOT EXISTS idx_analytics_alerts_triggered_at 
    ON analytics.analytics_alerts(triggered_at DESC) WHERE triggered = true;

-- Таблица настроек аналитики игроков
CREATE TABLE IF NOT EXISTS analytics.analytics_settings (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL UNIQUE REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    chart_preferences JSONB NOT NULL DEFAULT '{}'::jsonb,
    indicator_preferences JSONB NOT NULL DEFAULT '{}'::jsonb,
    alert_preferences JSONB NOT NULL DEFAULT '{}'::jsonb,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для analytics_settings
CREATE INDEX IF NOT EXISTS idx_analytics_settings_player_id 
    ON analytics.analytics_settings(player_id);

-- Комментарии к таблицам
COMMENT ON TABLE analytics.analytics_chart_data IS 'Данные для графиков (линейные, свечные, OHLC, area, гистограммы)';
COMMENT ON TABLE analytics.analytics_indicators IS 'Технические индикаторы (MA, RSI, MACD, Bollinger Bands и др.)';
COMMENT ON TABLE analytics.analytics_sentiment IS 'Анализ настроений рынка (бычьи/медвежьи сигналы, индекс страха и жадности)';
COMMENT ON TABLE analytics.analytics_portfolio_snapshots IS 'Снимки портфеля для аналитики (метрики доходности, риска, win rate)';
COMMENT ON TABLE analytics.analytics_alerts IS 'Оповещения игроков о ценах и событиях';
COMMENT ON TABLE analytics.analytics_settings IS 'Настройки аналитики игроков (предпочтения графиков, индикаторов, оповещений)';

-- Комментарии к колонкам
COMMENT ON COLUMN analytics.analytics_chart_data.symbol IS 'Символ актива (например, "BTC", "ETH", "AAPL")';
COMMENT ON COLUMN analytics.analytics_chart_data.chart_type IS 'Тип графика: line, candlestick, ohlc, area, volume';
COMMENT ON COLUMN analytics.analytics_chart_data.time_frame IS 'Таймфрейм: 1m, 5m, 15m, 1h, 4h, 1d, 1w';
COMMENT ON COLUMN analytics.analytics_chart_data.data IS 'Дополнительные данные графика в JSONB';
COMMENT ON COLUMN analytics.analytics_indicators.indicator_type IS 'Тип индикатора (MA, RSI, MACD, BollingerBands и др.)';
COMMENT ON COLUMN analytics.analytics_indicators.parameters IS 'Параметры индикатора в JSONB (например, {"period": 14, "stdDev": 2})';
COMMENT ON COLUMN analytics.analytics_indicators.signal IS 'Сигнал индикатора: buy, sell, neutral';
COMMENT ON COLUMN analytics.analytics_sentiment.symbol IS 'Символ актива (NULL для общего рынка)';
COMMENT ON COLUMN analytics.analytics_sentiment.bullish_signals IS 'Количество бычьих сигналов';
COMMENT ON COLUMN analytics.analytics_sentiment.bearish_signals IS 'Количество медвежьих сигналов';
COMMENT ON COLUMN analytics.analytics_sentiment.fear_greed_index IS 'Индекс страха и жадности (0.00-100.00)';
COMMENT ON COLUMN analytics.analytics_sentiment.sentiment_score IS 'Общий индекс настроений (-100.00 до 100.00)';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.total_value IS 'Общая стоимость портфеля';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.total_cost IS 'Общая стоимость покупки портфеля';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.total_return IS 'Общая доходность портфеля';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.total_return_percentage IS 'Процентная доходность портфеля';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.sharpe_ratio IS 'Коэффициент Шарпа (риск-скорректированная доходность)';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.win_rate IS 'Процент выигрышных сделок (0.00-100.00)';
COMMENT ON COLUMN analytics.analytics_portfolio_snapshots.profit_factor IS 'Фактор прибыли (отношение прибыли к убыткам)';
COMMENT ON COLUMN analytics.analytics_alerts.alert_type IS 'Тип оповещения: price (ценовое), event (событийное)';
COMMENT ON COLUMN analytics.analytics_alerts.symbol IS 'Символ актива для ценовых оповещений';
COMMENT ON COLUMN analytics.analytics_alerts.event_type IS 'Тип события для событийных оповещений';
COMMENT ON COLUMN analytics.analytics_alerts.condition IS 'Условие оповещения: above, below, equals, change_percentage';
COMMENT ON COLUMN analytics.analytics_alerts.target_value IS 'Целевое значение для условия оповещения';
COMMENT ON COLUMN analytics.analytics_alerts.triggered IS 'Сработало ли оповещение';
COMMENT ON COLUMN analytics.analytics_alerts.triggered_at IS 'Время срабатывания оповещения';
COMMENT ON COLUMN analytics.analytics_settings.chart_preferences IS 'Настройки графиков игрока (JSONB)';
COMMENT ON COLUMN analytics.analytics_settings.indicator_preferences IS 'Выбранные индикаторы игрока (JSONB)';
COMMENT ON COLUMN analytics.analytics_settings.alert_preferences IS 'Настройки оповещений игрока (JSONB)';


