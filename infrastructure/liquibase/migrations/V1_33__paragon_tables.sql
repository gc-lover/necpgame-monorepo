-- Issue: #1519
CREATE TABLE IF NOT EXISTS progression.paragon_levels (
  character_id UUID PRIMARY KEY,
  paragon_level INTEGER NOT NULL DEFAULT 0,
  paragon_points_total INTEGER NOT NULL DEFAULT 0,
  paragon_points_spent INTEGER NOT NULL DEFAULT 0,
  paragon_points_available INTEGER NOT NULL DEFAULT 0,
  experience_current BIGINT NOT NULL DEFAULT 0,
  experience_required BIGINT NOT NULL DEFAULT 150000,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS progression.paragon_allocations (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  stat_type VARCHAR(50) NOT NULL,
  points_allocated INTEGER NOT NULL DEFAULT 0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(character_id, stat_type),
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE,
  CHECK (stat_type IN ('strength', 'agility', 'intelligence', 'vitality', 'willpower', 'perception'))
);

CREATE INDEX IF NOT EXISTS idx_paragon_levels_level ON progression.paragon_levels(paragon_level);
CREATE INDEX IF NOT EXISTS idx_paragon_levels_points ON progression.paragon_levels(paragon_points_total);
CREATE INDEX IF NOT EXISTS idx_paragon_allocations_character ON progression.paragon_allocations(character_id);
CREATE INDEX IF NOT EXISTS idx_paragon_allocations_stat ON progression.paragon_allocations(stat_type);

