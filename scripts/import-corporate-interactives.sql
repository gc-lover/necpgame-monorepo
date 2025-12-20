-- Issue: #1841
-- SQL script to import corporate interactive objects into database

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

-- Import corporate interactive objects
SELECT import_interactive_object(
               'server_rack',
               'Server Rack',
               'Серверная стойка',
               'corporate',
               'cyberpunk',
               'tactical',
               'Корпоративные серверные системы с ценными данными. Взлом дает доступ к конфиденциальной информации, но активирует ICE-защиту.',
               '{
                   "data_extraction": {
                       "download_methods": ["quick_dump", "encrypted_transfer", "fragmented_upload"],
                       "data_categories": {
                           "financial_records": "company_finances_and_transactions",
                           "employee_data": "personnel_records_and_salaries",
                           "proprietary_tech": "patented_technologies_and_formulas",
                           "competitor_intel": "rival_company_information"
                       }
                   }
               }'::jsonb,
               '{
                   "server_types": {
                       "corporate_database": {
                           "data_value": "high",
                           "ice_rating": "3-6",
                           "access_time": "45-120_seconds",
                           "alarm_risk": "25-50%"
                       },
                       "executive_records": {
                           "data_value": "premium",
                           "ice_rating": "5-9",
                           "access_time": "90-180_seconds",
                           "alarm_risk": "35-65%"
                       },
                       "research_data": {
                           "data_value": "legendary",
                           "ice_rating": "7-12",
                           "access_time": "120-240_seconds",
                           "alarm_risk": "50-80%"
                       }
                   }
               }'::jsonb,
               '{
                   "ice_protection": {
                       "defense_mechanisms": {
                           "pattern_recognition": "adaptive_intrusion_detection",
                           "counter_hacking": "automated_response_systems",
                           "data_corruption": "self_destruct_protocols"
                       },
                       "breach_consequences": {
                           "alarm_activation": "security_team_dispatch",
                           "data_lockdown": "system_isolation",
                           "trace_routing": "intruder_location_tracking"
                       }
                   }
               }'::jsonb,
               '{
                   "server_breaches_attempted": "count",
                   "data_extraction_success_rates": "count",
                   "ice_countermeasures_triggered": "count",
                   "alarm_system_activations": "count"
               }'::jsonb,
               '{
                   "server_racks": {
                       "hardware_aesthetics": "sleek_modular_server_cabinets",
                       "activity_indicators": "blinking_led_status_lights",
                       "cooling_systems": "humming_fan_arrays",
                       "access_panels": "secured_maintenance_doors"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{
                   "corporate_security_intensity": {
                       "low_security_zones": {
                           "interactive_frequency": 0.8,
                           "security_complexity": "basic",
                           "reward_value": "standard",
                           "detection_risk": "low"
                       },
                       "standard_facilities": {
                           "interactive_frequency": 1.0,
                           "security_complexity": "advanced",
                           "reward_value": "premium",
                           "detection_risk": "medium"
                       },
                       "high_security_complexes": {
                           "interactive_frequency": 1.2,
                           "security_complexity": "elite",
                           "reward_value": "legendary",
                           "detection_risk": "high"
                       }
                   }
               }'::jsonb
       );

