CREATE TABLE IF NOT EXISTS mvp_core.character_positions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  position_x DECIMAL(10, 2) NOT NULL DEFAULT 0,
  position_y DECIMAL(10, 2) NOT NULL DEFAULT 0,
  position_z DECIMAL(10, 2) NOT NULL DEFAULT 0,
  yaw DECIMAL(10, 2) NOT NULL DEFAULT 0,
  velocity_x DECIMAL(10, 2) DEFAULT 0,
  velocity_y DECIMAL(10, 2) DEFAULT 0,
  velocity_z DECIMAL(10, 2) DEFAULT 0
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_character_positions_character 
  ON mvp_core.character_positions(character_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_character_positions_character_id ON mvp_core.character_positions(character_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_positions_updated_at ON mvp_core.character_positions(updated_at) WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS mvp_core.character_position_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  position_x DECIMAL(10, 2) NOT NULL,
  position_y DECIMAL(10, 2) NOT NULL,
  position_z DECIMAL(10, 2) NOT NULL,
  yaw DECIMAL(10, 2) NOT NULL,
  velocity_x DECIMAL(10, 2) DEFAULT 0,
  velocity_y DECIMAL(10, 2) DEFAULT 0,
  velocity_z DECIMAL(10, 2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_character_position_history_character_created ON mvp_core.character_position_history(character_id, created_at DESC);
