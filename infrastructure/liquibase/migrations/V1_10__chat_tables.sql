CREATE SCHEMA IF NOT EXISTS social;

CREATE TABLE IF NOT EXISTS social.chat_channels (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  owner_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
  description TEXT,
  type VARCHAR(50) NOT NULL,
  name VARCHAR(200) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  cooldown_seconds INT NOT NULL DEFAULT 0,
  max_length INT NOT NULL DEFAULT 500,
  is_active BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX IF NOT EXISTS idx_chat_channels_type 
  ON social.chat_channels(type) WHERE deleted_at IS NULL AND is_active = true;

CREATE INDEX IF NOT EXISTS idx_chat_channels_owner 
  ON social.chat_channels(owner_id) WHERE deleted_at IS NULL AND owner_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_chat_channels_active 
  ON social.chat_channels(is_active) WHERE deleted_at IS NULL AND is_active = true;

CREATE TABLE IF NOT EXISTS social.chat_messages (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  channel_id UUID NOT NULL REFERENCES social.chat_channels(id) ON DELETE CASCADE,
  sender_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  content TEXT NOT NULL,
  formatted TEXT,
  channel_type VARCHAR(50) NOT NULL,
  sender_name VARCHAR(200) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_chat_messages_channel 
  ON social.chat_messages(channel_id, created_at DESC) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_chat_messages_sender 
  ON social.chat_messages(sender_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_chat_messages_channel_type 
  ON social.chat_messages(channel_type, created_at DESC) WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS social.chat_bans (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  channel_id UUID REFERENCES social.chat_channels(id) ON DELETE CASCADE,
  admin_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
  reason TEXT NOT NULL,
  channel_type VARCHAR(50),
  expires_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX IF NOT EXISTS idx_chat_bans_character 
  ON social.chat_bans(character_id, is_active) WHERE is_active = true;

CREATE INDEX IF NOT EXISTS idx_chat_bans_expires 
  ON social.chat_bans(expires_at) WHERE is_active = true AND expires_at IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_chat_bans_channel 
  ON social.chat_bans(channel_id) WHERE is_active = true;

INSERT INTO social.chat_channels (id, type, name, description, cooldown_seconds, max_length, is_active) VALUES
  (gen_random_uuid(), 'global', 'Global Chat', 'Server-wide chat channel', 5, 500, true),
  (gen_random_uuid(), 'trade', 'Trade Chat', 'Trading channel', 3, 200, true),
  (gen_random_uuid(), 'newbie', 'Newbie Chat', 'Help channel for new players', 2, 500, true),
  (gen_random_uuid(), 'local', 'Local Chat', 'Zone-based chat', 1, 500, true),
  (gen_random_uuid(), 'system', 'System Messages', 'System notifications', 0, 1000, true)
ON CONFLICT DO NOTHING;

