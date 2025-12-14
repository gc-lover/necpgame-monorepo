--liquibase formatted sql

--changeset necp:data_corporate_interactives_v1_0_0 runOnChange:false
--comment: Import corporate interactives data from YAML specification

-- Insert main corporate interactive objects
INSERT INTO world_interactives (interactive_name, display_name, category, description, base_health, is_destructible, respawn_time_seconds) VALUES
('server_rack', 'Серверная стойка', 'data_storage', 'Корпоративные серверные системы с ценными данными. Взлом дает доступ к конфиденциальной информации, но активирует ICE-защиту.', 400, true, 300),
('biometric_lock', 'Биометрический замок', 'access_control', 'Системы контроля доступа с биометрической аутентификацией. Требуют специальных инструментов или социального инжиниринга.', 100, true, 120),
('corporate_safe', 'Корпоративный сейф', 'secure_storage', 'Защищенные хранилища для ценных предметов и документов. Многоступенчатая защита и высокие риски обнаружения.', 800, true, 900),
('conference_system', 'Конференц-система', 'surveillance', 'Корпоративные системы связи и видеоконференций. Могут быть использованы для шпионажа или компрометации.', 150, true, 180)
ON CONFLICT (interactive_name) DO UPDATE SET
    display_name = EXCLUDED.display_name,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    base_health = EXCLUDED.base_health,
    is_destructible = EXCLUDED.is_destructible,
    respawn_time_seconds = EXCLUDED.respawn_time_seconds,
    updated_at = NOW();

-- Insert server rack types (using communication relay schema for ICE/data)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, signal_strength, encryption_level, jamming_resistance, bandwidth_mbps) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'server_rack'), 'corporate_database', 'financial_records', 85, 'advanced', 75, 100),
((SELECT id FROM world_interactives WHERE interactive_name = 'server_rack'), 'executive_records', 'personnel_data', 95, 'military', 85, 150),
((SELECT id FROM world_interactives WHERE interactive_name = 'server_rack'), 'research_data', 'proprietary_tech', 98, 'military', 90, 200)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    signal_strength = EXCLUDED.signal_strength,
    encryption_level = EXCLUDED.encryption_level,
    jamming_resistance = EXCLUDED.jamming_resistance,
    bandwidth_mbps = EXCLUDED.bandwidth_mbps,
    updated_at = NOW();

-- Insert biometric lock types (using faction blockpost takeover mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max, takeover_success_rate_min, takeover_success_rate_max, takeover_detection_risk_percent, takeover_time_seconds) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'biometric_lock'), 'retinal_scanner', 'executive_access', 'hacking', 500, 1000, 30, 60, 70, 60),
((SELECT id FROM world_interactives WHERE interactive_name = 'biometric_lock'), 'dna_sampler', 'lab_security', 'hacking', 800, 1500, 20, 45, 80, 120),
((SELECT id FROM world_interactives WHERE interactive_name = 'biometric_lock'), 'neural_interface', 'ai_core', 'hacking', 1200, 2500, 10, 30, 90, 180)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    takeover_method = EXCLUDED.takeover_method,
    takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
    takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
    takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
    takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
    takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
    takeover_time_seconds = EXCLUDED.takeover_time_seconds,
    updated_at = NOW();

-- Insert corporate safe types (using logistics container schema)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, storage_capacity, security_level, loot_quality) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'corporate_safe'), 'executive_vault', 'high_security', 10, 'military', 'legendary'),
((SELECT id FROM world_interactives WHERE interactive_name = 'corporate_safe'), 'research_lockup', 'classified_storage', 5, 'military', 'epic'),
((SELECT id FROM world_interactives WHERE interactive_name = 'corporate_safe'), 'treasury_deposit', 'company_funds', 50, 'advanced', 'rare')
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    storage_capacity = EXCLUDED.storage_capacity,
    security_level = EXCLUDED.security_level,
    loot_quality = EXCLUDED.loot_quality,
    updated_at = NOW();

-- Insert conference system types (using medical station schema for surveillance)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, healing_rate_per_second, cyberware_repair, trauma_team_available) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'conference_system'), 'board_room', 'executive_meetings', 0, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'conference_system'), 'security_briefing', 'classified_discussions', 0, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'conference_system'), 'negotiation_suite', 'business_deals', 0, false, false)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    healing_rate_per_second = EXCLUDED.healing_rate_per_second,
    cyberware_repair = EXCLUDED.cyberware_repair,
    trauma_team_available = EXCLUDED.trauma_team_available,
    updated_at = NOW();

--rollback DELETE FROM interactive_types WHERE interactive_id IN (SELECT id FROM world_interactives WHERE interactive_name IN ('server_rack', 'biometric_lock', 'corporate_safe', 'conference_system'));
--rollback DELETE FROM world_interactives WHERE interactive_name IN ('server_rack', 'biometric_lock', 'corporate_safe', 'conference_system');