-- Issue: #1499

CREATE TABLE IF NOT EXISTS gameplay.restricted_mode_sessions (
  id UUID PRIMARY KEY,
  character_id UUID NOT NULL UNIQUE,
  mode_type VARCHAR(32) NOT NULL,
  state VARCHAR(16) NOT NULL,
  restrictions JSONB,
  started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP WITH TIME ZONE,
  CHECK (mode_type IN ('IRONMAN', 'HARDCORE', 'SOLO', 'NODEATH')),
  CHECK (state IN ('ACTIVE', 'COMPLETED', 'FAILED', 'INACTIVE'))
);

CREATE TABLE IF NOT EXISTS gameplay.ironman_characters (
  id UUID PRIMARY KEY,
  character_id UUID NOT NULL UNIQUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  died_at TIMESTAMP WITH TIME ZONE,
  achievements JSONB
);

CREATE INDEX IF NOT EXISTS idx_restricted_mode_sessions_character ON gameplay.restricted_mode_sessions(character_id);
CREATE INDEX IF NOT EXISTS idx_restricted_mode_sessions_state ON gameplay.restricted_mode_sessions(state);
CREATE INDEX IF NOT EXISTS idx_restricted_mode_sessions_mode ON gameplay.restricted_mode_sessions(mode_type);

