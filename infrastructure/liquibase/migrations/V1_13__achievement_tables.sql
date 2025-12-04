CREATE TABLE IF NOT EXISTS mvp_core.achievements (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  season_id UUID,
  description TEXT NOT NULL,
  code VARCHAR(100) NOT NULL UNIQUE,
  type VARCHAR(50) NOT NULL DEFAULT 'one_time',
  category VARCHAR(50) NOT NULL,
  rarity VARCHAR(50) NOT NULL DEFAULT 'common',
  title VARCHAR(200) NOT NULL,
  conditions JSONB NOT NULL DEFAULT '{}',
  rewards JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  points INT NOT NULL DEFAULT 0,
  is_hidden BOOLEAN NOT NULL DEFAULT false,
  is_seasonal BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS mvp_core.player_achievements (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  achievement_id UUID NOT NULL REFERENCES mvp_core.achievements(id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'locked',
  progress_data JSONB DEFAULT '{}',
  unlocked_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  progress INT NOT NULL DEFAULT 0,
  progress_max INT NOT NULL DEFAULT 0,
  UNIQUE(player_id, achievement_id)
);

CREATE INDEX IF NOT EXISTS idx_achievements_category 
  ON mvp_core.achievements(category);

CREATE INDEX IF NOT EXISTS idx_achievements_type 
  ON mvp_core.achievements(type);

CREATE INDEX IF NOT EXISTS idx_achievements_rarity 
  ON mvp_core.achievements(rarity);

CREATE INDEX IF NOT EXISTS idx_achievements_seasonal 
  ON mvp_core.achievements(is_seasonal, season_id) WHERE is_seasonal = true;

CREATE INDEX IF NOT EXISTS idx_player_achievements_player 
  ON mvp_core.player_achievements(player_id, status, updated_at DESC);

CREATE INDEX IF NOT EXISTS idx_player_achievements_achievement 
  ON mvp_core.player_achievements(achievement_id, status);

CREATE INDEX IF NOT EXISTS idx_player_achievements_unlocked 
  ON mvp_core.player_achievements(player_id, unlocked_at DESC) WHERE status = 'unlocked';

