-- Issue: #1869
-- SQL script to import Jackie Welles NPC profile into database

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Ensure narrative schema exists
CREATE SCHEMA IF NOT EXISTS narrative;

-- Create narrative.npc_definitions table if it doesn't exist
CREATE TABLE IF NOT EXISTS narrative.npc_definitions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  npc_id VARCHAR(255) NOT NULL UNIQUE,
  title VARCHAR(500) NOT NULL,
  content_data JSONB NOT NULL DEFAULT '{}',
  version INTEGER NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_npc_definitions_npc_id ON narrative.npc_definitions(npc_id);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_active ON narrative.npc_definitions(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_npc_definitions_version ON narrative.npc_definitions(version);

-- Create function to import NPC definition
CREATE OR REPLACE FUNCTION import_npc_definition(
    p_npc_id TEXT,
    p_title TEXT,
    p_content_data JSONB,
    p_version INTEGER DEFAULT 1
) RETURNS UUID AS $$
DECLARE
    v_id UUID;
    v_existing_version INTEGER;
BEGIN
    -- Check if NPC already exists
    SELECT id, version INTO v_id, v_existing_version
    FROM narrative.npc_definitions
    WHERE npc_id = p_npc_id;

    IF v_id IS NOT NULL THEN
        -- Update existing record if new version is higher
        IF p_version > v_existing_version THEN
            UPDATE narrative.npc_definitions SET
                title = p_title,
                content_data = p_content_data,
                version = p_version,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = v_id;

            RAISE NOTICE 'Updated existing NPC: % (version % → %)', p_title, v_existing_version, p_version;
        ELSE
            RAISE NOTICE 'NPC % already exists with same or higher version (%)', p_title, v_existing_version;
        END IF;
    ELSE
        -- Insert new record
        INSERT INTO narrative.npc_definitions (
            npc_id,
            title,
            content_data,
            version,
            is_active
        ) VALUES (
            p_npc_id,
            p_title,
            p_content_data,
            p_version,
            true
        ) RETURNING id INTO v_id;

        RAISE NOTICE 'Inserted new NPC: % (%)', p_title, p_npc_id;
    END IF;

    RETURN v_id;
END;
$$ LANGUAGE plpgsql;

-- Import Jackie Welles NPC profile
-- This would be populated with the actual YAML content converted to JSON
-- For now, we'll create a placeholder structure

SELECT import_npc_definition(
    'jackie-welles-street-partner',
    'Джеки Уэллс — напарник улиц Night City',
    '{
        "metadata": {
            "id": "canon-lore-jackie-wells",
            "title": "Джеки Уэллс — напарник улиц Night City",
            "category": "lore",
            "status": "ready_for_backend",
            "version": "1.1.0",
            "last_updated": "2025-12-14T17:29:00Z",
            "concept_approved": true,
            "tags": ["npc", "street-partner", "nomad", "mercenary", "night-city"]
        },
        "identity": {
            "name": "Santiago \"Jackie\" Welles Morales",
            "age": 28,
            "ethnicity": "Латиноамериканец (пуэрториканские корни)",
            "faction": "Valentinos (номадский клан)",
            "role": "Уличный напарник, начинающий фиксер, ментор для новоприбывших",
            "appearance": {
                "height": "185 см",
                "build": "Атлетичное, мускулистое",
                "hair": "Короткие, черные с седыми прядями",
                "eyes": "Темно-карие, проницательные",
                "distinguishing_marks": "Татуировка Valentinos на шее, шрамы от боев, золотые зубы"
            },
            "implants": [
                "Mantis Blades Mk.2 (основное оружие ближнего боя)",
                "Biotechnica Optics Mk.4 (тепловизор, определение угроз)",
                "Subdermal Armor (легкая баллистическая защита)",
                "Painkiller (эндокринный контроль)",
                "Gorilla Arms (усиление силы и стабильности)"
            ]
        },
        "combat_stats": {
            "style": "Ближний бой с имплантами, поддержка огнестрельным оружием",
            "attributes": {
                "health": 150,
                "stamina": 120,
                "strength": 8,
                "reflexes": 9,
                "technique": 7,
                "cool": 6
            },
            "skills": {
                "melee": "Expert",
                "firearms": "Advanced",
                "hacking": "Basic",
                "tactics": "Expert"
            },
            "preferred_weapons": [
                "Malorian Arms 3516 shotgun (Betty)",
                "Ramirez Nuevos handgun",
                "Mantis Blades",
                "Frag grenades"
            ]
        },
        "inventory_equipment": {
            "weapons": [
                "Malorian Arms 3516 shotgun",
                "Ramirez Nuevos handgun",
                "Combat knife"
            ],
            "armor": [
                "Leather jacket with ballistic inserts",
                "Reinforced jeans",
                "Combat boots"
            ],
            "transport": "Thorton Galena (customized pickup truck)",
            "cash_reserve": 5000
        },
        "quest_integration": {
            "main_arcs": [
                "Welcome to Night City",
                "Family Matters",
                "Brother''s Shadow",
                "Rising Through Ranks"
            ],
            "side_contracts": [
                "Trash Run",
                "Debt Collection",
                "Equipment Recovery"
            ],
            "faction_quests": [
                "Valentinos: Family duties",
                "Nomad Alliance: Inter-clan cooperation"
            ]
        },
        "dialogue_trees": {
            "greetings": [
                "Ey, choom! Jackie Welles aquí. ¿Qué necesitas?",
                "What''s up, my friend? Ready to make some eddies?",
                "Vato, you look like you could use a hand."
            ],
            "combat": [
                "¡Vamos! Time to dance!",
                "Cover me, I''ll flank ''em!",
                "Watch your six, vato!"
            ],
            "personal": [
                "Sometimes I dream about leaving Night City...",
                "My brother Claude... he was the good one.",
                "Money''s good, but family lasts forever."
            ]
        },
        "economic_interactions": {
            "trading_discounts": "15% on street weapons, 20% on implant repairs",
            "financial_habits": "500-1000 eddies daily expenses, 15000 eddies debt",
            "economic_links": ["Valentinos Bank", "Black Market", "Fixer Network"]
        },
        "backstory": {
            "early_years": "Born in Puerto Rico, family emigrated to Night City",
            "military_service": "Militech soldier, gained combat experience and PTSD",
            "valentinos_membership": "Found family in nomad clan, became street mercenary",
            "key_events": [
                "Saved Rogue AM''s life (2072)",
                "First major contract - stole corporate transport (2074)",
                "Valentinos full membership tattoo (2075)",
                "Lost brother Claude to Maelstrom (2075)"
            ]
        },
        "personality": {
            "traits": ["Loyal", "Optimistic", "Careful", "Impulsive", "Humorous"],
            "weaknesses": ["PTSD flashbacks", "Impulsive decisions", "Financial struggles"],
            "values": ["Family above all", "Street honor", "Help the weak"],
            "motivations": ["Protect family", "Accumulate wealth", "Find life meaning"]
        },
        "relationships": {
            "family": ["Mother Rosa (widow)", "Sister Elena (student)", "Brother Claude †"],
            "valentinos": ["Padre (clan leader)", "Tia Maria (spiritual guide)", "Hermano Carlos (best friend)"],
            "contacts": ["Misty Olshevski (spiritual)", "Rogue AM (debt)", "Panam Palmer (romantic interest)"]
        }
    }'::jsonb,
    3
);

-- Clean up the temporary function
DROP FUNCTION import_npc_definition(TEXT, TEXT, JSONB, INTEGER);

-- Show results
SELECT
    COUNT(*) as total_imported,
    COUNT(*) FILTER (WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute') as newly_created,
    COUNT(*) FILTER (WHERE updated_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute' AND created_at < CURRENT_TIMESTAMP - INTERVAL '1 minute') as updated
FROM narrative.npc_definitions
WHERE npc_id = 'jackie-welles-street-partner';

-- Show imported NPC
SELECT npc_id, title, version, is_active, created_at, updated_at
FROM narrative.npc_definitions
WHERE npc_id = 'jackie-welles-street-partner';