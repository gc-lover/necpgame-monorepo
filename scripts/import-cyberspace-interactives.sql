-- Issue: #1843
-- SQL script to import cyberspace interactive objects into database

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create temporary function to import interactive object
CREATE OR REPLACE FUNCTION import_interactive_object(
    p_object_id TEXT,
    p_name TEXT,
    p_display_name TEXT,
    p_category TEXT,
    p_era TEXT,
    p_threat_level TEXT,
    p_description TEXT,
    p_metadata JSONB,
    p_interaction_data JSONB,
    p_effects_data JSONB,
    p_telemetry_data JSONB,
    p_visual_data JSONB,
    p_audio_data JSONB,
    p_balance_data JSONB
) RETURNS UUID AS $$
DECLARE
    v_id UUID;
BEGIN
    -- Generate UUID from object_id if it doesn't exist
    SELECT id INTO v_id
    FROM gameplay.interactive_objects
    WHERE object_id = p_object_id;

    IF v_id IS NULL THEN
        -- Insert new record
        INSERT INTO gameplay.interactive_objects (
            id,
            object_id,
            name,
            display_name,
            category,
            era,
            threat_level,
            description,
            location_data,
            interaction_data,
            effects_data,
            telemetry_data,
            visual_data,
            audio_data,
            balance_data,
            is_active,
            created_at,
            updated_at
        ) VALUES (
            uuid_generate_v4(),
            p_object_id,
            p_name,
            p_display_name,
            p_category,
            p_era,
            p_threat_level,
            p_description,
            p_metadata,
            p_interaction_data,
            p_effects_data,
            p_telemetry_data,
            p_visual_data,
            p_audio_data,
            p_balance_data,
            true,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        ) RETURNING id INTO v_id;
    ELSE
        -- Update existing record
        UPDATE gameplay.interactive_objects
        SET
            name = p_name,
            display_name = p_display_name,
            category = p_category,
            era = p_era,
            threat_level = p_threat_level,
            description = p_description,
            location_data = p_metadata,
            interaction_data = p_interaction_data,
            effects_data = p_effects_data,
            telemetry_data = p_telemetry_data,
            visual_data = p_visual_data,
            audio_data = p_audio_data,
            balance_data = p_balance_data,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = v_id;
    END IF;

    RETURN v_id;
END;
$$ LANGUAGE plpgsql;

-- Import cyberspace interactive objects
SELECT import_interactive_object(
    'ice_access_node',
    'ICE Access Node',
    'ICE Узел Доступа',
    'cyberspace',
    'cyberpunk',
    'tactical',
    'Цифровые узлы с интра-ICE защитой, содержащие ценные данные и бонусы. Взлом дает временные баффы к нетраннингу или ключи к системам реального мира.',
    '{
        "ice_protection": {
            "defense_mechanisms": {
                "pattern_recognition": "adaptive_algorithms",
                "counter_intrusion": "automated_response",
                "data_corruption": "self_destruct_protocols"
            },
            "breach_difficulty": {
                "novice_level": "basic_encryption",
                "intermediate_level": "multi_layer_defense",
                "expert_level": "ai_controlled_systems"
            }
        }
    }'::jsonb,
    '{
        "node_types": {
            "skill_booster_node": {
                "ice_rating": "1-3",
                "access_time": "15-45_seconds",
                "corruption_risk": "10-20%",
                "reward_type": "temporary_skill_buff"
            },
            "data_key_node": {
                "ice_rating": "2-5",
                "access_time": "30-90_seconds",
                "corruption_risk": "15-30%",
                "reward_type": "real_world_access_key"
            },
            "system_override_node": {
                "ice_rating": "4-7",
                "access_time": "60-150_seconds",
                "corruption_risk": "25-45%",
                "reward_type": "network_control_token"
            }
        }
    }'::jsonb,
    '{
        "reward_system": {
            "skill_enhancements": {
                "quickhack_efficiency": "+15-35%",
                "buffer_capacity": "+20-40%",
                "daemon_stability": "+10-25%"
            },
            "access_grants": {
                "security_door_unlock": "bypass_physical_security",
                "elevator_control": "vertical_movement_override",
                "surveillance_disable": "camera_network_shutdown"
            }
        }
    }'::jsonb,
    '{
        "ice_node_breaches": "count",
        "corruption_incidents": "count",
        "skill_buff_applications": "count",
        "access_key_utilizations": "count"
    }'::jsonb,
    '{
        "ice_nodes": {
            "visual_representation": "glowing_data_structures_with_intricate_patterns",
            "breach_effects": "particle_explosions_and_code_cascades",
            "corruption_indicators": "glitchy_distortions_and_error_messages"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{
        "cyberspace_density": {
            "netrunner_hotspots": {
                "interactive_frequency": 1.5,
                "ice_complexity": "high",
                "corruption_severity": "extreme",
                "competition_intensity": "maximum"
            },
            "standard_networks": {
                "interactive_frequency": 1.0,
                "ice_complexity": "medium",
                "corruption_severity": "moderate",
                "competition_intensity": "standard"
            },
            "peripheral_systems": {
                "interactive_frequency": 0.5,
                "ice_complexity": "low",
                "corruption_severity": "minimal",
                "competition_intensity": "casual"
            }
        }
    }'::jsonb
);

