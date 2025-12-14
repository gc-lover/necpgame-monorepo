-- Issue: #1844
-- SQL script to import world common interactive objects into database

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

-- Import world common interactive objects
SELECT import_interactive_object(
    'faction_blockpost',
    'Faction Blockpost',
    'Блок-пост фракций',
    'world_common',
    'cyberpunk',
    'strategic',
    'Контрольно-пропускные пункты различных фракций и корпораций. Определяют зоны контроля, влияют на цены и доступ к территориям.',
    '{
        "control_mechanics": {
            "takeover_methods": ["bribery", "hacking", "assault"],
            "control_effects": {
                "price_influence": {
                    "local_shops": "+10-50%",
                    "transport_costs": "+5-25%",
                    "black_market_rates": "-20-0% (uncontrolled)"
                },
                "security_changes": {
                    "patrol_frequency": "+25-75%",
                    "wanted_level": "+1-3",
                    "safe_zones": "reduced_accessibility"
                }
            }
        }
    }'::jsonb,
    '{
        "faction_types": {
            "corporate_control": {
                "controlling_entity": ["arasaka", "militech", "biotechnica"],
                "control_radius": "500-1000_meters",
                "price_modifier": "+15-30%",
                "access_requirements": "corporate_id_or_bribe"
            },
            "gang_territory": {
                "controlling_entity": ["valentinos", "maelstrom", "animals"],
                "control_radius": "200-500_meters",
                "price_modifier": "+5-20%",
                "access_requirements": "gang_loyalty_or_force"
            },
            "government_checkpoint": {
                "controlling_entity": ["ncpd", "max_tac", "trauma_team"],
                "control_radius": "300-800_meters",
                "price_modifier": "+10-25%",
                "access_requirements": "official_permit"
            }
        }
    }'::jsonb,
    '{
        "control_mechanics": {
            "takeover_methods": {
                "bribery": {
                    "cost_range": "500-2000_eddies",
                    "success_rate": "70-90%",
                    "detection_risk": "20%"
                },
                "hacking": {
                    "time_required": "60-180_seconds",
                    "success_rate": "40-80%",
                    "detection_risk": "60%",
                    "alarm_probability": "30%"
                },
                "assault": {
                    "difficulty_rating": "medium-high",
                    "reinforcement_time": "120-300_seconds",
                    "casualty_risk": "high"
                }
            }
        }
    }'::jsonb,
    '{
        "blockpost_control_changes": "count",
        "takeover_attempts_successful": "count",
        "bribery_transactions": "count",
        "security_level_adjustments": "count"
    }'::jsonb,
    '{
        "blockpost_models": {
            "corporate_checkpoints": "sleek_metal_structures_with_scanners",
            "gang_outposts": "barricaded_positions_with_graffiti",
            "government_stations": "official_buildings_with_flags"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{
        "world_distribution": {
            "urban_density": {
                "container_frequency": 1.0,
                "station_availability": 0.8,
                "relay_coverage": 0.6,
                "blockpost_density": 0.4
            },
            "rural_scarcity": {
                "container_frequency": 0.3,
                "station_availability": 0.4,
                "relay_coverage": 0.5,
                "blockpost_density": 0.2
            },
            "highway_networks": {
                "container_frequency": 0.7,
                "station_availability": 0.9,
                "relay_coverage": 0.8,
                "blockpost_density": 0.6
            }
        },
        "faction_influence": {
            "corporate_zones": {
                "security_intensity": "high",
                "resource_quality": "premium",
                "trap_frequency": "medium"
            },
            "gang_territories": {
                "security_intensity": "variable",
                "resource_quality": "mixed",
                "trap_frequency": "high"
            },
            "neutral_grounds": {
                "security_intensity": "low",
                "resource_quality": "random",
                "trap_frequency": "medium"
            }
        }
    }'::jsonb
);

