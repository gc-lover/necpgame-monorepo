-- MMO Index Optimization: Composite Keys and Time-Based Partitioning
-- Issue: #1949 - Оптимизация индексов для MMO - composite keys и partitioning по времени
-- liquibase formatted sql

--changeset author:backend-agent dbms:postgresql

BEGIN;
--comment: MMO database optimization with composite indexes and time-based partitioning for high-throughput queries

-- ===========================================
-- COMPOSITE INDEXES FOR MMO WORKLOADS
-- ===========================================

-- Combat sessions - player + time queries (most frequent in MMO)
CREATE INDEX IF NOT EXISTS idx_combat_sessions_player_time ON gameplay.combat_sessions (player_id, created_at DESC) WHERE status IN ('active', 'completed');
CREATE INDEX IF NOT EXISTS idx_combat_sessions_region_time ON gameplay.combat_sessions (region_id, created_at DESC) WHERE status = 'active';

-- Combat session players - multi-player lookups
CREATE INDEX IF NOT EXISTS idx_combat_session_players_session_player ON gameplay.combat_session_players (session_id, player_id) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_combat_session_players_player_time ON gameplay.combat_session_players (player_id, joined_at DESC);

-- Quest progress - player activity tracking
CREATE INDEX IF NOT EXISTS idx_quest_progress_player_quest_status ON gameplay.quest_progress (player_id, quest_id, status) WHERE status IN ('active', 'completed');
CREATE INDEX IF NOT EXISTS idx_quest_progress_updated_time ON gameplay.quest_progress (updated_at DESC, status) WHERE status = 'active';

-- Player inventory - hot inventory queries
CREATE INDEX IF NOT EXISTS idx_player_inventory_player_item ON gameplay.player_inventory (player_id, item_id) WHERE quantity > 0;
CREATE INDEX IF NOT EXISTS idx_player_inventory_player_equipped ON gameplay.player_inventory (player_id, equipped_slot) WHERE equipped_slot IS NOT NULL;

-- Auction system - market queries
CREATE INDEX IF NOT EXISTS idx_auctions_seller_time ON gameplay.auctions (seller_id, created_at DESC) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auctions_item_price ON gameplay.auctions (item_id, buyout_price) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auction_bids_bidder_time ON gameplay.auction_bids (bidder_id, created_at DESC);

