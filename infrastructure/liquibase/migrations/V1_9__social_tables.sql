CREATE TABLE IF NOT EXISTS mvp_core.notifications (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  account_id UUID NOT NULL REFERENCES mvp_core.player_account(id) ON DELETE CASCADE,
  type VARCHAR(50) NOT NULL,
  priority VARCHAR(20) NOT NULL DEFAULT 'medium',
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  data JSONB,
  status VARCHAR(20) NOT NULL DEFAULT 'unread',
  channels JSONB NOT NULL DEFAULT '["in_game"]'::jsonb,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  read_at TIMESTAMP,
  expires_at TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_notifications_account_id 
  ON mvp_core.notifications(account_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_notifications_status 
  ON mvp_core.notifications(account_id, status) WHERE deleted_at IS NULL AND expires_at IS NULL OR expires_at > NOW();

CREATE INDEX IF NOT EXISTS idx_notifications_type 
  ON mvp_core.notifications(account_id, type) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_notifications_created_at 
  ON mvp_core.notifications(account_id, created_at DESC) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_notifications_expires_at 
  ON mvp_core.notifications(expires_at) WHERE expires_at IS NOT NULL AND expires_at < NOW();

CREATE TABLE IF NOT EXISTS mvp_core.notification_preferences (
  account_id UUID PRIMARY KEY REFERENCES mvp_core.player_account(id) ON DELETE CASCADE,
  quest_enabled BOOLEAN NOT NULL DEFAULT true,
  message_enabled BOOLEAN NOT NULL DEFAULT true,
  achievement_enabled BOOLEAN NOT NULL DEFAULT true,
  system_enabled BOOLEAN NOT NULL DEFAULT true,
  friend_enabled BOOLEAN NOT NULL DEFAULT true,
  guild_enabled BOOLEAN NOT NULL DEFAULT true,
  trade_enabled BOOLEAN NOT NULL DEFAULT true,
  combat_enabled BOOLEAN NOT NULL DEFAULT true,
  preferred_channels JSONB NOT NULL DEFAULT '["in_game"]'::jsonb,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.friendships (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_a_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  character_b_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  initiator_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  CONSTRAINT chk_character_order CHECK (character_a_id < character_b_id),
  CONSTRAINT uq_friendship_pair UNIQUE (character_a_id, character_b_id) WHERE deleted_at IS NULL
);

CREATE INDEX IF NOT EXISTS idx_friendships_character_a 
  ON mvp_core.friendships(character_a_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_friendships_character_b 
  ON mvp_core.friendships(character_b_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_friendships_status 
  ON mvp_core.friendships(character_a_id, status) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_friendships_initiator 
  ON mvp_core.friendships(initiator_id) WHERE deleted_at IS NULL;