SELECT import_interactive_object(
               'biometric_lock',
               'Biometric Lock',
               'Биометрический замок',
               'corporate',
               'cyberpunk',
               'strategic',
               'Высокотехнологичные замки с биометрической аутентификацией. Могут быть обмануты через социальный инжиниринг, маски или хакинг.',
               '{
                   "bypass_methods": {
                       "social_engineering": {
                           "disguise_techniques": {
                               "employee_impersonation": "forged_credentials",
                               "authority_figure_roleplay": "supervisor_override",
                               "emergency_scenario": "crisis_exploitation"
                           },
                           "success_factors": ["acting_skill", "timing", "confidence_level"]
                       },
                       "hardware_bypass": {
                           "device_types": {
                               "spoofing_implant": "biometric_signal_emulation",
                               "override_chip": "control_system_hack",
                               "power_interruption": "emergency_access_activation"
                           },
                           "technical_requirements": ["specialized_tools", "expertise_level", "time_available"]
                       },
                       "cyber_intrusion": {
                           "hack_approaches": {
                               "system_compromise": "network_infiltration",
                               "firmware_exploit": "software_vulnerability",
                               "physical_jack": "direct_hardware_access"
                           },
                           "digital_risks": ["trace_detection", "counter_hack_response", "data_corruption"]
                       }
                   }
               }'::jsonb,
               '{
                   "lock_types": {
                       "executive_suite": {
                           "security_level": "high",
                           "access_requirements": ["retinal_scan", "voice_print", "dna_sample"],
                           "bypass_difficulty": "medium-high",
                           "reward_quality": "premium"
                       },
                       "research_lab": {
                           "security_level": "very_high",
                           "access_requirements": ["neural_imprint", "blood_sample", "behavioral_pattern"],
                           "bypass_difficulty": "high",
                           "reward_quality": "legendary"
                       },
                       "vault_access": {
                           "security_level": "maximum",
                           "access_requirements": ["multi_factor_auth", "real_time_monitoring", "ai_supervision"],
                           "bypass_difficulty": "extreme",
                           "reward_quality": "unique"
                       }
                   }
               }'::jsonb,
               '{
                   "security_responses": {
                       "detection_measures": {
                           "anomaly_monitoring": "behavioral_pattern_analysis",
                           "biometric_verification": "real_time_validation",
                           "environmental_scanning": "contextual_assessment"
                       },
                       "alarm_triggers": {
                           "failed_attempts_threshold": "attempt_limit_exceeded",
                           "suspicious_behavior": "anomaly_detected",
                           "system_compromise": "integrity_breach"
                       }
                   }
               }'::jsonb,
               '{
                   "biometric_bypass_attempts": "count",
                   "access_grant_success_rates": "count",
                   "security_alarm_triggers": "count",
                   "method_effectiveness_tracking": "count"
               }'::jsonb,
               '{
                   "biometric_locks": {
                       "scanning_interfaces": "futuristic_biometric_readers",
                       "authentication_sounds": "soft_beep_confirmation_tones",
                       "security_warnings": "stern_voice_alerts",
                       "bypass_indicators": "subtle_status_leds"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

SELECT import_interactive_object(
               'corporate_safe',
               'Corporate Safe',
               'Корпоративный сейф/Датаволт',
               'corporate',
               'cyberpunk',
               'dangerous',
               'Многоуровневые системы хранения с редкими чертежами и документами. Требуют многоступенчатого взлома с высоким риском обнаружения.',
               '{
                   "breach_mechanics": {
                       "layer_progression": {
                           "outer_defenses": "physical_barriers_and_locks",
                           "electronic_security": "digital_encryption_and_sensors",
                           "final_containment": "redundant_backup_systems",
                           "content_protection": "self_destruct_mechanisms"
                       },
                       "breach_techniques": {
                           "mechanical_override": {
                               "lockpicking_skills": "precision_manipulation",
                               "cutting_tools": "thermal_or_laser_cutting",
                               "explosive_breaching": "controlled_demolition"
                           },
                           "electronic_bypass": {
                               "circuit_hacking": "wiring_manipulation",
                               "code_cracking": "combination_discovery",
                               "power_manipulation": "system_overload_or_shutdown"
                           },
                           "digital_intrusion": {
                               "network_compromise": "remote_access_exploit",
                               "firmware_reprogramming": "control_system_override",
                               "data_decryption": "encryption_breaking"
                           }
                       }
                   }
               }'::jsonb,
               '{
                   "safe_types": {
                       "executive_vault": {
                           "security_layers": 3,
                           "contents_rarity": "high",
                           "breach_time": "180-300_seconds",
                           "detection_probability": "40-70%"
                       },
                       "research_archive": {
                           "security_layers": 4,
                           "contents_rarity": "premium",
                           "breach_time": "240-420_seconds",
                           "detection_probability": "50-80%"
                       },
                       "prototype_storage": {
                           "security_layers": 5,
                           "contents_rarity": "legendary",
                           "breach_time": "300-600_seconds",
                           "detection_probability": "60-90%"
                       }
                   }
               }'::jsonb,
               '{
                   "content_inventory": {
                       "document_types": {
                           "technical_blueprints": "product_designs_and_schematics",
                           "financial_reports": "company_performance_data",
                           "personnel_files": "executive_background_information",
                           "strategic_plans": "corporate_development_roadmaps"
                       },
                       "artifact_categories": {
                           "prototype_components": "cutting_edge_technology_parts",
                           "experimental_devices": "unreleased_product_prototypes",
                           "corporate_secrets": "competitively_sensitive_information",
                           "historical_records": "company_evolution_documentation"
                       }
                   }
               }'::jsonb,
               '{
                   "safe_breach_attempts": "count",
                   "layer_penetration_success": "count",
                   "content_extraction_rates": "count",
                   "security_response_activations": "count"
               }'::jsonb,
               '{
                   "corporate_safes": {
                       "vault_construction": "reinforced_composite_materials",
                       "locking_mechanisms": "complex_dial_wheel_combinations",
                       "alarm_systems": "piercing_siren_wails",
                       "access_denied": "heavy_metal_clangs"
                   }
               }'::jsonb,
               '{}'::jsonb,
               '{}'::jsonb
       );

