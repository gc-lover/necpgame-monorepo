-- Issue: #1840
-- SQL script to import industrial interactive objects into database

-- Enable UUID extension if not already enabled
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create temporary function to import interactive object
CREATE
OR REPLACE FUNCTION import_interactive_object(
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
SELECT id
INTO v_id
FROM gameplay.interactive_objects
WHERE object_id = p_object_id;

IF
v_id IS NULL THEN
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
SET name             = p_name,
    display_name     = p_display_name,
    category         = p_category,
    era              = p_era,
    threat_level     = p_threat_level,
    description      = p_description,
    location_data    = p_metadata,
    interaction_data = p_interaction_data,
    effects_data     = p_effects_data,
    telemetry_data   = p_telemetry_data,
    visual_data      = p_visual_data,
    audio_data       = p_audio_data,
    balance_data     = p_balance_data,
    updated_at       = CURRENT_TIMESTAMP
WHERE id = v_id;
END IF;

RETURN v_id;
END;
$$
LANGUAGE plpgsql;

-- Import industrial interactive objects
SELECT import_interactive_object(
               'electrical_panel',
               'Electrical Panel',
               'Электрощит/Рубильник',
               'industrial',
               'cyberpunk',
               'tactical',
               'Панели управления электропитанием для локального контроля освещения, лазерных систем и безопасности. Взлом или отключение создает зоны темноты, снижает эффективность патрулей или вызывает аварии.',
               '{
                   "control_methods": {
                       "physical_override": {
                           "access_method": "panel_breaking",
                           "time_required": "15-30_seconds",
                           "detection_risk": "40-60%",
                           "evidence_left": "broken_locks"
                       },
                       "electronic_hack": {
                           "access_method": "system_breach",
                           "time_required": "30-60_seconds",
                           "detection_risk": "20-40%",
                           "evidence_left": "system_logs"
                       },
                       "maintenance_bypass": {
                           "access_method": "service_codes",
                           "time_required": "5-15_seconds",
                           "detection_risk": "10-20%",
                           "evidence_left": "maintenance_records"
                       }
                   }
               }'::jsonb,
               '{
                   "panel_types": {
                       "main_distribution": {
                           "coverage_radius": "50-100_meters",
                           "effect_duration": "120-300_seconds",
                           "power_levels": ["full_shutdown", "reduced_power", "emergency_mode"],
                           "restoration_time": "30-90_seconds"
                       },
                       "security_grid": {
                           "coverage_radius": "30-60_meters",
                           "effect_duration": "60-180_seconds",
                           "power_levels": ["laser_deactivation", "camera_blackout", "alarm_silencing"],
                           "restoration_time": "15-45_seconds"
                       },
                       "emergency_backup": {
                           "coverage_radius": "20-40_meters",
                           "effect_duration": "45-120_seconds",
                           "power_levels": ["auxiliary_power", "fail_safe_activation"],
                           "restoration_time": "10-30_seconds"
                       }
                   }
               }'::jsonb,
               '{
                   "environmental_effects": {
                       "lighting_control": {
                           "darkness_zones": "enemy_detection_reduction",
                           "laser_grid_shutdown": "safe_passage_creation",
                           "emergency_lights": "alert_activation"
                       },
                       "security_impacts": {
                           "camera_disruption": "blind_spots_creation",
                           "alarm_system_override": "silent_operation",
                           "patrol_route_changes": "guard_repositioning"
                       }
                   }
               }'::jsonb,
               '{
                   "electrical_panel_accesses": "count",
                   "power_shutdown_events": "count",
                   "security_system_disruptions": "count",
                   "restoration_times_tracked": "count"
               }'::jsonb,
               '{
                   "electrical_panels": {
                       "control_interfaces": "industrial_touchscreen_displays",
                       "warning_indicators": "flashing_alert_lights",
                       "access_mechanisms": "mechanical_lock_systems"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{
                   "industrial_density": {
                       "heavy_industry": {
                           "interactive_frequency": 1.5,
                           "hazard_intensity": "high",
                           "machinery_complexity": "advanced",
                           "maintenance_access": "restricted"
                       },
                       "light_manufacturing": {
                           "interactive_frequency": 1.0,
                           "hazard_intensity": "medium",
                           "machinery_complexity": "standard",
                           "maintenance_access": "controlled"
                       },
                       "abandoned_facility": {
                           "interactive_frequency": 0.5,
                           "hazard_intensity": "variable",
                           "machinery_complexity": "degraded",
                           "maintenance_access": "unsecured"
                       }
                   }
               }'::jsonb
       );

