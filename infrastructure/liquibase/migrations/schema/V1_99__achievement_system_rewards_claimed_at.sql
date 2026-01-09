-- Achievement System Rewards Claimed At Migration
-- Adds rewards_claimed_at column to track when achievement rewards were claimed

-- Add rewards_claimed_at column to player_achievements table
ALTER TABLE player_achievements
ADD COLUMN rewards_claimed_at TIMESTAMP WITH TIME ZONE;

-- Add index for efficient queries on claimed rewards
CREATE INDEX IF NOT EXISTS idx_player_achievements_rewards_claimed_at
ON player_achievements(rewards_claimed_at) WHERE rewards_claimed_at IS NOT NULL;

-- Add comment for documentation
COMMENT ON COLUMN player_achievements.rewards_claimed_at IS 'Timestamp when achievement rewards were claimed by the player';























