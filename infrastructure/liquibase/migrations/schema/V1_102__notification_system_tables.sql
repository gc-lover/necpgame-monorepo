-- Notification System Tables Migration
-- Creates all necessary tables for the notification system

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('system', 'achievement', 'quest', 'social', 'combat', 'economy', 'event')),
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    data JSONB,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    read_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(10) NOT NULL DEFAULT 'normal' CHECK (priority IN ('low', 'normal', 'high', 'urgent')),

    -- Indexes for performance
    CONSTRAINT fk_notifications_player_id FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);

-- Create indexes for efficient queries
CREATE INDEX IF NOT EXISTS idx_notifications_player_id ON notifications(player_id);
CREATE INDEX IF NOT EXISTS idx_notifications_player_id_created_at ON notifications(player_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_notifications_player_id_is_read ON notifications(player_id, is_read);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(type);
CREATE INDEX IF NOT EXISTS idx_notifications_expires_at ON notifications(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_notifications_priority ON notifications(priority);

-- Add comments for documentation
COMMENT ON TABLE notifications IS 'Player notifications and messages';
COMMENT ON COLUMN notifications.id IS 'Unique notification identifier';
COMMENT ON COLUMN notifications.player_id IS 'Player who receives the notification';
COMMENT ON COLUMN notifications.type IS 'Notification type (system, achievement, quest, etc.)';
COMMENT ON COLUMN notifications.title IS 'Notification title';
COMMENT ON COLUMN notifications.message IS 'Notification message content';
COMMENT ON COLUMN notifications.data IS 'Additional JSON data for the notification';
COMMENT ON COLUMN notifications.is_read IS 'Whether the notification has been read';
COMMENT ON COLUMN notifications.created_at IS 'When the notification was created';
COMMENT ON COLUMN notifications.read_at IS 'When the notification was read';
COMMENT ON COLUMN notifications.expires_at IS 'When the notification expires (optional)';
COMMENT ON COLUMN notifications.priority IS 'Notification priority level';

