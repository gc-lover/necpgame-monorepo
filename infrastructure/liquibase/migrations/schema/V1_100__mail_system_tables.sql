-- Mail System Tables Migration
-- Creates all necessary tables for the mail system

-- Create mails table
CREATE TABLE IF NOT EXISTS mails (
    id UUID PRIMARY KEY,
    sender_id UUID NOT NULL,
    recipient_id UUID NOT NULL,
    subject VARCHAR(200) NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('personal', 'system', 'trade', 'guild', 'event', 'reward')),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('low', 'normal', 'high', 'urgent')),
    sent_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    read_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    folder VARCHAR(20) NOT NULL DEFAULT 'inbox' CHECK (folder IN ('inbox', 'sent', 'archived', 'trash', 'system')),
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    content JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create mail_attachments table
CREATE TABLE IF NOT EXISTS mail_attachments (
    id UUID PRIMARY KEY,
    mail_id UUID NOT NULL REFERENCES mails(id) ON DELETE CASCADE,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL CHECK (size_bytes > 0),
    data BYTEA NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create mail_reports table for moderation
CREATE TABLE IF NOT EXISTS mail_reports (
    id UUID PRIMARY KEY,
    mail_id UUID NOT NULL REFERENCES mails(id) ON DELETE CASCADE,
    reporter_id UUID NOT NULL,
    reason VARCHAR(50) NOT NULL CHECK (reason IN ('spam', 'harassment', 'inappropriate_content', 'scam', 'other')),
    description TEXT,
    severity VARCHAR(10) NOT NULL DEFAULT 'medium' CHECK (severity IN ('low', 'medium', 'high')),
    submitted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL DEFAULT 'submitted' CHECK (status IN ('submitted', 'under_review', 'resolved', 'dismissed')),
    moderator_id UUID,
    resolved_at TIMESTAMP WITH TIME ZONE,
    resolution_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_mails_recipient_id ON mails(recipient_id);
CREATE INDEX IF NOT EXISTS idx_mails_sender_id ON mails(sender_id);
CREATE INDEX IF NOT EXISTS idx_mails_sent_at ON mails(sent_at);
CREATE INDEX IF NOT EXISTS idx_mails_expires_at ON mails(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_mails_folder ON mails(folder);
CREATE INDEX IF NOT EXISTS idx_mails_category ON mails(category);
CREATE INDEX IF NOT EXISTS idx_mails_is_deleted ON mails(is_deleted);
CREATE INDEX IF NOT EXISTS idx_mails_read_status ON mails(recipient_id, read_at) WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_mail_attachments_mail_id ON mail_attachments(mail_id);
CREATE INDEX IF NOT EXISTS idx_mail_reports_mail_id ON mail_reports(mail_id);
CREATE INDEX IF NOT EXISTS idx_mail_reports_reporter_id ON mail_reports(reporter_id);
CREATE INDEX IF NOT EXISTS idx_mail_reports_status ON mail_reports(status);

-- Create composite indexes for common queries
CREATE INDEX IF NOT EXISTS idx_mails_recipient_folder ON mails(recipient_id, folder, sent_at DESC) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_mails_recipient_category ON mails(recipient_id, category, sent_at DESC) WHERE is_deleted = FALSE;

-- Add table comments
COMMENT ON TABLE mails IS 'Main table for storing mail messages in the mail system';
COMMENT ON TABLE mail_attachments IS 'Table for storing mail attachments as binary data';
COMMENT ON TABLE mail_reports IS 'Table for storing mail moderation reports';

-- Add column comments
COMMENT ON COLUMN mails.sender_id IS 'UUID of the player who sent the mail';
COMMENT ON COLUMN mails.recipient_id IS 'UUID of the player who received the mail';
COMMENT ON COLUMN mails.subject IS 'Mail subject line (max 200 characters)';
COMMENT ON COLUMN mails.category IS 'Mail category: personal, system, trade, guild, event, reward';
COMMENT ON COLUMN mails.priority IS 'Mail priority level: low, normal, high, urgent';
COMMENT ON COLUMN mails.sent_at IS 'Timestamp when the mail was sent';
COMMENT ON COLUMN mails.read_at IS 'Timestamp when the mail was read (NULL if unread)';
COMMENT ON COLUMN mails.expires_at IS 'Timestamp when the mail expires (NULL for no expiration)';
COMMENT ON COLUMN mails.folder IS 'Current folder: inbox, sent, archived, trash, system';
COMMENT ON COLUMN mails.is_archived IS 'Whether the mail has been archived by the recipient';
COMMENT ON COLUMN mails.is_deleted IS 'Whether the mail has been deleted (soft delete)';
COMMENT ON COLUMN mails.content IS 'JSON content of the mail including text, HTML, and format';

COMMENT ON COLUMN mail_attachments.filename IS 'Original filename of the attachment';
COMMENT ON COLUMN mail_attachments.content_type IS 'MIME content type of the attachment';
COMMENT ON COLUMN mail_attachments.size_bytes IS 'Size of the attachment in bytes';
COMMENT ON COLUMN mail_attachments.data IS 'Binary data of the attachment';

COMMENT ON COLUMN mail_reports.reason IS 'Reason for reporting: spam, harassment, inappropriate_content, scam, other';
COMMENT ON COLUMN mail_reports.severity IS 'Severity level: low, medium, high';
COMMENT ON COLUMN mail_reports.status IS 'Report status: submitted, under_review, resolved, dismissed';

-- Create trigger for updating updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_mails_updated_at BEFORE UPDATE ON mails
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_mail_reports_updated_at BEFORE UPDATE ON mail_reports
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();


















