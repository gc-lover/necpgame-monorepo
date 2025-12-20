-- Issue: #1914
-- Jackie Welles NPC profile migration v1.3.0
-- Enhanced profile with combat stats, inventory, quest integration, relationships, economic mechanics

DO $$
DECLARE
    npc_profile_json JSONB;
    npc_id UUID;
BEGIN
    -- Generate UUID for the NPC
    npc_id := gen_random_uuid();

    -- Create comprehensive NPC profile JSON
    npc_profile_json := '{
        "id": "' || npc_id || '",
        "name": "Santiago \"Jackie\" Welles Morales",
        "display_name": "Jackie Welles",
        "aliases": ["Santiago Welles", "Jackie", "El Gallo"],
        "age": 28,
        "gender": "male",
        "race": "human",
        "nationality": "mexican",
        "occupation": "Mercenary & Fixer",
        "faction": "Valentinos (former)",
        "status": "Independent Fixer",
        "location": "Heywood, Night City",
        "residence": "Modest family home in Heywood",

        "physical_appearance": {
            "height": "185cm",
            "build": "athletic",
            "hair": "black, short",
            "eyes": "brown",
            "skin": "olive",
            "distinguishing_features": ["Mantis Blade scars on arms", "Valentinos tattoos", "confident smile"],
            "style": "streetwear with mexican influences"
        },

        "background": {
            "birth_date": "2010-05-15",
            "birth_place": "Heywood, Night City",
            "family": {
                "mother": "Maria Morales (alive, homemaker)",
                "father": "Carlos Morales (deceased, Arasaka worker)",
                "sister": "Ana Morales (younger, student)",
                "extended": "Large extended family in Valentinos community"
            },
            "education": "Street-educated, Valentinos training",
            "military_service": "None",
            "criminal_record": "Minor offenses as youth, clean adult record",
            "key_events": [
                "2025: Father killed in corporate riots",
                "2027: Joined Valentinos as youth member",
                "2030s: Began mercenary work",
                "2040s: Lost partner in contract gone wrong, became more selective",
                "2050s: Established as reliable fixer"
            ]
        },

        "personality": {
            "traits": ["loyal", "optimistic", "honorable", "family-oriented", "charismatic"],
            "strengths": ["combat skills", "street knowledge", "network connections", "moral compass"],
            "weaknesses": ["family blind spot", "occasional recklessness", "PTSD triggers"],
            "speech_patterns": ["Mexican accent", "Spanish phrases", "street slang", "infectious laugh"],
            "mannerisms": ["confident posture", "frequent smiles", "family mentions", "hand gestures"]
        },

        "skills_abilities": {
            "combat": {
                "primary_weapon": "Mantis Blades",
                "secondary_weapon": "Shotgun (Overwatch)",
                "specialization": "Close-quarters combat, protection details",
                "skill_level": "expert",
                "implants": ["Mantis Blades", "Gorilla Arms", "Reinforced Tendons", "Pain Editor"]
            },
            "technical": {
                "hacking": "basic",
                "driving": "expert",
                "mechanics": "proficient",
                "negotiation": "skilled"
            },
            "social": {
                "charisma": "high",
                "networking": "extensive Valentinos and street contacts",
                "languages": ["English", "Spanish", "Street slang"]
            }
        },

        "relationships": {
            "family": [
                {"name": "Maria Morales", "relation": "mother", "status": "close"},
                {"name": "Ana Morales", "relation": "sister", "status": "protective"}
            ],
            "valentinos": [
                {"name": "El Capitan", "relation": "mentor", "status": "respects"},
                {"name": "Various clan members", "relation": "brothers", "status": "loyal"}
            ],
            "contacts": [
                {"name": "Marco \"Fix\" Sanchez", "relation": "fellow fixer", "status": "friendly rival"},
                {"name": "Misty", "relation": "close friend", "status": "romantic interest"},
                {"name": "Various street mercs", "relation": "network", "status": "professional"}
            ],
            "enemies": [
                {"name": "Corporate security forces", "reason": "past contracts"},
                {"name": "Rival gangs", "reason": "territory disputes"}
            ]
        },

        "inventory_weapons": [
            {"name": "Mantis Blades", "type": "melee", "quality": "legendary", "mods": ["extended reach", "poison coating"]},
            {"name": "Overwatch shotgun", "type": "shotgun", "quality": "high", "mods": ["extended magazine", "smart choke"]},
            {"name": "Malorian Arms 3516", "type": "pistol", "quality": "standard", "mods": ["silencer"]},
            {"name": "Gorilla Arms cyberlimbs", "type": "implants", "quality": "military grade"}
        ],

        "inventory_equipment": [
            {"name": "Thorton Galena", "type": "vehicle", "quality": "modified", "mods": ["turbo", "armored plating"]},
            {"name": "Streetwear armor", "type": "armor", "quality": "crafted", "mods": ["bullet resistant", "stylish"]},
            {"name": "Fixer tools", "type": "tools", "quality": "professional"}
        ],

        "economic_profile": {
            "income_sources": ["mercenary contracts", "fixer fees", "Valentinos support"],
            "wealth_level": "middle class",
            "assets": ["family home", "Thorton Galena", "weapons cache"],
            "debts": ["none significant"],
            "trading_preferences": ["weapons", "vehicles", "information", "favors"]
        },

        "quest_integration": {
            "main_story_hooks": [
                "Street Kid starting companion",
                "Heywood district guide",
                "Valentinos faction quests"
            ],
            "side_quests": [
                "Family protection contracts",
                "Mercenary training missions",
                "Street justice campaigns"
            ],
            "companion_system": {
                "available": true,
                "combat_role": "melee specialist, tank",
                "dialogue_options": ["optimistic", "honorable", "street-smart"],
                "relationship_levels": ["stranger", "acquaintance", "friend", "brother"]
            }
        },

        "reputation_mechanics": {
            "street_cred": "high",
            "corporate_rep": "neutral",
            "gang_rep": {"valentinos": "high", "maelstrom": "low", "trauma_team": "high"},
            "fixer_rating": "reliable, honorable",
            "notable_achievements": ["survived 50+ contracts", "saved Valentinos from raid", "trained 20+ new mercs"]
        },

        "behavior_patterns": {
            "daily_routine": ["morning workouts", "family time", "contract work", "networking"],
            "decision_making": ["moral compass first", "family impact consideration", "professional risk assessment"],
            "stress_responses": ["PTSD flashbacks", "increased protectiveness", "reckless behavior"],
            "motivation_triggers": ["family threats", "injustice", "honorable contracts", "community needs"]
        },

        "gameplay_mechanics": {
            "spawn_locations": ["Heywood streets", "Valentinos hideouts", "Mercenary bars"],
            "interaction_types": ["dialogue", "combat companion", "quest giver", "trader", "trainer"],
            "combat_ai": "aggressive melee, protective positioning, coordinated attacks",
            "dialogue_system": {
                "languages": ["English", "Spanish"],
                "tone": "friendly, street-smart, occasionally philosophical",
                "topics": ["family", "honor", "Night City life", "Valentinos history", "mercenary wisdom"]
            }
        },

        "narrative_importance": {
            "story_arcs": ["Street Kid origin", "Heywood development", "Valentinos integration"],
            "themes": ["family loyalty", "street honor", "immigrant experience", "redemption"],
            "player_impact": "mentorship, faction access, street knowledge, emotional support",
            "endgame_potential": "leadership position in Valentinos, successful fixer business"
        },

        "metadata": {
            "created_at": "2025-12-20T05:00:00Z",
            "updated_at": "2025-12-20T05:00:00Z",
            "version": "1.3.0",
            "author": "Content Writer",
            "review_status": "approved",
            "gameplay_ready": true,
            "qa_verified": false
        }
    }';

    -- Insert or update the NPC profile
    INSERT INTO narrative.npc_definitions (
        npc_id,
        name,
        display_name,
        npc_type,
        faction,
        location,
        status,
        profile_data,
        created_at,
        updated_at,
        version
    ) VALUES (
        npc_id,
        'Santiago "Jackie" Welles Morales',
        'Jackie Welles',
        'important',
        'valentinos',
        'heywood',
        'active',
        npc_profile_json,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        '1.3.0'
    )
    ON CONFLICT (npc_id) DO UPDATE SET
        profile_data = EXCLUDED.profile_data,
        updated_at = CURRENT_TIMESTAMP,
        version = '1.3.0';

    RAISE NOTICE 'Jackie Welles NPC profile v1.3.0 inserted/updated successfully with ID: %', npc_id;

END $$;