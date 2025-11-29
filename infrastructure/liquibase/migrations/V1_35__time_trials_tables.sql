CREATE TABLE IF NOT EXISTS gameplay.time_trial_sessions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  trial_type VARCHAR(50) NOT NULL,
  content_id UUID NOT NULL,
  player_id UUID NOT NULL,
  team_id UUID,
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP,
  completion_time_ms INTEGER,
  deaths_count INTEGER DEFAULT 0,
  abilities_used TEXT[],
  route_optimization DECIMAL(5,2),
  status VARCHAR(20) NOT NULL DEFAULT 'in_progress',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CHECK (trial_type IN ('speedrun_raid', 'time_attack_dungeon', 'weekly_challenge')),
  CHECK (status IN ('in_progress', 'completed', 'failed'))
);

CREATE TABLE IF NOT EXISTS gameplay.weekly_time_challenges (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  week_start TIMESTAMP NOT NULL,
  week_end TIMESTAMP NOT NULL,
  challenge_type VARCHAR(50) NOT NULL,
  content_id UUID NOT NULL,
  time_limit_ms INTEGER NOT NULL,
  conditions JSONB,
  rewards JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(week_start, week_end),
  CHECK (challenge_type IN ('solo_challenge', 'team_challenge', 'class_challenge'))
);

CREATE TABLE IF NOT EXISTS gameplay.time_trial_records (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  trial_type VARCHAR(50) NOT NULL,
  content_id UUID NOT NULL,
  player_id UUID NOT NULL,
  team_id UUID,
  session_id UUID NOT NULL,
  completion_time_ms INTEGER NOT NULL,
  rank INTEGER,
  is_personal_best BOOLEAN DEFAULT false,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (session_id) REFERENCES gameplay.time_trial_sessions(id) ON DELETE CASCADE,
  CHECK (trial_type IN ('speedrun_raid', 'time_attack_dungeon', 'weekly_challenge'))
);

CREATE INDEX IF NOT EXISTS idx_time_trial_sessions_player ON gameplay.time_trial_sessions(player_id);
CREATE INDEX IF NOT EXISTS idx_time_trial_sessions_content ON gameplay.time_trial_sessions(content_id);
CREATE INDEX IF NOT EXISTS idx_time_trial_sessions_status ON gameplay.time_trial_sessions(status);
CREATE INDEX IF NOT EXISTS idx_time_trial_sessions_type ON gameplay.time_trial_sessions(trial_type);
CREATE INDEX IF NOT EXISTS idx_weekly_challenges_week_start ON gameplay.weekly_time_challenges(week_start);
CREATE INDEX IF NOT EXISTS idx_weekly_challenges_week_end ON gameplay.weekly_time_challenges(week_end);
CREATE INDEX IF NOT EXISTS idx_time_trial_records_player ON gameplay.time_trial_records(player_id);
CREATE INDEX IF NOT EXISTS idx_time_trial_records_content ON gameplay.time_trial_records(content_id);
CREATE INDEX IF NOT EXISTS idx_time_trial_records_type ON gameplay.time_trial_records(trial_type);
CREATE INDEX IF NOT EXISTS idx_time_trial_records_rank ON gameplay.time_trial_records(rank);

