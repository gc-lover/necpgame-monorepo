-- Issue: #688
-- Create quest_definitions table for content quests

CREATE TABLE IF NOT EXISTS gameplay.quest_definitions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  quest_id VARCHAR(255) NOT NULL UNIQUE,
  title VARCHAR(500) NOT NULL,
  description TEXT,
  quest_type VARCHAR(50) NOT NULL DEFAULT 'side',
  level_min INTEGER,
  level_max INTEGER,
  requirements JSONB NOT NULL DEFAULT '{}',
  objectives JSONB NOT NULL DEFAULT '{}',
  rewards JSONB NOT NULL DEFAULT '{}',
  branches JSONB NOT NULL DEFAULT '{}',
  dialogue_id UUID,
  content_data JSONB NOT NULL DEFAULT '{}',
  version INTEGER NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_quest_definitions_quest_id ON gameplay.quest_definitions(quest_id);
CREATE INDEX IF NOT EXISTS idx_quest_definitions_type_active ON gameplay.quest_definitions(quest_type, is_active);
CREATE INDEX IF NOT EXISTS idx_quest_definitions_level_range ON gameplay.quest_definitions(level_min, level_max);


