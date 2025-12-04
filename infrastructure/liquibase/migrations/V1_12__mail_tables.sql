CREATE TABLE IF NOT EXISTS social.mail_messages (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  sender_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
  recipient_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  content TEXT NOT NULL,
  sender_name VARCHAR(200) NOT NULL,
  type VARCHAR(50) NOT NULL DEFAULT 'player',
  subject VARCHAR(500) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'unread',
  attachments JSONB,
  sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  read_at TIMESTAMP,
  expires_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  cod_amount INT,
  is_read BOOLEAN NOT NULL DEFAULT false,
  is_claimed BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX IF NOT EXISTS idx_mail_messages_recipient 
  ON social.mail_messages(recipient_id, sent_at DESC) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_mail_messages_sender 
  ON social.mail_messages(sender_id) WHERE deleted_at IS NULL AND sender_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_mail_messages_status 
  ON social.mail_messages(recipient_id, status) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_mail_messages_expires 
  ON social.mail_messages(expires_at) WHERE deleted_at IS NULL AND expires_at IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_mail_messages_type 
  ON social.mail_messages(type, sent_at DESC) WHERE deleted_at IS NULL;