SELECT import_interactive_object(
    'comm_relay',
    'Communication Relay',
    'Коммуникационный ретранслятор',
    'world_common',
    'cyberpunk',
    'tactical',
    'Высокотехнологичные ретрансляторы связи для улучшения коммуникаций. Увеличивают радиус связи, раскрывают карту, но привлекают патрули.',
    '{
        "enhancement_effects": {
            "squad_communication": {
                "voice_chat_quality": "crystal_clear",
                "coordinate_sharing": "real_time",
                "emergency_beacons": "amplified"
            },
            "tactical_advantages": {
                "enemy_position_tracking": "improved",
                "objective_markers": "persistent",
                "extraction_points": "highlighted"
            },
            "strategic_benefits": {
                "intel_gathering": "automated_scanning",
                "resource_locations": "revealed",
                "safe_routes": "calculated"
            }
        }
    }'::jsonb,
    '{
        "relay_types": {
            "urban_network": {
                "coverage_radius": "300-600_meters",
                "signal_boost": "+50%",
                "map_reveal": "25-50%",
                "patrol_attraction": "medium"
            },
            "highway_system": {
                "coverage_radius": "800-1500_meters",
                "signal_boost": "+75%",
                "map_reveal": "40-70%",
                "patrol_attraction": "high"
            },
            "remote_outpost": {
                "coverage_radius": "500-1000_meters",
                "signal_boost": "+60%",
                "map_reveal": "30-60%",
                "patrol_attraction": "low"
            }
        }
    }'::jsonb,
    '{
        "operational_risks": {
            "detection_mechanisms": {
                "signal_interception": "15-30%_chance",
                "patrol_dispatch": "45-75%_chance",
                "alarm_activation": "20-40%_chance"
            },
            "countermeasures": {
                "signal_jamming": "temporary_blackout",
                "false_signals": "decoy_transmissions",
                "relay_destruction": "permanent_shutdown"
            }
        }
    }'::jsonb,
    '{
        "relay_activation_events": "count",
        "signal_boost_utilization": "count",
        "map_reveal_statistics": "count",
        "patrol_attraction_incidents": "count"
    }'::jsonb,
    '{
        "relay_antennas": {
            "urban_towers": "sleek_communication_masts",
            "highway_poles": "integrated_roadside_installations",
            "remote_beacons": "satellite-linked_outposts"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{}'::jsonb
);

SELECT import_interactive_object(
    'medical_station',
    'Medical Station',
    'Медицинская станция',
    'world_common',
    'cyberpunk',
    'utility',
    'Автоматизированные медицинские пункты для быстрого лечения. Ограниченные заряды, создают шум, привлекают врагов.',
    '{
        "healing_mechanics": {
            "treatment_types": {
                "emergency_stabilization": {
                    "hp_restored": "25-50%",
                    "time_required": "10-20_seconds",
                    "charge_cost": 1
                },
                "full_recovery": {
                    "hp_restored": "75-100%",
                    "time_required": "30-60_seconds",
                    "charge_cost": "2-3"
                },
                "cyberware_repair": {
                    "implant_restored": "50-100%",
                    "time_required": "45-90_seconds",
                    "charge_cost": 2
                }
            },
            "resource_management": {
                "charge_regeneration": {
                    "time_interval": "4-8_hours",
                    "regeneration_rate": "1_charge",
                    "max_capacity": "station_specific"
                },
                "emergency_protocols": {
                    "critical_condition_bonus": "+25%_efficiency",
                    "override_charges": "1_free_treatment",
                    "alarm_suppression": "temporary"
                }
            }
        }
    }'::jsonb,
    '{
        "station_types": {
            "roadside_emergency": {
                "charge_capacity": "3-5_charges",
                "healing_efficiency": "60-80%",
                "noise_level": "medium",
                "enemy_attraction_radius": "100-200_meters"
            },
            "urban_clinic": {
                "charge_capacity": "5-8_charges",
                "healing_efficiency": "70-90%",
                "noise_level": "high",
                "enemy_attraction_radius": "150-300_meters"
            },
            "corporate_medical": {
                "charge_capacity": "8-12_charges",
                "healing_efficiency": "80-95%",
                "noise_level": "very_high",
                "enemy_attraction_radius": "200-400_meters"
            }
        }
    }'::jsonb,
    '{
        "security_concerns": {
            "detection_vectors": {
                "medical_scanners": "wound_detection",
                "surveillance_cameras": "activity_monitoring",
                "network_logging": "usage_tracking"
            },
            "enemy_responses": {
                "patrol_investigation": "60-80%_chance",
                "alarm_activation": "30-50%_chance",
                "bounty_increase": "+10-25%"
            }
        }
    }'::jsonb,
    '{
        "medical_station_usage": "count",
        "healing_efficiency_stats": "count",
        "charge_consumption_rates": "count",
        "security_incident_reports": "count"
    }'::jsonb,
    '{
        "medical_stations": {
            "emergency_kiosks": "automated_treatment_booths",
            "field_hospitals": "mobile_medical_units",
            "corporate_clinics": "high-tech_medical_facilities"
        }
    }'::jsonb,
    '{}'::jsonb,
    '{}'::jsonb
);

