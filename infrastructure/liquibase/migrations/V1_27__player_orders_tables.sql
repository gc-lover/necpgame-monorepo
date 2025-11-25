--liquibase formatted sql

--changeset necpgame:V1_27_player_orders_tables
--comment: Create tables for player orders system

CREATE TABLE IF NOT EXISTS social.player_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    executor_id UUID,
    order_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'open',
    reward JSONB,
    requirements JSONB,
    deadline TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES mvp_core.character(id),
    CONSTRAINT fk_executor FOREIGN KEY (executor_id) REFERENCES mvp_core.character(id),
    CONSTRAINT chk_order_type CHECK (order_type IN ('combat', 'hacking', 'trade', 'political', 'research', 'social')),
    CONSTRAINT chk_status CHECK (status IN ('open', 'accepted', 'in_progress', 'completed', 'cancelled'))
);

CREATE INDEX IF NOT EXISTS idx_player_orders_customer_id ON social.player_orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_player_orders_executor_id ON social.player_orders(executor_id);
CREATE INDEX IF NOT EXISTS idx_player_orders_status ON social.player_orders(status);
CREATE INDEX IF NOT EXISTS idx_player_orders_order_type ON social.player_orders(order_type);
CREATE INDEX IF NOT EXISTS idx_player_orders_created_at ON social.player_orders(created_at DESC);

CREATE TABLE IF NOT EXISTS social.player_order_reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    reviewer_id UUID NOT NULL,
    executor_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES social.player_orders(id),
    CONSTRAINT fk_reviewer FOREIGN KEY (reviewer_id) REFERENCES mvp_core.character(id),
    CONSTRAINT fk_executor FOREIGN KEY (executor_id) REFERENCES mvp_core.character(id),
    CONSTRAINT uq_order_review UNIQUE (order_id, reviewer_id)
);

CREATE INDEX IF NOT EXISTS idx_player_order_reviews_order_id ON social.player_order_reviews(order_id);
CREATE INDEX IF NOT EXISTS idx_player_order_reviews_executor_id ON social.player_order_reviews(executor_id);
CREATE INDEX IF NOT EXISTS idx_player_order_reviews_rating ON social.player_order_reviews(rating);

--rollback DROP TABLE IF EXISTS social.player_order_reviews;
--rollback DROP TABLE IF EXISTS social.player_orders;

