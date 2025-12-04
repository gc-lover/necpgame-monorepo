CREATE SCHEMA IF NOT EXISTS economy;

CREATE TABLE IF NOT EXISTS economy.trade_sessions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  initiator_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  recipient_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  zone_id UUID,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  initiator_offer JSONB NOT NULL DEFAULT '{"items": [],
  recipient_offer JSONB NOT NULL DEFAULT '{"items": [],
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  completed_at TIMESTAMP,
  initiator_confirmed BOOLEAN NOT NULL DEFAULT false,
  recipient_confirmed BOOLEAN NOT NULL DEFAULT false,
  "currency": {}}',
  "currency": {}}'
);

CREATE TABLE IF NOT EXISTS economy.trade_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  trade_session_id UUID NOT NULL,
  initiator_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  recipient_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  zone_id UUID,
  status VARCHAR(20) NOT NULL,
  initiator_offer JSONB NOT NULL,
  recipient_offer JSONB NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_trade_sessions_initiator ON economy.trade_sessions(initiator_id, status);
CREATE INDEX IF NOT EXISTS idx_trade_sessions_recipient ON economy.trade_sessions(recipient_id, status);
CREATE INDEX IF NOT EXISTS idx_trade_sessions_status ON economy.trade_sessions(status, expires_at);
CREATE INDEX IF NOT EXISTS idx_trade_sessions_expires ON economy.trade_sessions(expires_at) WHERE status IN ('pending', 'active', 'confirmed');

CREATE INDEX IF NOT EXISTS idx_trade_history_initiator ON economy.trade_history(initiator_id, completed_at DESC);
CREATE INDEX IF NOT EXISTS idx_trade_history_recipient ON economy.trade_history(recipient_id, completed_at DESC);
CREATE INDEX IF NOT EXISTS idx_trade_history_session ON economy.trade_history(trade_session_id);