-- Transaction history - financial tracking
CREATE INDEX IF NOT EXISTS idx_transaction_history_player_time ON gameplay.transaction_history (player_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_transaction_history_type_time ON gameplay.transaction_history (transaction_type, created_at DESC);

-- Market price history - economic analysis
CREATE INDEX IF NOT EXISTS idx_market_price_history_item_time ON gameplay.market_price_history (item_id, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_price_history_time_price ON gameplay.market_price_history (recorded_at DESC, price);

-- ===========================================
-- TIME-BASED PARTITIONING FOR HIGH-VOLUME TABLES
-- ===========================================

-- Partition combat_sessions by month (high volume, time-based queries)
-- Drop existing table and recreate as partitioned
DO $$
BEGIN
    -- Check if table is already partitioned
    IF NOT EXISTS (
        SELECT 1 FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'combat_sessions'
        AND n.nspname = 'gameplay'
        AND c.relispartition = true
    ) THEN
        -- Create partitioned version if not exists
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_partitioned (
            LIKE gameplay.combat_sessions INCLUDING ALL
        ) PARTITION BY RANGE (created_at);

        -- Create monthly partitions for current and next 6 months
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_01 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_02 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_03 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-03-01') TO ('2024-04-01');
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_04 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-04-01') TO ('2024-05-01');
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_05 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-05-01') TO ('2024-06-01');
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_2024_06 PARTITION OF gameplay.combat_sessions_partitioned
            FOR VALUES FROM ('2024-06-01') TO ('2024-07-01');

        -- Default partition for future data
        CREATE TABLE IF NOT EXISTS gameplay.combat_sessions_default PARTITION OF gameplay.combat_sessions_partitioned
            DEFAULT;
    END IF;
END $$;

-- Partition transaction_history by month (financial audit trails)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = 'transaction_history_partitioned'
        AND n.nspname = 'gameplay'
    ) THEN
        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_partitioned (
            LIKE gameplay.transaction_history INCLUDING ALL
        ) PARTITION BY RANGE (created_at);

        -- Create quarterly partitions (financial reporting cycles)
        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_2024_q1 PARTITION OF gameplay.transaction_history_partitioned
            FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');
        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_2024_q2 PARTITION OF gameplay.transaction_history_partitioned
            FOR VALUES FROM ('2024-04-01') TO ('2024-07-01');
        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_2024_q3 PARTITION OF gameplay.transaction_history_partitioned
            FOR VALUES FROM ('2024-07-01') TO ('2024-10-01');
        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_2024_q4 PARTITION OF gameplay.transaction_history_partitioned
            FOR VALUES FROM ('2024-10-01') TO ('2025-01-01');

        CREATE TABLE IF NOT EXISTS gameplay.transaction_history_default PARTITION OF gameplay.transaction_history_partitioned
            DEFAULT;
    END IF;
END $$;

-- ===========================================
-- TABLE OPTIMIZATION FOR MMO PERFORMANCE
-- ===========================================

-- Optimize high-traffic tables for MMO workloads
ALTER TABLE gameplay.combat_sessions SET (autovacuum_vacuum_scale_factor = 0.02);
ALTER TABLE gameplay.combat_sessions SET (autovacuum_analyze_scale_factor = 0.01);
ALTER TABLE gameplay.combat_sessions SET (autovacuum_vacuum_threshold = 50);
ALTER TABLE gameplay.combat_sessions SET (autovacuum_analyze_threshold = 25);
ALTER TABLE gameplay.combat_sessions SET (fillfactor = 90);

ALTER TABLE gameplay.player_inventory SET (autovacuum_vacuum_scale_factor = 0.02);
ALTER TABLE gameplay.player_inventory SET (autovacuum_analyze_scale_factor = 0.01);
ALTER TABLE gameplay.player_inventory SET (fillfactor = 90);

ALTER TABLE gameplay.auctions SET (autovacuum_vacuum_scale_factor = 0.02);
ALTER TABLE gameplay.auctions SET (autovacuum_analyze_scale_factor = 0.01);
ALTER TABLE gameplay.auctions SET (fillfactor = 90);

-- ===========================================
-- ADDITIONAL MMO-SPECIFIC INDEXES
-- ===========================================

-- Player activity tracking (session management)
CREATE INDEX IF NOT EXISTS idx_player_sessions_player_active ON gameplay.player_sessions (player_id, last_activity DESC) WHERE status = 'active';

-- Guild/clan member queries
CREATE INDEX IF NOT EXISTS idx_guild_members_guild_role ON gameplay.guild_members (guild_id, role, joined_at DESC);

-- Social relationships (friend lists, alliances)
CREATE INDEX IF NOT EXISTS idx_social_relationships_player_type ON gameplay.social_relationships (player_id, relationship_type, created_at DESC) WHERE status = 'active';

-- Achievement progress tracking
CREATE INDEX IF NOT EXISTS idx_achievement_progress_player_achievement ON gameplay.achievement_progress (player_id, achievement_id) WHERE progress < 100;

-- Market transaction analytics
CREATE INDEX IF NOT EXISTS idx_market_transactions_item_region_time ON gameplay.market_transactions (item_id, region_id, transaction_time DESC);

-- ===========================================
-- INDEX MAINTENANCE OPTIMIZATION
-- ===========================================

-- Create partial indexes for active records only (reduces index size significantly)
CREATE INDEX IF NOT EXISTS idx_active_combat_sessions ON gameplay.combat_sessions (player_id, created_at DESC) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_active_auctions ON gameplay.auctions (item_id, buyout_price) WHERE status = 'active' AND expires_at > CURRENT_TIMESTAMP;
CREATE INDEX IF NOT EXISTS idx_active_quests ON gameplay.quest_progress (player_id, updated_at DESC) WHERE status = 'active';

-- Create covering indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_player_inventory_covering ON gameplay.player_inventory (player_id, equipped_slot, item_id, quantity) WHERE quantity > 0;
CREATE INDEX IF NOT EXISTS idx_auction_covering ON gameplay.auctions (seller_id, item_id, buyout_price, expires_at) WHERE status = 'active';

-- ===========================================
-- PARTITION MANAGEMENT FUNCTIONS
-- ===========================================

-- Function to automatically create new partitions
CREATE OR REPLACE FUNCTION gameplay.create_monthly_partition(
    base_table_name TEXT,
    partition_date DATE DEFAULT CURRENT_DATE
) RETURNS TEXT AS $$
DECLARE
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    -- Calculate partition boundaries
    start_date := date_trunc('month', partition_date);
    end_date := start_date + INTERVAL '1 month';
    partition_name := base_table_name || '_' || to_char(start_date, 'YYYY_MM');

    -- Create partition if it doesn't exist
    EXECUTE format('CREATE TABLE IF NOT EXISTS %I PARTITION OF %I FOR VALUES FROM (%L) TO (%L)',
                   partition_name, base_table_name, start_date, end_date);

    RETURN partition_name;
END;
$$ LANGUAGE plpgsql;

-- Function to drop old partitions (data retention)
CREATE OR REPLACE FUNCTION gameplay.drop_old_partitions(
    base_table_name TEXT,
    retention_months INTEGER DEFAULT 12
) RETURNS INTEGER AS $$
DECLARE
    partition_name TEXT;
    dropped_count INTEGER := 0;
    cutoff_date DATE := CURRENT_DATE - (retention_months || ' months')::INTERVAL;
BEGIN
    -- Find and drop partitions older than retention period
    FOR partition_name IN
        SELECT tablename FROM pg_tables
        WHERE schemaname = 'gameplay'
        AND tablename LIKE base_table_name || '_%'
        AND tablename ~ ('^' || base_table_name || '_[0-9]{4}_[0-9]{2}$')
    LOOP
        -- Extract date from partition name and check if it's old enough
        IF substring(partition_name from char_length(base_table_name) + 2) ~ '^[0-9]{4}_[0-9]{2}$' THEN
            DECLARE
                partition_date DATE;
            BEGIN
                partition_date := to_date(substring(partition_name from char_length(base_table_name) + 2), 'YYYY_MM');
                IF partition_date < cutoff_date THEN
                    EXECUTE format('DROP TABLE IF EXISTS %I', partition_name);
                    dropped_count := dropped_count + 1;
                END IF;
            EXCEPTION WHEN OTHERS THEN
                -- Skip invalid partition names
                CONTINUE;
            END;
        END IF;
    END LOOP;

    RETURN dropped_count;
END;
$$ LANGUAGE plpgsql;

-- ===========================================
-- PERFORMANCE MONITORING INDEXES
-- ===========================================

-- Query performance tracking
CREATE INDEX IF NOT EXISTS idx_query_performance_time ON gameplay.query_performance (executed_at DESC, duration_ms);
CREATE INDEX IF NOT EXISTS idx_query_performance_slow ON gameplay.query_performance (duration_ms DESC) WHERE duration_ms > 100;

-- Database connection monitoring
CREATE INDEX IF NOT EXISTS idx_connection_pool_stats_time ON gameplay.connection_pool_stats (recorded_at DESC, active_connections);

-- ===========================================
-- FINAL OPTIMIZATION COMMANDS
-- ===========================================

-- Analyze tables to update statistics after index creation
ANALYZE gameplay.combat_sessions;
ANALYZE gameplay.player_inventory;
ANALYZE gameplay.auctions;
ANALYZE gameplay.quest_progress;
ANALYZE gameplay.transaction_history;

COMMIT;