--liquibase formatted sql

--changeset necp-game:001-create-oauth-accounts-table
--comment: Create oauth_accounts table for OAuth provider integration

CREATE TABLE IF NOT EXISTS oauth_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    provider VARCHAR(50) NOT NULL,
    oauth_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(provider, oauth_id),
    UNIQUE(user_id, provider)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_oauth_accounts_user_id ON oauth_accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_oauth_accounts_provider_oauth_id ON oauth_accounts(provider, oauth_id);

-- Add comments
COMMENT ON TABLE oauth_accounts IS 'OAuth provider accounts linked to user accounts';
COMMENT ON COLUMN oauth_accounts.user_id IS 'Reference to the user account';
COMMENT ON COLUMN oauth_accounts.provider IS 'OAuth provider name (google, github, discord)';
COMMENT ON COLUMN oauth_accounts.oauth_id IS 'OAuth provider user ID';

--changeset necp-game:001-create-oauth-accounts-table-rollback
--rollback DROP TABLE IF EXISTS oauth_accounts;