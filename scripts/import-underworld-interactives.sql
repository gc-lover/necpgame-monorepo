-- Issue: #1842
-- SQL script to import underworld interactive objects into database

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

-- Import underworld interactive objects
SELECT import_interactive_object(
    'black_market',
    'Black Market',
    'Чёрный Рынок',
    'underworld',
    'cyberpunk',
    'tactical',
    'Теневая торговая площадка в бэкрумах и подвалах. Обмен редкостями, квестовые NPC, контрабандные сделки.',
    '{
        "trade_mechanics": {
            "item_exchange": {
                "barter_system": "item_for_item",
                "eddies_conversion": "black_market_rate",
                "reputation_discounts": "loyalty_bonuses",
                "scarcity_multipliers": "demand_based_pricing"
            },
            "quest_integration": {
                "fixer_quests": "retrieval_missions",
                "contact_introductions": "network_expansion",
                "faction_rep_changes": "loyalty_shifts",
                "story_advancement": "plot_unlocks"
            }
        }
    }'::jsonb,
    '{
        "market_types": {
            "backroom_exchange": {
                "merchant_count": "3-8",
                "item_rarity": "rare-epic",
                "security_level": "medium",
                "reputation_requirement": "street_cred_100"
            },
            "underground_bazaar": {
                "merchant_count": "8-15",
                "item_rarity": "uncommon-legendary",
                "security_level": "high",
                "reputation_requirement": "fixer_contacts"
            },
            "nomad_outpost": {
                "merchant_count": "2-5",
                "item_rarity": "basic-rare",
                "security_level": "low",
                "reputation_requirement": "nomad_alliance"
            }
        }
    }'::jsonb,
    '{
        "security_measures": {
            "undercover_agents": {
                "detection_chance": "15-30%",
                "response_time": "60-180_seconds",
                "bounty_consequences": "wanted_level_increase"
            },
            "market_raids": {
                "raid_frequency": "random_events",
                "escape_routes": "emergency_exits",
                "loot_protection": "stash_systems"
            }
        }
    }'::jsonb,
    '{
        "market_transaction_volumes": "count",
        "item_rarity_exchanges": "count",
        "quest_completions": "count",
        "security_incident_reports": "count"
    }'::jsonb,
    '{
        "black_markets": {
            "environmental_ambience": "dim_lighting_with_neon_signs",
            "merchant_interactions": "subdued_conversations_and_bargaining",
            "security_atmosphere": "tension_with_occasional_shouts",
            "exit_points": "hidden_doors_and_escape_routes"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{
        "underworld_density": {
            "street_level": {
                "interactive_frequency": 1.2,
                "market_activity": "high",
                "lab_safety": "low",
                "tunnel_complexity": "medium"
            },
            "deep_underground": {
                "interactive_frequency": 0.8,
                "market_activity": "variable",
                "lab_safety": "very_low",
                "tunnel_complexity": "high"
            },
            "border_zones": {
                "interactive_frequency": 1.0,
                "market_activity": "premium",
                "lab_safety": "medium",
                "tunnel_complexity": "maximum"
            }
        }
    }'::jsonb
);