SELECT import_interactive_object(
    'phantom_archive',
    'Phantom Archive',
    'Фантомный Архив',
    'cyberspace',
    'cyberpunk',
    'dangerous',
    'Неустойчивые цифровые хранилища с лором, квестовыми данными и историями. Доступ дает ценную информацию, но несет риск цифровой коррупции игрока.',
    '{
        "corruption_mechanics": {
            "digital_instability": {
                "manifestation_types": ["system_glitches", "memory_fragmentation", "neural_noise"],
                "severity_levels": ["minor", "moderate", "severe"],
                "duration_range": "30-300_seconds"
            },
            "recovery_options": {
                "system_purge": "data_cleanup_routine",
                "buffer_flush": "memory_reset_protocol",
                "daemon_recalibration": "skill_realignment"
            }
        }
    }'::jsonb,
    '{
        "archive_types": {
            "lore_fragment": {
                "data_integrity": "70-90%",
                "corruption_chance": "20-35%",
                "access_time": "10-25_seconds",
                "content_type": "narrative_lore"
            },
            "quest_intelligence": {
                "data_integrity": "60-85%",
                "corruption_chance": "25-40%",
                "access_time": "20-40_seconds",
                "content_type": "mission_data"
            },
            "historical_record": {
                "data_integrity": "80-95%",
                "corruption_chance": "15-25%",
                "access_time": "15-35_seconds",
                "content_type": "timeline_information"
            }
        }
    }'::jsonb,
    '{
        "content_structure": {
            "narrative_elements": {
                "story_fragments": ["character_backgrounds", "world_events", "faction_histories"],
                "quest_hooks": ["mission_triggers", "contact_locations", "objective_clues"],
                "environmental_data": ["location_intel", "security_layouts", "resource_maps"]
            },
            "data_preservation": {
                "integrity_scanning": "content_verification",
                "corruption_detection": "anomaly_identification",
                "recovery_attempts": "data_restoration"
            }
        }
    }'::jsonb,
    '{
        "archive_accesses": "count",
        "corruption_events": "count",
        "lore_discoveries": "count",
        "quest_triggers_activated": "count"
    }'::jsonb,
    '{
        "phantom_archives": {
            "archive_manifestation": "floating_data_orbs_with_holographic_displays",
            "corruption_visuals": "static_interference_and_fragmented_textures",
            "content_reveal": "progressive_data_crystallization"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{}'::jsonb
);

SELECT import_interactive_object(
    'tournament_arena',
    'Tournament Arena',
    'Турнирная Арена',
    'cyberspace',
    'cyberpunk',
    'utility',
    'Цифровые арены для соревновательного нетраннинга с мини-ивентами. Участие дает косметические награды, репутацию и временные бонусы.',
    '{
        "competition_formats": {
            "elimination_tournament": {
                "round_structure": "single_elimination",
                "advancement_criteria": "victory_points",
                "spectator_access": "real_time_streaming"
            },
            "points_accumulation": {
                "scoring_system": "objective_completion",
                "leaderboard_tracking": "real_time_ranking",
                "bonus_objectives": "optional_challenges"
            }
        }
    }'::jsonb,
    '{
        "arena_types": {
            "speed_hack_challenge": {
                "participant_limit": "4-8",
                "duration": "5-10_minutes",
                "victory_condition": "first_to_complete",
                "reward_tier": "bronze_silver_gold"
            },
            "defense_matrix": {
                "participant_limit": "6-12",
                "duration": "10-15_minutes",
                "victory_condition": "system_defense_duration",
                "reward_tier": "defensive_mastery"
            },
            "data_race": {
                "participant_limit": "8-16",
                "duration": "8-12_minutes",
                "victory_condition": "most_data_collected",
                "reward_tier": "collection_specialist"
            }
        }
    }'::jsonb,
    '{
        "reward_ecosystem": {
            "cosmetic_rewards": {
                "visual_effects": ["particle_trails", "interface_themes", "avatar_modifiers"],
                "sound_design": ["victory_fanfares", "ambient_overlays", "notification_tones"],
                "animation_sets": ["victory_poses", "defeat_reactions", "idle_animations"]
            },
            "functional_bonuses": {
                "temporary_buffs": ["skill_multipliers", "resource_bonuses", "cooldown_reductions"],
                "reputation_gains": ["faction_standing", "network_access", "special_permissions"]
            }
        }
    }'::jsonb,
    '{
        "arena_participation": "count",
        "tournament_completions": "count",
        "reward_distributions": "count",
        "spectator_engagement": "count"
    }'::jsonb,
    '{
        "tournament_arenas": {
            "arena_environments": "dynamic_virtual_spaces_with_animated_backgrounds",
            "participant_indicators": "color_coded_energy_aura_effects",
            "victory_celebrations": "spectacular_light_and_sound_displays"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{}'::jsonb
);

-- Clean up the temporary function
DROP FUNCTION import_interactive_object(TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, JSONB, JSONB, JSONB, JSONB, JSONB, JSONB, JSONB);

-- Show results
SELECT
    COUNT(*) as total_imported,
    COUNT(*) FILTER (WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute') as newly_created,
    COUNT(*) FILTER (WHERE updated_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute' AND created_at < CURRENT_TIMESTAMP - INTERVAL '1 minute') as updated
FROM gameplay.interactive_objects
WHERE object_id IN ('ice_access_node', 'phantom_archive', 'tournament_arena');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN ('ice_access_node', 'phantom_archive', 'tournament_arena')
ORDER BY object_id;