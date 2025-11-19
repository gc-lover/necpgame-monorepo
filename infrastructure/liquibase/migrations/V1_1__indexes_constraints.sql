CREATE INDEX IF NOT EXISTS idx_outbox_created_at ON mvp_meta.outbox(created_at);
CREATE INDEX IF NOT EXISTS idx_event_log_created_at ON mvp_meta.event_log(created_at);