SELECT import_interactive_object(
    'makeshift_lab',
    'Makeshift Lab',
    'Импровизированная Лаборатория',
    'underworld',
    'cyberpunk',
    'dangerous',
    'Самодельные мастерские для крафта и апгрейдов. Рискованные эксперименты с возможными взрывами и отравлениями.',
    '{
        "crafting_system": {
            "recipe_complexity": {
                "basic_mods": "simple_materials",
                "advanced_upgrades": "rare_components",
                "experimental_items": "unstable_compounds"
            },
            "success_mechanics": {
                "skill_requirements": "crafting_proficiency",
                "tool_quality": "equipment_condition",
                "material_purity": "ingredient_quality",
                "environmental_factors": "lab_conditions"
            }
        }
    }'::jsonb,
    '{
        "lab_types": {
            "street_chemist": {
                "specialization": "drug_synthesis",
                "explosion_risk": "25-40%",
                "product_quality": "medium-high",
                "contamination_radius": "5-10_meters"
            },
            "ripperdoc_den": {
                "specialization": "cyberware_modification",
                "explosion_risk": "15-30%",
                "product_quality": "high",
                "contamination_radius": "3-7_meters"
            },
            "weapons_workshop": {
                "specialization": "gun_modification",
                "explosion_risk": "20-35%",
                "product_quality": "medium",
                "contamination_radius": "4-8_meters"
            }
        }
    }'::jsonb,
    '{
        "hazard_mechanics": {
            "explosion_effects": {
                "damage_radius": "lab_destruction",
                "fire_spread": "adjacent_room_ignition",
                "toxic_cloud": "area_poisoning",
                "structural_damage": "building_instability"
            },
            "contamination_types": {
                "chemical_burns": "direct_damage_over_time",
                "radiation_poisoning": "health_degradation",
                "cyberware_malfunction": "implant_corruption",
                "hallucination_effects": "perception_distortion"
            }
        }
    }'::jsonb,
    '{
        "crafting_attempt_rates": "count",
        "explosion_incidents": "count",
        "product_success_rates": "count",
        "contamination_exposures": "count"
    }'::jsonb,
    '{
        "makeshift_labs": {
            "equipment_variety": "scavenged_tools_and_makeshift_equipment",
            "hazard_indicators": "warning_lights_and_containment_sounds",
            "explosion_effects": "dramatic_fire_and_debris_animations",
            "safety_protocols": "emergency_shutdown_sequences"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{}'::jsonb
);

SELECT import_interactive_object(
    'contraband_tunnel',
    'Contraband Tunnel',
    'Контрабандный Тоннель',
    'underworld',
    'cyberpunk',
    'tactical',
    'Секретные проходы и люки для быстрого перемещения. Случайные встречи, засады и скрытые пути в другие зоны.',
    '{
        "travel_mechanics": {
            "route_navigation": {
                "path_complexity": "branching_junctions",
                "landmark_recognition": "environmental_cues",
                "dead_end_traps": "false_paths",
                "shortcut_discovery": "hidden_passages"
            },
            "encounter_system": {
                "random_events": ["patrol_encounters", "rival_smugglers", "mutant_infestation"],
                "ambush_probability": "tunnel_traffic_based",
                "escape_options": "alternative_routes",
                "combat_modifiers": "confined_space_penalties"
            }
        }
    }'::jsonb,
    '{
        "tunnel_types": {
            "sewer_network": {
                "travel_speed": "1.5x_normal",
                "encounter_chance": "30-50%",
                "destination_options": ["adjacent_blocks", "district_crossing"],
                "maintenance_level": "poor"
            },
            "utility_corridors": {
                "travel_speed": "2x_normal",
                "encounter_chance": "20-40%",
                "destination_options": ["service_access", "roof_access"],
                "maintenance_level": "fair"
            },
            "smugglers_route": {
                "travel_speed": "2.5x_normal",
                "encounter_chance": "40-60%",
                "destination_options": ["border_crossing", "safehouse_network"],
                "maintenance_level": "excellent"
            }
        }
    }'::jsonb,
    '{
        "security_features": {
            "access_control": {
                "biometric_locks": "dna_scanners",
                "password_systems": "rotating_codes",
                "keycard_requirements": "faction_membership",
                "trap_mechanisms": "automated_defenses"
            },
            "surveillance_network": {
                "camera_coverage": "motion_detection",
                "alarm_triggers": "unauthorized_entry",
                "reinforcement_calls": "emergency_response",
                "tracking_beacons": "location_monitoring"
            }
        }
    }'::jsonb,
    '{
        "tunnel_usage_frequency": "count",
        "encounter_success_rates": "count",
        "travel_time_statistics": "count",
        "security_breach_incidents": "count"
    }'::jsonb,
    '{
        "contraband_tunnels": {
            "environmental_details": "dripping_water_and_rust_textures",
            "navigation_cues": "graffiti_markers_and_utility_lighting",
            "encounter_sounds": "echoing_footsteps_and_distant_threats",
            "transition_effects": "smooth_camera_movements_through_access_points"
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
WHERE object_id IN ('black_market', 'makeshift_lab', 'contraband_tunnel');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN ('black_market', 'makeshift_lab', 'contraband_tunnel')
ORDER BY object_id;