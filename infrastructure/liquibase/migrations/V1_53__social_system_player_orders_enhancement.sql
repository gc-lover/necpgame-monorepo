-- Issue: #140875791
-- Social System - Player Orders Enhancement
-- Дополнение схемы системы заказов игроков:
-- - Мульти-исполнительные заказы (multi_executor_orders)
-- - Аукционы заказов (order_auctions, auction_bids)
-- - Опционы на заказы (order_options)
-- - Арбитраж заказов (order_arbitration)
-- - Страхование заказов (order_insurance)
-- - Рейтинги заказов (order_ratings)
-- - Репутация в заказах (order_reputation)
-- - Экономика заказов (order_economy)
-- - Телеметрия заказов (order_telemetry)

-- Таблица мульти-исполнительных заказов
CREATE TABLE IF NOT EXISTS social.multi_executor_orders (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    team_leader_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    required_roles JSONB NOT NULL,
    executor_ids UUID[] NOT NULL,
    role_assignments JSONB,
    reward_distribution JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для multi_executor_orders
CREATE INDEX IF NOT EXISTS idx_multi_executor_orders_order_id ON social.multi_executor_orders(order_id);
CREATE INDEX IF NOT EXISTS idx_multi_executor_orders_team_leader_id ON social.multi_executor_orders(team_leader_id);

-- Таблица аукционов заказов
CREATE TABLE IF NOT EXISTS social.order_auctions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    auction_type VARCHAR(20) NOT NULL CHECK (auction_type IN ('ascending', 'descending', 'sealed')),
    start_price DECIMAL(10, 2) NOT NULL CHECK (start_price >= 0),
    current_price DECIMAL(10, 2) NOT NULL CHECK (current_price >= 0),
    reserve_price DECIMAL(10, 2) CHECK (reserve_price IS NULL OR reserve_price >= 0),
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('open', 'closed', 'cancelled')),
    winner_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для order_auctions
CREATE INDEX IF NOT EXISTS idx_order_auctions_order_id ON social.order_auctions(order_id);
CREATE INDEX IF NOT EXISTS idx_order_auctions_status ON social.order_auctions(status, end_time) WHERE status = 'open';
CREATE INDEX IF NOT EXISTS idx_order_auctions_winner_id ON social.order_auctions(winner_id) WHERE winner_id IS NOT NULL;

-- Таблица ставок на аукционах
CREATE TABLE IF NOT EXISTS social.auction_bids (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    auction_id UUID NOT NULL REFERENCES social.order_auctions(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    bid_amount DECIMAL(10, 2) NOT NULL CHECK (bid_amount > 0),
    bid_conditions JSONB,
    bid_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'outbid', 'winning', 'lost'))
);

-- Индексы для auction_bids
CREATE INDEX IF NOT EXISTS idx_auction_bids_auction_id ON social.auction_bids(auction_id, bid_amount DESC);
CREATE INDEX IF NOT EXISTS idx_auction_bids_bidder_id ON social.auction_bids(bidder_id, status);
CREATE INDEX IF NOT EXISTS idx_auction_bids_status ON social.auction_bids(status, bid_time DESC);

-- Таблица опционов на заказы
CREATE TABLE IF NOT EXISTS social.order_options (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    option_type VARCHAR(30) NOT NULL CHECK (option_type IN ('customer_cancellation', 'executor_cancellation', 'mutual_cancellation')),
    buyer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    premium DECIMAL(10, 2) NOT NULL CHECK (premium >= 0),
    compensation_amount DECIMAL(10, 2) NOT NULL CHECK (compensation_amount >= 0),
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exercised_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'exercised', 'expired'))
);

-- Индексы для order_options
CREATE INDEX IF NOT EXISTS idx_order_options_order_id ON social.order_options(order_id, status);
CREATE INDEX IF NOT EXISTS idx_order_options_buyer_id ON social.order_options(buyer_id, status);
CREATE INDEX IF NOT EXISTS idx_order_options_status ON social.order_options(status);

-- Таблица арбитража заказов
CREATE TABLE IF NOT EXISTS social.order_arbitration (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    complainant_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    defendant_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    reason TEXT NOT NULL,
    evidence JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'in-review', 'resolved', 'dismissed')),
    decision JSONB,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для order_arbitration
CREATE INDEX IF NOT EXISTS idx_order_arbitration_order_id ON social.order_arbitration(order_id, status);
CREATE INDEX IF NOT EXISTS idx_order_arbitration_complainant ON social.order_arbitration(complainant_id, status);
CREATE INDEX IF NOT EXISTS idx_order_arbitration_defendant ON social.order_arbitration(defendant_id, status);

-- Таблица страхования заказов
CREATE TABLE IF NOT EXISTS social.order_insurance (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    insurance_type VARCHAR(30) NOT NULL CHECK (insurance_type IN ('mission_failure', 'cargo', 'delay', 'comprehensive')),
    buyer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    premium DECIMAL(10, 2) NOT NULL CHECK (premium >= 0),
    coverage_amount DECIMAL(10, 2) NOT NULL CHECK (coverage_amount >= 0),
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    claimed_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'claimed', 'expired'))
);

-- Индексы для order_insurance
CREATE INDEX IF NOT EXISTS idx_order_insurance_order_id ON social.order_insurance(order_id, status);
CREATE INDEX IF NOT EXISTS idx_order_insurance_buyer_id ON social.order_insurance(buyer_id, status);
CREATE INDEX IF NOT EXISTS idx_order_insurance_status ON social.order_insurance(status);

