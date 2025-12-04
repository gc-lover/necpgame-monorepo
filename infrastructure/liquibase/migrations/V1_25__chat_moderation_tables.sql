CREATE TABLE IF NOT EXISTS social.chat_reports (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  reporter_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  reported_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  message_id UUID REFERENCES social.chat_messages(id) ON DELETE SET NULL,
  channel_id UUID REFERENCES social.chat_channels(id) ON DELETE SET NULL,
  admin_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
  reason TEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  resolved_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_chat_reports_reporter 
  ON social.chat_reports(reporter_id);

CREATE INDEX IF NOT EXISTS idx_chat_reports_reported 
  ON social.chat_reports(reported_id);

CREATE INDEX IF NOT EXISTS idx_chat_reports_status 
  ON social.chat_reports(status) WHERE status = 'pending';

CREATE INDEX IF NOT EXISTS idx_chat_reports_created_at 
  ON social.chat_reports(created_at DESC);


