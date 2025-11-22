CREATE SCHEMA IF NOT EXISTS admin;

CREATE TABLE IF NOT EXISTS admin.admin_audit_log (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  admin_id UUID NOT NULL,
  action_type VARCHAR(50) NOT NULL,
  target_id UUID,
  target_type VARCHAR(50) NOT NULL,
  details JSONB NOT NULL DEFAULT '{}',
  ip_address VARCHAR(45),
  user_agent TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_admin_audit_log_admin ON admin.admin_audit_log(admin_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_audit_log_action ON admin.admin_audit_log(action_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_audit_log_target ON admin.admin_audit_log(target_id, target_type);
CREATE INDEX IF NOT EXISTS idx_admin_audit_log_created ON admin.admin_audit_log(created_at DESC);

