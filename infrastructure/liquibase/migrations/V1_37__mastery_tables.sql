CREATE SCHEMA IF NOT EXISTS progression;

CREATE TABLE IF NOT EXISTS progression.mastery_levels (
  character_id UUID NOT NULL,
  mastery_type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  experience_current BIGINT NOT NULL DEFAULT 0,
  experience_required BIGINT NOT NULL DEFAULT 1000,
  total_experience_earned BIGINT NOT NULL DEFAULT 0,
  mastery_level INTEGER NOT NULL DEFAULT 0,
  completions_count INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (character_id, mastery_type),
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE,
  CHECK (mastery_level >= 0 AND mastery_level <= 100),
  CHECK (mastery_type IN ('raid', 'dungeon', 'world_boss', 'pvp', 'exploration')),
  CHECK (experience_current >= 0),
  CHECK (experience_required > 0),
  CHECK (total_experience_earned >= 0),
  CHECK (completions_count >= 0)
);

CREATE TABLE IF NOT EXISTS progression.mastery_rewards (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  mastery_type VARCHAR(50) NOT NULL,
  reward_type VARCHAR(50) NOT NULL,
  reward_id VARCHAR(255) NOT NULL,
  unlocked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  reward_level INTEGER NOT NULL,
  FOREIGN KEY (character_id) REFERENCES character.characters(id) ON DELETE CASCADE,
  CHECK (mastery_type IN ('raid', 'dungeon', 'world_boss', 'pvp', 'exploration')),
  CHECK (reward_level >= 0 AND reward_level <= 100),
  UNIQUE(character_id, mastery_type, reward_level, reward_id)
);

CREATE INDEX IF NOT EXISTS idx_mastery_levels_character ON progression.mastery_levels(character_id);
CREATE INDEX IF NOT EXISTS idx_mastery_levels_type ON progression.mastery_levels(mastery_type);
CREATE INDEX IF NOT EXISTS idx_mastery_levels_level ON progression.mastery_levels(mastery_level);
CREATE INDEX IF NOT EXISTS idx_mastery_rewards_character ON progression.mastery_rewards(character_id);
CREATE INDEX IF NOT EXISTS idx_mastery_rewards_type ON progression.mastery_rewards(mastery_type);
CREATE INDEX IF NOT EXISTS idx_mastery_rewards_level ON progression.mastery_rewards(reward_level);