SELECT import_interactive_object(
               'valve_system',
               'Valve System',
               'Клапанная система',
               'industrial',
               'cyberpunk',
               'dangerous',
               'Системы клапанов для управления потоками пара, химикатов и промышленных жидкостей. Неправильное использование вызывает повреждения игроку или создает зоны эффектов.',
               '{
                   "operation_mechanics": {
                       "pressure_control": {
                           "valve_states": ["closed", "low_flow", "high_flow", "rupture"],
                           "pressure_buildup": "5-15_seconds",
                           "release_patterns": ["controlled_burst", "random_spray", "area_flood"]
                       },
                       "safety_protocols": {
                           "emergency_shutdown": "automatic_valve_closure",
                           "pressure_relief": "venting_system_activation",
                           "containment_failure": "hazard_zone_expansion"
                       }
                   }
               }'::jsonb,
               '{
                   "valve_types": {
                       "steam_valve": {
                           "hazard_type": "thermal",
                           "effect_radius": "8-15_meters",
                           "damage_type": "burn_damage",
                           "effect_duration": "20-40_seconds",
                           "player_risk": "medium"
                       },
                       "chemical_valve": {
                           "hazard_type": "corrosive",
                           "effect_radius": "5-12_meters",
                           "damage_type": "acid_damage",
                           "effect_duration": "30-60_seconds",
                           "player_risk": "high"
                       },
                       "coolant_valve": {
                           "hazard_type": "cryogenic",
                           "effect_radius": "6-10_meters",
                           "damage_type": "freeze_damage",
                           "effect_duration": "15-30_seconds",
                           "player_risk": "medium"
                       }
                   }
               }'::jsonb,
               '{
                   "tactical_applications": {
                       "enemy_suppression": {
                           "steam_blast": "temporary_blindness_zone",
                           "chemical_fog": "movement_slow_debuff",
                           "coolant_spray": "freeze_trap_creation"
                       },
                       "environmental_manipulation": {
                           "pressure_room_creation": "sealed_combat_zone",
                           "ventilation_control": "gas_distribution",
                           "temperature_modulation": "heat_signature_alteration"
                       }
                   }
               }'::jsonb,
               '{
                   "valve_operation_events": "count",
                   "hazard_exposure_incidents": "count",
                   "environmental_effect_usage": "count",
                   "safety_protocol_activations": "count"
               }'::jsonb,
               '{
                   "valve_systems": {
                       "pressure_gauges": "analog_dial_instruments",
                       "release_valves": "heavy_industrial_handles",
                       "hazard_markings": "warning_signage_networks"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

SELECT import_interactive_object(
               'conveyor_system',
               'Conveyor System',
               'Конвейерная система',
               'industrial',
               'cyberpunk',
               'utility',
               'Промышленные конвейеры и лифтовые системы для перемещения грузов и персонала. Могут быть использованы для создания альтернативных маршрутов или блокировки патрулей.',
               '{
                   "operational_controls": {
                       "direction_manipulation": {
                           "reversal_capability": "bidirectional_operation",
                           "speed_modulation": "variable_velocity_control",
                           "route_switching": "junction_point_override"
                       },
                       "safety_mechanisms": {
                           "emergency_stops": "system_wide_shutdown",
                           "load_distribution": "weight_balance_monitoring",
                           "collision_prevention": "proximity_sensor_network"
                       }
                   }
               }'::jsonb,
               '{
                   "system_types": {
                       "cargo_conveyor": {
                           "movement_speed": "2-5_mps",
                           "capacity_limit": "500-2000_kg",
                           "route_options": ["linear_path", "branched_network", "loop_circuit"],
                           "control_methods": ["manual_override", "system_hack", "emergency_stop"]
                       },
                       "personnel_lift": {
                           "movement_speed": "1-3_mps",
                           "capacity_limit": "4-8_persons",
                           "access_levels": ["maintenance", "operations", "executive"],
                           "security_features": ["biometric_scan", "keycard_system", "emergency_lockdown"]
                       },
                       "automated_cart": {
                           "movement_speed": "3-8_mps",
                           "capacity_limit": "200-800_kg",
                           "navigation_type": ["rail_guided", "magnetic_levitation", "autonomous_ai"],
                           "cargo_types": ["raw_materials", "finished_products", "hazardous_waste"]
                       }
                   }
               }'::jsonb,
               '{
                   "strategic_utilization": {
                       "alternative_routing": {
                           "bypass_creation": "patrol_route_disruption",
                           "shortcut_activation": "fast_traversal_options",
                           "elevation_changes": "vertical_mobility"
                       },
                       "tactical_obstacles": {
                           "blockage_induction": "enemy_movement_impediment",
                           "trap_construction": "cargo_drop_hazards",
                           "surveillance_disruption": "blind_corner_exploitation"
                       }
                   }
               }'::jsonb,
               '{
                   "conveyor_system_usage": "count",
                   "route_alteration_events": "count",
                   "blockage_incidents": "count",
                   "safety_mechanism_activations": "count"
               }'::jsonb,
               '{
                   "conveyor_belts": {
                       "mechanical_components": "geared_drive_systems",
                       "control_stations": "operator_control_panels",
                       "safety_features": "emergency_stop_buttons"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

SELECT import_interactive_object(
               'crane_manipulator',
               'Crane Manipulator',
               'Кран/Манипулятор',
               'industrial',
               'cyberpunk',
               'strategic',
               'Промышленные краны и манипуляторы для подъема и перемещения тяжелых грузов. Могут создавать укрытия, открывать пути или использоваться как импровизированное оружие.',
               '{
                   "operational_parameters": {
                       "load_handling": {
                           "attachment_methods": ["electromagnetic", "hydraulic_clamp", "cargo_net"],
                           "stability_controls": ["counterweight_system", "gyroscopic_stabilization"],
                           "safety_limits": ["overload_protection", "wind_compensation"]
                       },
                       "precision_controls": {
                           "positioning_accuracy": "centimeter_precision",
                           "speed_governing": "variable_acceleration",
                           "collision_avoidance": "laser_guidance_system"
                       }
                   }
               }'::jsonb,
               '{
                   "crane_types": {
                       "overhead_crane": {
                           "lift_capacity": "5-20_tons",
                           "reach_radius": "10-30_meters",
                           "movement_speed": "0.5-2_mps",
                           "control_precision": "medium"
                       },
                       "robotic_arm": {
                           "lift_capacity": "1-5_tons",
                           "reach_radius": "5-15_meters",
                           "movement_speed": "1-4_mps",
                           "control_precision": "high"
                       },
                       "gantry_system": {
                           "lift_capacity": "10-50_tons",
                           "reach_radius": "20-50_meters",
                           "movement_speed": "0.3-1_mps",
                           "control_precision": "low"
                       }
                   }
               }'::jsonb,
               '{
                   "environmental_interactions": {
                       "cover_creation": {
                           "cargo_suspension": "temporary_barrier_formation",
                           "container_positioning": "defensive_structure_building",
                           "equipment_arrangement": "tactical_cover_placement"
                       },
                       "path_manipulation": {
                           "obstacle_clearing": "blocked_route_opening",
                           "platform_construction": "elevated_access_creation",
                           "barrier_destruction": "structural_weakness_exploitation"
                       },
                       "combat_utilization": {
                           "improvised_weapons": "cargo_drop_attacks",
                           "area_denial": "suspended_hazard_creation",
                           "mobility_disruption": "movement_path_blocking"
                       }
                   }
               }'::jsonb,
               '{
                   "crane_operation_events": "count",
                   "load_handling_statistics": "count",
                   "environmental_manipulation": "count",
                   "safety_incident_reports": "count"
               }'::jsonb,
               '{
                   "crane_structures": {
                       "support_frameworks": "steel_girder_construction",
                       "lifting_mechanisms": "hydraulic_cylinder_assemblies",
                       "operator_cabs": "enclosed_control_stations"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- Clean up the temporary function
DROP FUNCTION import_interactive_object(TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, JSONB, JSONB, JSONB, JSONB, JSONB, JSONB, JSONB);

-- Show results
SELECT COUNT(*) as total_imported,
       COUNT(*)    FILTER (WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute') as newly_created, COUNT(*) FILTER (WHERE updated_at >= CURRENT_TIMESTAMP - INTERVAL '1 minute' AND created_at < CURRENT_TIMESTAMP - INTERVAL '1 minute') as updated
FROM gameplay.interactive_objects
WHERE object_id IN ('electrical_panel', 'valve_system', 'conveyor_system', 'crane_manipulator');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN ('electrical_panel', 'valve_system', 'conveyor_system', 'crane_manipulator')
ORDER BY object_id;