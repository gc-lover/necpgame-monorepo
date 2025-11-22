CREATE TABLE IF NOT EXISTS social.voice_channels (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  type VARCHAR(50) NOT NULL,
  owner_id UUID NOT NULL,
  owner_type VARCHAR(50) NOT NULL DEFAULT 'character',
  name VARCHAR(255) NOT NULL,
  max_members INTEGER NOT NULL,
  quality_preset VARCHAR(50) NOT NULL DEFAULT 'standard',
  settings JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS social.voice_participants (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  channel_id UUID NOT NULL REFERENCES social.voice_channels(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'connected',
  webrtc_token TEXT,
  position JSONB NOT NULL DEFAULT '{}',
  stats JSONB NOT NULL DEFAULT '{}',
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(channel_id, character_id)
);

CREATE INDEX IF NOT EXISTS idx_voice_channels_type ON social.voice_channels(type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_voice_channels_owner ON social.voice_channels(owner_id, owner_type);
CREATE INDEX IF NOT EXISTS idx_voice_participants_channel ON social.voice_participants(channel_id, joined_at);
CREATE INDEX IF NOT EXISTS idx_voice_participants_character ON social.voice_participants(character_id);
CREATE INDEX IF NOT EXISTS idx_voice_participants_status ON social.voice_participants(channel_id, status);