SELECT import_interactive_object(
               'conference_system',
               'Conference System',
               'Конференц-система',
               'corporate',
               'cyberpunk',
               'tactical',
               'Корпоративные системы конференц-связи для перехвата разговоров. Позволяют записывать компрометирующие материалы и стратегическую информацию.',
               '{
                   "intelligence_gathering": {
                       "content_categories": {
                           "strategic_discussions": "corporate_strategy_reveals",
                           "personnel_matters": "executive_decisions_and_changes",
                           "financial_planning": "budget_allocations_and_forecasts",
                           "competitive_intel": "rival_analysis_and_countermeasures"
                       },
                       "recording_quality": {
                           "audio_clarity": "conversation_transcription_accuracy",
                           "video_resolution": "visual_evidence_quality",
                           "metadata_capture": "participant_identification",
                           "timestamp_accuracy": "event_timeline_reconstruction"
                       }
                   }
               }'::jsonb,
               '{
                   "system_types": {
                       "boardroom_setup": {
                           "participant_count": "8-12",
                           "recording_quality": "high",
                           "security_level": "medium",
                           "intel_value": "strategic"
                       },
                       "executive_call": {
                           "participant_count": "4-6",
                           "recording_quality": "premium",
                           "security_level": "high",
                           "intel_value": "critical"
                       },
                       "remote_meeting": {
                           "participant_count": "10-20",
                           "recording_quality": "variable",
                           "security_level": "low-medium",
                           "intel_value": "operational"
                       }
                   }
               }'::jsonb,
               '{
                   "surveillance_mechanics": {
                       "access_methods": {
                           "physical_tapping": {
                               "device_placement": "conference_room_hardware",
                               "signal_interception": "audio_video_capture",
                               "transmission_setup": "data_relay_establishment"
                           },
                           "network_infiltration": {
                               "system_compromise": "meeting_platform_hack",
                               "participant_tracking": "user_monitoring",
                               "data_stream_capture": "encrypted_channel_breaking"
                           },
                           "environmental_monitoring": {
                               "hidden_devices": "room_bugging",
                               "external_microphones": "window_wall_penetration",
                               "signal_analysis": "frequency_scanning"
                           }
                       }
                   },
                   "operational_risks": {
                       "detection_vectors": {
                           "system_monitoring": "anomaly_detection_systems",
                           "participant_awareness": "suspicious_behavior_alerts",
                           "security_scans": "regular_vulnerability_checks",
                           "forensic_analysis": "post_incident_investigations"
                       },
                       "countermeasures": {
                           "encryption_protocols": "end_to_end_security",
                           "monitoring_disruption": "signal_jamming_techniques",
                           "physical_barriers": "tamper_proof_installations",
                           "ai_detection": "automated_threat_identification"
                       }
                   }
               }'::jsonb,
               '{
                   "conference_surveillance_attempts": "count",
                   "intelligence_capture_success": "count",
                   "detection_avoidance_rates": "count",
                   "information_value_assessment": "count"
               }'::jsonb,
               '{
                   "conference_systems": {
                       "room_environments": "sterile_executive_boardrooms",
                       "equipment_layout": "integrated_audio_visual_setup",
                       "participant_interactions": "professional_business_discussions",
                       "surveillance_hints": "subtle_recording_indicators"
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
WHERE object_id IN ('server_rack', 'biometric_lock', 'corporate_safe', 'conference_system');

-- Show imported objects
SELECT object_id, name, category, threat_level, created_at, updated_at
FROM gameplay.interactive_objects
WHERE object_id IN ('server_rack', 'biometric_lock', 'corporate_safe', 'conference_system')
ORDER BY object_id;