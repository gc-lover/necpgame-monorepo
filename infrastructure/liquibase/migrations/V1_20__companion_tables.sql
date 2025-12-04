CREATE TABLE IF NOT EXISTS gameplay.companion_types (
  id VARCHAR(100) PRIMARY KEY,
  description TEXT,
  abilities TEXT[] NOT NULL DEFAULT '{}',
  category VARCHAR(20) NOT NULL,
  name VARCHAR(100) NOT NULL,
  stats JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  cost BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS gameplay.player_companions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  companion_type_id VARCHAR(100) NOT NULL,
  custom_name VARCHAR(100),
  status VARCHAR(20) NOT NULL DEFAULT 'owned',
  equipment JSONB NOT NULL DEFAULT '{}',
  stats JSONB NOT NULL DEFAULT '{}',
  summoned_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  experience BIGINT NOT NULL DEFAULT 0,
  level INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (companion_type_id) REFERENCES gameplay.companion_types(id)
);

CREATE TABLE IF NOT EXISTS gameplay.companion_abilities (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_companion_id UUID NOT NULL,
  ability_id VARCHAR(100) NOT NULL,
  cooldown_until TIMESTAMP,
  last_used_at TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN NOT NULL DEFAULT true,
  FOREIGN KEY (player_companion_id) REFERENCES gameplay.player_companions(id) ON DELETE CASCADE,
  UNIQUE(player_companion_id, ability_id)
);

CREATE INDEX IF NOT EXISTS idx_companion_types_category ON gameplay.companion_types(category);
CREATE INDEX IF NOT EXISTS idx_player_companions_character ON gameplay.player_companions(character_id, status);
CREATE INDEX IF NOT EXISTS idx_player_companions_type ON gameplay.player_companions(companion_type_id);
CREATE INDEX IF NOT EXISTS idx_companion_abilities_companion ON gameplay.companion_abilities(player_companion_id);

