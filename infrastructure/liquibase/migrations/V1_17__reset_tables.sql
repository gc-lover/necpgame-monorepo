CREATE SCHEMA IF NOT EXISTS operations;

CREATE TABLE IF NOT EXISTS operations.reset_records (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  error TEXT,
  metadata JSONB NOT NULL DEFAULT '{}'
);

CREATE INDEX IF NOT EXISTS idx_reset_records_type ON operations.reset_records(type, started_at DESC);
CREATE INDEX IF NOT EXISTS idx_reset_records_status ON operations.reset_records(status, started_at DESC);
CREATE INDEX IF NOT EXISTS idx_reset_records_completed ON operations.reset_records(type, completed_at DESC) WHERE status = 'completed';

