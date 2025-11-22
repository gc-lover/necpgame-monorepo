CREATE TABLE IF NOT EXISTS mvp_core.player_sessions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id VARCHAR(100) NOT NULL,
  token VARCHAR(255) NOT NULL UNIQUE,
  reconnect_token VARCHAR(255) NOT NULL UNIQUE,
  status VARCHAR(20) NOT NULL DEFAULT 'created',
  server_id VARCHAR(100) NOT NULL,
  ip_address VARCHAR(45),
  user_agent TEXT,
  character_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
  last_heartbeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  disconnect_count INT NOT NULL DEFAULT 0,
  deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_player_sessions_player_id 
  ON mvp_core.player_sessions(player_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_player_sessions_token 
  ON mvp_core.player_sessions(token) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_player_sessions_reconnect_token 
  ON mvp_core.player_sessions(reconnect_token) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_player_sessions_status 
  ON mvp_core.player_sessions(status, last_heartbeat) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_player_sessions_server_id 
  ON mvp_core.player_sessions(server_id, status) WHERE deleted_at IS NULL AND status = 'active';

CREATE INDEX IF NOT EXISTS idx_player_sessions_last_heartbeat 
  ON mvp_core.player_sessions(last_heartbeat) WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS mvp_core.session_audit_log (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  session_id UUID REFERENCES mvp_core.player_sessions(id) ON DELETE CASCADE,
  player_id VARCHAR(100) NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  old_status VARCHAR(20),
  new_status VARCHAR(20),
  metadata JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_session_audit_log_session_id 
  ON mvp_core.session_audit_log(session_id);

CREATE INDEX IF NOT EXISTS idx_session_audit_log_player_id 
  ON mvp_core.session_audit_log(player_id);

CREATE INDEX IF NOT EXISTS idx_session_audit_log_event_type 
  ON mvp_core.session_audit_log(event_type, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_session_audit_log_created_at 
  ON mvp_core.session_audit_log(created_at DESC);

