-- Issue: #database-schema-setup
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create main database schemas for NECPGAME

-- Create main schemas
CREATE SCHEMA IF NOT EXISTS gameplay;
CREATE SCHEMA IF NOT EXISTS narrative;
CREATE SCHEMA IF NOT EXISTS knowledge;

-- Grant permissions (adjust as needed for your setup)
-- GRANT USAGE ON SCHEMA gameplay TO necpgame_app;
-- GRANT USAGE ON SCHEMA narrative TO necpgame_app;
-- GRANT USAGE ON SCHEMA knowledge TO necpgame_app;

-- Comments for schema documentation
COMMENT ON SCHEMA gameplay IS 'Gameplay-related tables (quests, items, combat, progression)';
COMMENT ON SCHEMA narrative IS 'Narrative and story-related tables (NPCs, dialogues, events)';
COMMENT ON SCHEMA knowledge IS 'Knowledge base tables (lore, world-building, documentation)';

-- BACKEND NOTE: Schema separation provides logical data organization
-- and allows for different access patterns per microservice