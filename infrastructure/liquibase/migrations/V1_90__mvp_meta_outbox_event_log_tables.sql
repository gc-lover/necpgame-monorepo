-- Issue: #142102432
-- MVP Meta - Outbox and Event Log Tables
-- Создание таблиц для Outbox Pattern и Event Log:
-- - mvp_meta.outbox (Outbox Pattern для гарантированной доставки событий)
-- - mvp_meta.event_log (журнал событий)

-- Создание схемы mvp_meta, если её нет (уже создана в V1_0, но для безопасности)
CREATE SCHEMA IF NOT EXISTS mvp_meta;

-- Таблица Outbox Pattern
CREATE TABLE IF NOT EXISTS mvp_meta.outbox (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  aggregate_type VARCHAR(100) NOT NULL,
  aggregate_id VARCHAR(100) NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  payload JSONB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  processed_at TIMESTAMP,
  status VARCHAR(50) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'processing', 'processed', 'failed')),
  retry_count INTEGER NOT NULL DEFAULT 0 CHECK (retry_count >= 0),
  error_message TEXT
);

-- Индексы для outbox
CREATE INDEX IF NOT EXISTS idx_outbox_status_created_at 
    ON mvp_meta.outbox(status, created_at) WHERE status = 'pending';
CREATE INDEX IF NOT EXISTS idx_outbox_aggregate 
    ON mvp_meta.outbox(aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS idx_outbox_event_type 
    ON mvp_meta.outbox(event_type);
CREATE INDEX IF NOT EXISTS idx_outbox_created_at 
    ON mvp_meta.outbox(created_at);

-- Таблица Event Log
CREATE TABLE IF NOT EXISTS mvp_meta.event_log (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  event_type VARCHAR(100) NOT NULL,
  aggregate_type VARCHAR(100),
  aggregate_id VARCHAR(100),
  payload JSONB NOT NULL,
  metadata JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для event_log
CREATE INDEX IF NOT EXISTS idx_event_log_event_type 
    ON mvp_meta.event_log(event_type);
CREATE INDEX IF NOT EXISTS idx_event_log_aggregate 
    ON mvp_meta.event_log(aggregate_type, aggregate_id) WHERE aggregate_type IS NOT NULL AND aggregate_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_event_log_created_at 
    ON mvp_meta.event_log(created_at);

-- Комментарии к таблицам
COMMENT ON TABLE mvp_meta.outbox IS 'Outbox Pattern для гарантированной доставки событий через Event Bus';
COMMENT ON TABLE mvp_meta.event_log IS 'Журнал событий для аудита и аналитики';
COMMENT ON COLUMN mvp_meta.outbox.processed_at IS 'Время обработки события';
COMMENT ON COLUMN mvp_meta.outbox.status IS 'Статус обработки: pending, processing, processed, failed';
COMMENT ON COLUMN mvp_meta.outbox.retry_count IS 'Количество попыток обработки (0+)';
COMMENT ON COLUMN mvp_meta.outbox.error_message IS 'Сообщение об ошибке при обработке';

