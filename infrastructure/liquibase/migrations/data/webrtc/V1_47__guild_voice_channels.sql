-- Liquibase formatted SQL
-- Changeset: V1_47__guild_voice_channels
-- Description: Add guild-specific voice channel support to WebRTC signaling service
-- Issue: #2263

-- Add new columns to voice_channels table for guild support
ALTER TABLE voice_channels
ADD COLUMN IF NOT EXISTS is_default_guild_channel BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS guild_permissions JSONB;

-- Create index for guild voice channels
CREATE INDEX IF NOT EXISTS idx_voice_channels_guild_id ON voice_channels(guild_id) WHERE type = 'guild';
CREATE INDEX IF NOT EXISTS idx_voice_channels_guild_default ON voice_channels(guild_id, is_default_guild_channel) WHERE type = 'guild';

-- Update existing guild channels to have default permissions if they don't have any
UPDATE voice_channels
SET guild_permissions = '{
  "allowed_roles": ["leader", "officer", "member"],
  "blocked_users": [],
  "muted_users": [],
  "deafened_users": [],
  "is_moderated": false,
  "require_approval": false
}'::jsonb
WHERE type = 'guild' AND guild_permissions IS NULL;

-- Add comments for documentation
COMMENT ON COLUMN voice_channels.is_default_guild_channel IS 'Indicates if this is the default voice channel for a guild';
COMMENT ON COLUMN voice_channels.guild_permissions IS 'Guild-specific permissions for voice channel access and moderation';

-- Insert sample guild voice channels for testing (optional)
-- These will be created by the application, this is just for initial data
INSERT INTO voice_channels (
    id, name, type, guild_id, owner_id, max_users, current_users,
    is_active, created_at, updated_at, is_default_guild_channel, guild_permissions
) VALUES
    (
        gen_random_uuid(),
        'General Voice',
        'guild',
        (SELECT id FROM guilds LIMIT 1), -- Replace with actual guild ID
        (SELECT leader_id FROM guilds LIMIT 1), -- Replace with actual leader ID
        50, 0, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true,
        '{
            "allowed_roles": ["leader", "officer", "member"],
            "blocked_users": [],
            "muted_users": [],
            "deafened_users": [],
            "is_moderated": false,
            "require_approval": false
        }'::jsonb
    )
ON CONFLICT DO NOTHING;

-- Rollback section (for Liquibase)
-- rollback ALTER TABLE voice_channels DROP COLUMN IF EXISTS is_default_guild_channel;
-- rollback ALTER TABLE voice_channels DROP COLUMN IF EXISTS guild_permissions;
-- rollback DROP INDEX IF EXISTS idx_voice_channels_guild_id;
-- rollback DROP INDEX IF EXISTS idx_voice_channels_guild_default;
