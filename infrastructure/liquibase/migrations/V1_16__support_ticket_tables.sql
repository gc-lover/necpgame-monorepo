CREATE SCHEMA IF NOT EXISTS support;

CREATE TABLE IF NOT EXISTS support.support_tickets (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  assigned_agent_id UUID,
  description TEXT NOT NULL,
  number VARCHAR(50) NOT NULL UNIQUE,
  category VARCHAR(20) NOT NULL,
  priority VARCHAR(20) NOT NULL DEFAULT 'normal',
  status VARCHAR(20) NOT NULL DEFAULT 'open',
  subject VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  resolved_at TIMESTAMP,
  closed_at TIMESTAMP,
  first_response_at TIMESTAMP,
  satisfaction_rating INT CHECK (satisfaction_rating >= 1 AND satisfaction_rating <= 5)
);

CREATE TABLE IF NOT EXISTS support.ticket_responses (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  ticket_id UUID NOT NULL REFERENCES support.support_tickets(id) ON DELETE CASCADE,
  author_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  message TEXT NOT NULL,
  visibility VARCHAR(20) NOT NULL DEFAULT 'public',
  attachments JSONB NOT NULL DEFAULT '[]',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_agent BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX IF NOT EXISTS idx_support_tickets_player ON support.support_tickets(player_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_support_tickets_agent ON support.support_tickets(assigned_agent_id, status);
CREATE INDEX IF NOT EXISTS idx_support_tickets_status ON support.support_tickets(status, created_at ASC);
CREATE INDEX IF NOT EXISTS idx_support_tickets_number ON support.support_tickets(number);
CREATE INDEX IF NOT EXISTS idx_support_tickets_category ON support.support_tickets(category, status);
CREATE INDEX IF NOT EXISTS idx_support_tickets_priority ON support.support_tickets(priority, status);

CREATE INDEX IF NOT EXISTS idx_ticket_responses_ticket ON support.ticket_responses(ticket_id, created_at ASC);
CREATE INDEX IF NOT EXISTS idx_ticket_responses_author ON support.ticket_responses(author_id);

