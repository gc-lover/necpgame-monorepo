CREATE SCHEMA IF NOT EXISTS gameplay;

CREATE TABLE IF NOT EXISTS gameplay.quest_instances (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  quest_id VARCHAR(100) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'not_started',
  current_node VARCHAR(100) NOT NULL DEFAULT 'start',
  dialogue_state JSONB NOT NULL DEFAULT '{}',
  objectives JSONB NOT NULL DEFAULT '{}',
  started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS gameplay.dialogue_state (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  quest_instance_id UUID NOT NULL,
  character_id UUID NOT NULL,
  visited_nodes TEXT[] NOT NULL DEFAULT '{}',
  current_node VARCHAR(100) NOT NULL,
  choices JSONB NOT NULL DEFAULT '{}',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (quest_instance_id) REFERENCES gameplay.quest_instances(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS gameplay.skill_check_results (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  quest_instance_id UUID NOT NULL,
  character_id UUID NOT NULL,
  skill_id VARCHAR(100) NOT NULL,
  required_level INTEGER NOT NULL,
  actual_level INTEGER NOT NULL,
  passed BOOLEAN NOT NULL,
  checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (quest_instance_id) REFERENCES gameplay.quest_instances(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_quest_instances_character ON gameplay.quest_instances(character_id, status);
CREATE INDEX IF NOT EXISTS idx_quest_instances_quest ON gameplay.quest_instances(quest_id, status);
CREATE INDEX IF NOT EXISTS idx_dialogue_state_quest_instance ON gameplay.dialogue_state(quest_instance_id);
CREATE INDEX IF NOT EXISTS idx_skill_check_results_quest_instance ON gameplay.skill_check_results(quest_instance_id);