SELECT import_interactive_object(
    'logistics_container',
    'Logistics Container',
    'Логистический контейнер',
    'world_common',
    'cyberpunk',
    'tactical',
    'Контейнеры с припасами, оборудованием и ресурсами. Могут содержать ценные грузы или быть ловушками/сигналками.',
    '{
        "loot_system": {
            "resource_categories": {
                "medical_supplies": ["medkits", "boosters", "implants"],
                "ammunition": ["standard_rounds", "special_ammo", "grenades"],
                "equipment": ["weapons", "armor", "gadgets"],
                "valuables": ["eddies", "data_chips", "rare_materials"]
            },
            "loot_tables": {
                "common_drops": {
                    "probability": "50-70%",
                    "value_range": "100-500_eddies",
                    "item_rarity": "basic"
                },
                "rare_drops": {
                    "probability": "20-40%",
                    "value_range": "500-2000_eddies",
                    "item_rarity": "uncommon"
                },
                "legendary_drops": {
                    "probability": "5-15%",
                    "value_range": "2000-10000_eddies",
                    "item_rarity": "rare"
                }
            }
        }
    }'::jsonb,
    '{
        "container_types": {
            "standard_shipping": {
                "loot_quality": "medium",
                "trap_probability": "15-25%",
                "security_level": "basic",
                "access_time": "15-30_seconds"
            },
            "military_supply": {
                "loot_quality": "high",
                "trap_probability": "30-45%",
                "security_level": "advanced",
                "access_time": "30-60_seconds"
            },
            "black_market_cache": {
                "loot_quality": "premium",
                "trap_probability": "20-35%",
                "security_level": "variable",
                "access_time": "20-45_seconds"
            },
            "abandoned_freight": {
                "loot_quality": "random",
                "trap_probability": "40-60%",
                "security_level": "degraded",
                "access_time": "10-25_seconds"
            }
        }
    }'::jsonb,
    '{
        "trap_mechanics": {
            "trap_types": {
                "explosive_device": {
                    "damage_radius": "5-10_meters",
                    "damage_type": "explosive",
                    "trigger_condition": "container_opening"
                },
                "alarm_system": {
                    "alert_radius": "200-500_meters",
                    "response_time": "30-90_seconds",
                    "patrol_count": "2-4_units"
                },
                "cyber_trap": {
                    "effect_type": "system_corruption",
                    "duration": "60-180_seconds",
                    "severity": "moderate_disruption"
                }
            },
            "countermeasure_options": {
                "scanner_detection": {
                    "success_rate": "60-85%",
                    "time_required": "5-15_seconds",
                    "false_positive_risk": "10%"
                },
                "careful_inspection": {
                    "success_rate": "40-70%",
                    "time_required": "20-40_seconds",
                    "evidence_preservation": "partial"
                },
                "remote_activation": {
                    "success_rate": "25-50%",
                    "time_required": "45-90_seconds",
                    "collateral_damage": "minimal"
                }
            }
        }
    }'::jsonb,
    '{
        "container_access_attempts": "count",
        "loot_value_statistics": "count",
        "trap_activation_rates": "count",
        "security_breach_incidents": "count"
    }'::jsonb,
    '{
        "container_variants": {
            "shipping_containers": "standard_iso_containers",
            "military_crates": "reinforced_storage_units",
            "black_market_stashes": "hidden_underground_caches"
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
WHERE object_id IN ('faction_blockpost', 'comm_relay', 'medical_station', 'logistics_container');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN ('faction_blockpost', 'comm_relay', 'medical_station', 'logistics_container')
ORDER BY object_id;