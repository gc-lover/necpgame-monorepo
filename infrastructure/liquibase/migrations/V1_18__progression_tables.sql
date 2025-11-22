CREATE SCHEMA IF NOT EXISTS progression;

CREATE TABLE IF NOT EXISTS progression.character_progression (
  character_id UUID PRIMARY KEY,
  level INTEGER NOT NULL DEFAULT 1,
  experience BIGINT NOT NULL DEFAULT 0,
  experience_to_next BIGINT NOT NULL DEFAULT 100,
  attribute_points INTEGER NOT NULL DEFAULT 0,
  skill_points INTEGER NOT NULL DEFAULT 0,
  attributes JSONB NOT NULL DEFAULT '{}',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS progression.skill_experience (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  skill_id VARCHAR(100) NOT NULL,
  level INTEGER NOT NULL DEFAULT 1,
  experience BIGINT NOT NULL DEFAULT 0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(character_id, skill_id)
);

CREATE INDEX IF NOT EXISTS idx_character_progression_level ON progression.character_progression(level);
CREATE INDEX IF NOT EXISTS idx_skill_experience_character ON progression.skill_experience(character_id);
CREATE INDEX IF NOT EXISTS idx_skill_experience_skill ON progression.skill_experience(skill_id);

