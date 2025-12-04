CREATE SCHEMA IF NOT EXISTS progression;

CREATE TABLE IF NOT EXISTS progression.prestige_levels (
  character_id UUID PRIMARY KEY,
  bonuses_applied JSONB NOT NULL DEFAULT '{}',
  last_reset_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  prestige_level INTEGER NOT NULL DEFAULT 0,
  reset_count INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE,
  CHECK (prestige_level >= 0 AND prestige_level <= 10),
  CHECK (reset_count >= 0)
);

CREATE TABLE IF NOT EXISTS progression.prestige_resets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  bonuses_gained JSONB NOT NULL DEFAULT '{}',
  reset_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  prestige_level_before INTEGER NOT NULL,
  prestige_level_after INTEGER NOT NULL,
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_prestige_levels_level ON progression.prestige_levels(prestige_level);
CREATE INDEX IF NOT EXISTS idx_prestige_levels_reset_count ON progression.prestige_levels(reset_count);
CREATE INDEX IF NOT EXISTS idx_prestige_resets_character ON progression.prestige_resets(character_id);
CREATE INDEX IF NOT EXISTS idx_prestige_resets_reset_at ON progression.prestige_resets(reset_at);