-- Таблица рейтингов заказов
CREATE TABLE IF NOT EXISTS social.order_ratings (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    rater_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    rated_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    rating_type VARCHAR(20) NOT NULL CHECK (rating_type IN ('customer', 'executor')),
    completion_rate DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (completion_rate >= 0 AND completion_rate <= 1),
    quality_score DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (quality_score >= 0 AND quality_score <= 1),
    timeliness DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (timeliness >= 0 AND timeliness <= 1),
    communication DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (communication >= 0 AND communication <= 1),
    cooperation DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (cooperation >= 0 AND cooperation <= 1),
    overall_rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (overall_rating >= 0 AND overall_rating <= 1),
    review_text TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(order_id, rater_id, rating_type)
);

-- Индексы для order_ratings
CREATE INDEX IF NOT EXISTS idx_order_ratings_order_id ON social.order_ratings(order_id);
CREATE INDEX IF NOT EXISTS idx_order_ratings_rated_id ON social.order_ratings(rated_id, rating_type);
CREATE INDEX IF NOT EXISTS idx_order_ratings_overall ON social.order_ratings(overall_rating DESC);

-- Таблица репутации в заказах
CREATE TABLE IF NOT EXISTS social.order_reputation (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    reputation_type VARCHAR(20) NOT NULL CHECK (reputation_type IN ('customer', 'executor')),
    order_count INTEGER NOT NULL DEFAULT 0 CHECK (order_count >= 0),
    completion_rate DECIMAL(5, 4) NOT NULL DEFAULT 0.0000 CHECK (completion_rate >= 0 AND completion_rate <= 1),
    average_rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (average_rating >= 0 AND average_rating <= 1),
    arbitration_cases INTEGER NOT NULL DEFAULT 0 CHECK (arbitration_cases >= 0),
    insurance_claims INTEGER NOT NULL DEFAULT 0 CHECK (insurance_claims >= 0),
    trust_level DECIMAL(3, 2) NOT NULL DEFAULT 0.00 CHECK (trust_level >= 0 AND trust_level <= 1),
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, reputation_type)
);

-- Индексы для order_reputation
CREATE INDEX IF NOT EXISTS idx_order_reputation_character_id ON social.order_reputation(character_id);
CREATE INDEX IF NOT EXISTS idx_order_reputation_type ON social.order_reputation(reputation_type, trust_level DESC);
CREATE INDEX IF NOT EXISTS idx_order_reputation_trust ON social.order_reputation(trust_level DESC);

-- Таблица экономики заказов
CREATE TABLE IF NOT EXISTS social.order_economy (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    transaction_type VARCHAR(30) NOT NULL CHECK (transaction_type IN ('deposit', 'payment', 'commission', 'refund', 'penalty')),
    amount DECIMAL(10, 2) NOT NULL,
    from_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    to_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для order_economy
CREATE INDEX IF NOT EXISTS idx_order_economy_order_id ON social.order_economy(order_id, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_order_economy_from_id ON social.order_economy(from_id, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_order_economy_to_id ON social.order_economy(to_id, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_order_economy_type ON social.order_economy(transaction_type, transaction_date DESC);

-- Таблица телеметрии заказов
CREATE TABLE IF NOT EXISTS social.order_telemetry (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES social.player_orders(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL,
    event_data JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для order_telemetry
CREATE INDEX IF NOT EXISTS idx_order_telemetry_order_id ON social.order_telemetry(order_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_order_telemetry_event_type ON social.order_telemetry(event_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_order_telemetry_created_at ON social.order_telemetry(created_at DESC);

-- Обновление player_orders для соответствия архитектуре
ALTER TABLE social.player_orders
ADD COLUMN IF NOT EXISTS complexity INTEGER DEFAULT 0,
ADD COLUMN IF NOT EXISTS risk_level VARCHAR(20) CHECK (risk_level IN ('low', 'medium', 'high')),
ADD COLUMN IF NOT EXISTS reward_amount DECIMAL(10, 2),
ADD COLUMN IF NOT EXISTS payment_model VARCHAR(20) CHECK (payment_model IN ('fixed', 'hourly', 'percentage', 'hybrid')),
ADD COLUMN IF NOT EXISTS format VARCHAR(20) CHECK (format IN ('public', 'selective', 'private', 'auction'));

-- Обновление индексов для player_orders
CREATE INDEX IF NOT EXISTS idx_player_orders_complexity ON social.player_orders(complexity, status);
CREATE INDEX IF NOT EXISTS idx_player_orders_risk_level ON social.player_orders(risk_level, status);
CREATE INDEX IF NOT EXISTS idx_player_orders_format ON social.player_orders(format, status);

-- Комментарии к таблицам
COMMENT ON TABLE social.multi_executor_orders IS 'Мульти-исполнительные заказы (команды исполнителей)';
COMMENT ON TABLE social.order_auctions IS 'Аукционы заказов (ascending, descending, sealed)';
COMMENT ON TABLE social.auction_bids IS 'Ставки на аукционах заказов';
COMMENT ON TABLE social.order_options IS 'Опционы на заказы (отмена, компенсация)';
COMMENT ON TABLE social.order_arbitration IS 'Арбитраж заказов (споры, решения)';
COMMENT ON TABLE social.order_insurance IS 'Страхование заказов (провал миссии, груз, задержка)';
COMMENT ON TABLE social.order_ratings IS 'Рейтинги заказов (качество, своевременность, коммуникация)';
COMMENT ON TABLE social.order_reputation IS 'Репутация в заказах (заказчик/исполнитель)';
COMMENT ON TABLE social.order_economy IS 'Экономика заказов (депозиты, выплаты, комиссии)';
COMMENT ON TABLE social.order_telemetry IS 'Телеметрия заказов (события, аналитика)';


