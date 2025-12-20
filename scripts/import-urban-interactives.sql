-- Issue: #1857
-- SQL script to import urban interactive objects into database

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
            version,
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
            '{}'::jsonb, -- location_data (empty for now)
            p_interaction_data,
            p_effects_data,
            p_telemetry_data,
            p_visual_data,
            p_audio_data,
            p_balance_data,
            true,
            '1.0.0',
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        ) RETURNING id INTO v_id;

        RAISE
NOTICE 'Inserted new interactive object: % (%)', p_name, p_object_id;
ELSE
        -- Update existing record
UPDATE gameplay.interactive_objects
SET name             = p_name,
    display_name     = p_display_name,
    category         = p_category,
    era              = p_era,
    threat_level     = p_threat_level,
    description      = p_description,
    interaction_data = p_interaction_data,
    effects_data     = p_effects_data,
    telemetry_data   = p_telemetry_data,
    visual_data      = p_visual_data,
    audio_data       = p_audio_data,
    balance_data     = p_balance_data,
    updated_at       = CURRENT_TIMESTAMP
WHERE id = v_id;

RAISE
NOTICE 'Updated existing interactive object: % (%)', p_name, p_object_id;
END IF;

RETURN v_id;
END;
$$
LANGUAGE plpgsql;

-- Import urban interactive objects from the YAML data
-- Street Terminal
SELECT import_interactive_object(
               'street_terminal',
               'Street Terminal',
               'Уличный Терминал/Банкомат',
               'urban_data_access',
               '2020-2093',
               'utility',
               'Публичные терминалы для доступа к информации, переводам и услугам. Взлом дает доступ к данным, кредитам или черному рынку. Высокий риск привлечения патрулей или корпоративной безопасности.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "interaction",
                       "method": "hack_or_manual",
                       "difficulty": "low",
                       "time_seconds": 3
                   },
                   "effects": {
                       "data_access": {
                           "data_types": ["personal_records", "financial_data", "market_info"],
                           "alarm_chance": 25,
                           "loot_tables": ["small_credits", "data_chips", "access_codes"]
                       },
                       "security_alert": {
                           "radius_meters": 100,
                           "patrol_type": "street_cops",
                           "delay_seconds": 15
                       },
                       "black_market": {
                           "services": ["weapon_deals", "info_broker", "fake_id"],
                           "risk_level": "medium"
                       }
                   }
               }'::jsonb,
               '{
                   "data_access": {
                       "type": "information_gain",
                       "data_types": ["personal_records", "financial_data", "market_info"],
                       "alarm_chance": 0.25,
                       "loot_tables": ["small_credits", "data_chips", "access_codes"]
                   },
                   "security_alert": {
                       "type": "patrol_attraction",
                       "radius": 100,
                       "patrol_type": "street_cops",
                       "delay": 15
                   },
                   "black_market": {
                       "type": "special_service",
                       "services": ["weapon_deals", "info_broker", "fake_id"],
                       "risk_level": "medium"
                   }
               }'::jsonb,
               '{
                   "terminal_accesses": "count",
                   "alarm_triggers": "count",
                   "data_thefts": "count"
               }'::jsonb,
               '{
                   "street_terminal": {
                       "model": "urban_terminal_unit",
                       "animations": ["screen_boot", "data_transfer", "alarm_trigger"],
                       "sounds": ["button_presses", "data_beeps", "alarm_siren"]
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- AR Billboard
SELECT import_interactive_object(
               'ar_billboard',
               'AR Billboard',
               'AR-Борд/Реклама',
               'urban_information',
               '2020-2093',
               'utility',
               'Гигантские рекламные панели с дополненной реальностью. Содержат скрытые коды, квестовые подсказки или баффы восприятия. Могут быть взломаны для изменения контента или получения данных.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "interaction",
                       "method": "scan_or_hack",
                       "difficulty": "low",
                       "time_seconds": 2
                   }
               }'::jsonb,
               '{
                   "hidden_codes": {
                       "type": "quest_progression",
                       "code_types": ["access_codes", "coordinates", "passwords"],
                       "rarity_chance": 0.15,
                       "quest_triggers": ["side_missions", "lore_reveals"]
                   },
                   "perception_buff": {
                       "type": "temporary_upgrade",
                       "duration": 300,
                       "effects": ["enemy_highlight", "item_detection", "weakpoint_reveal"],
                       "stack_limit": 3
                   },
                   "content_hack": {
                       "type": "environmental_change",
                       "hack_effects": ["propaganda_override", "emergency_broadcast", "distraction_signal"],
                       "duration": 60
                   }
               }'::jsonb,
               '{
                   "code_discoveries": "count",
                   "buff_activations": "count",
                   "content_hacks": "count"
               }'::jsonb,
               '{
                   "ar_billboard": {
                       "model": "holographic_display_system",
                       "animations": ["ad_rotation", "code_reveal", "hack_glitch"],
                       "sounds": ["holographic_hum", "code_beep", "content_change"]
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- Access Door
SELECT import_interactive_object(
               'access_door',
               'Access Door',
               'Дверь с Уровнем Доступа',
               'urban_security',
               '2020-2093',
               'strategic',
               'Двери с различными уровнями безопасности от простых замков до биометрии. Могут быть открыты взломом, ключами, социальной инженерией или силой. Содержат разные типы лута в зависимости от уровня доступа.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "interaction",
                       "method": "multiple_methods",
                       "difficulty": "variable",
                       "time_seconds": "5-15"
                   }
               }'::jsonb,
               '{
                   "access_tiers": {
                       "type": "loot_scaling",
                       "tier_1": ["basic_supplies", "small_credits"],
                       "tier_2": ["valuable_data", "mid_credits", "weapons"],
                       "tier_3": ["rare_artifacts", "high_credits", "prototypes"]
                   },
                   "security_bypass": {
                       "type": "zone_unlock",
                       "unlocks": ["new_areas", "elevators", "service_tunnels"],
                       "alarm_chance": 0.4
                   },
                   "social_engineering": {
                       "type": "alternative_access",
                       "methods": ["disguise", "fake_id", "intimidation"],
                       "success_modifiers": ["charisma", "reputation", "contacts"]
                   }
               }'::jsonb,
               '{
                   "door_breaches": "count",
                   "loot_obtained": "count",
                   "security_breaches": "count"
               }'::jsonb,
               '{
                   "access_door": {
                       "model": "security_door_variants",
                       "animations": ["lock_mechanism", "access_granted", "breach_alarm"],
                       "sounds": ["lock_click", "door_hiss", "alarm_blare"]
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- Delivery Drone
SELECT import_interactive_object(
               'delivery_drone',
               'Delivery Drone',
               'Дрон-Поставщик',
               'urban_mobility',
               '2020-2093',
               'tactical',
               'Автономные дроны доставки, летающие по предустановленным маршрутам. Могут быть сбиты для получения груза или взломаны для изменения маршрута. Взломанный дрон может привлечь патруль или доставить взрывчатку.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "combat_interaction",
                       "method": "shoot_down_or_hack",
                       "difficulty": "medium",
                       "time_seconds": 1
                   }
               }'::jsonb,
               '{
                   "cargo_drop": {
                       "type": "loot_spawn",
                       "loot_types": ["delivery_packages", "contraband", "medical_supplies"],
                       "destruction_chance": 0.3
                   },
                   "route_hack": {
                       "type": "patrol_disruption",
                       "effects": ["wrong_delivery", "patrol_attraction", "explosive_delivery"],
                       "hack_difficulty": "high"
                   },
                   "emergency_mode": {
                       "type": "defensive_response",
                       "triggers_alarm": true,
                       "calls_backup_chance": 0.5,
                       "self_destruct_damage": 100
                   }
               }'::jsonb,
               '{
                   "drone_interceptions": "count",
                   "cargo_recoveries": "count",
                   "route_disruptions": "count"
               }'::jsonb,
               '{
                   "delivery_drone": {
                       "model": "urban_delivery_quadcopter",
                       "animations": ["flight_pattern", "cargo_drop", "emergency_lights"],
                       "sounds": ["propeller_whir", "package_drop", "distress_signal"]
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- Garbage Chute
SELECT import_interactive_object(
               'garbage_chute',
               'Garbage Chute',
               'Мусоропровод/Контейнер',
               'urban_navigation',
               '2020-2093',
               'utility',
               'Системы утилизации отходов, часто используемые как тайные пути. Содержат тайники, черный рынок предметов или доступ к подполью. Могут быть опасными из-за токсичных веществ или крыс.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "exploration",
                       "method": "manual_interaction",
                       "difficulty": "low",
                       "time_seconds": 4
                   }
               }'::jsonb,
               '{
                   "hidden_stashes": {
                       "type": "loot_cache",
                       "cache_types": ["street_kid_supplies", "black_market_goods", "forgotten_valuables"],
                       "discovery_chance": 0.35
                   },
                   "underground_access": {
                       "type": "secret_path",
                       "leads_to": ["maintenance_tunnels", "smuggler_routes", "rat_nests"],
                       "danger_level": "medium"
                   },
                   "environmental_hazard": {
                       "type": "random_event",
                       "hazards": ["toxic_waste", "mutant_rats", "structural_collapse"],
                       "damage_range": "50-150"
                   }
               }'::jsonb,
               '{
                   "stash_discoveries": "count",
                   "path_utilizations": "count",
                   "hazard_encounters": "count"
               }'::jsonb,
               '{
                   "garbage_chute": {
                       "model": "industrial_waste_system",
                       "animations": ["chute_opening", "item_reveal", "hazard_leak"],
                       "sounds": ["metal_clang", "waste_shift", "hazard_hiss"]
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

-- Security Camera
SELECT import_interactive_object(
               'security_camera',
               'Security Camera',
               'Камера/Сеть Наблюдения',
               'urban_surveillance',
               '2020-2093',
               'strategic',
               'Системы видеонаблюдения с ИИ распознаванием лиц и паттернов. Могут быть взломаны для получения видео, отключения тревоги или маркировки целей. Перегрузка сети вызывает ложные срабатывания.',
               '{}'::jsonb,
               '{
                   "activation": {
                       "type": "stealth_interaction",
                       "method": "hack_or_emf",
                       "difficulty": "medium",
                       "time_seconds": 6
                   }
               }'::jsonb,
               '{
                   "surveillance_override": {
                       "type": "security_control",
                       "effects": ["blind_spots", "false_alarms", "target_marking"],
                       "duration": 120
                   },
                   "feed_hijack": {
                       "type": "information_gain",
                       "reveals": ["patrol_routes", "vip_locations", "security_weakpoints"],
                       "intel_quality": "high"
                   },
                   "network_overload": {
                       "type": "area_disruption",
                       "radius": 50,
                       "effects": ["camera_blackout", "false_alerts", "system_crash"],
                       "recovery_time": 30
                   }
               }'::jsonb,
               '{
                   "surveillance_breaches": "count",
                   "intel_gathered": "count",
                   "network_disruptions": "count"
               }'::jsonb,
               '{
                   "security_camera": {
                       "model": "surveillance_camera_array",
                       "animations": ["pan_tilt", "zoom_focus", "system_glitch"],
                       "sounds": ["servo_whir", "lens_focus", "error_beep"]
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
WHERE object_id IN
      ('street_terminal', 'ar_billboard', 'access_door', 'delivery_drone', 'garbage_chute', 'security_camera');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN
      ('street_terminal', 'ar_billboard', 'access_door', 'delivery_drone', 'garbage_chute', 'security_camera')
ORDER BY object_id;