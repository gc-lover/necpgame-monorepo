-- Issue: #2263 - Guild Voice Channels Database Schema
-- liquibase formatted sql

--changeset backend:guild-voice-channels-schema dbms:postgresql
--comment: Create schema for guild voice channels with WebRTC integration

BEGIN;

-- Create guild_voice_channels table
CREATE TABLE IF NOT EXISTS social.guild_voice_channels (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    channel_id VARCHAR(100) NOT NULL UNIQUE, -- WebRTC signaling channel ID
    max_users INTEGER NOT NULL DEFAULT 10 CHECK (max_users >= 1 AND max_users <= 50),
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'maintenance')),

    FOREIGN KEY (guild_id) REFERENCES social.guilds(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES social.guild_members(user_id) ON DELETE CASCADE
);

-- Create guild_voice_participants table
CREATE TABLE IF NOT EXISTS social.guild_voice_participants (
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    guild_id UUID NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_muted BOOLEAN NOT NULL DEFAULT FALSE,
    is_deafened BOOLEAN NOT NULL DEFAULT FALSE,
    webrtc_id VARCHAR(100) NOT NULL, -- WebRTC participant ID

    PRIMARY KEY (user_id, channel_id),
    FOREIGN KEY (channel_id) REFERENCES social.guild_voice_channels(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES social.guild_members(user_id) ON DELETE CASCADE,
    FOREIGN KEY (guild_id) REFERENCES social.guilds(id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_guild_voice_channels_guild_id ON social.guild_voice_channels(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_voice_channels_status ON social.guild_voice_channels(status);
CREATE INDEX IF NOT EXISTS idx_guild_voice_channels_created_at ON social.guild_voice_channels(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_guild_voice_participants_channel_id ON social.guild_voice_participants(channel_id);
CREATE INDEX IF NOT EXISTS idx_guild_voice_participants_user_id ON social.guild_voice_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_guild_voice_participants_guild_id ON social.guild_voice_participants(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_voice_participants_joined_at ON social.guild_voice_participants(joined_at DESC);

-- Create trigger for updated_at on voice channels
CREATE OR REPLACE FUNCTION update_guild_voice_channels_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_guild_voice_channels_updated_at
    BEFORE UPDATE ON social.guild_voice_channels
    FOR EACH ROW EXECUTE FUNCTION update_guild_voice_channels_updated_at();

COMMIT;
