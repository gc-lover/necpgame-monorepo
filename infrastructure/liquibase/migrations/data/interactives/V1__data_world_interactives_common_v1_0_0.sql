--liquibase formatted sql

--changeset necp:data_world_interactives_common_v1_0_0 runOnChange:false
--comment: Import world common interactives data from YAML specification

-- Insert main interactive objects
INSERT INTO world_interactives (interactive_name, display_name, category, description, base_health, is_destructible, respawn_time_seconds) VALUES
('faction_blockpost', 'Блок-пост фракций', 'faction_control', 'Контрольно-пропускные пункты различных фракций и корпораций. Определяют зоны контроля, влияют на цены и доступ к территориям.', 500, true, 600),
('communication_relay', 'Ретранслятор связи', 'communication', 'Устройства связи и коммуникации для поддержания сетевой инфраструктуры. Могут быть взломаны или выведены из строя.', 300, true, 300),
('medical_station', 'Медицинская станция', 'medical', 'Медицинские пункты для лечения ранений и ремонта имплантов. Варьируются от полевых госпиталей до корпоративных клиник.', 200, false, 0),
('logistics_container', 'Логистический контейнер', 'logistics', 'Хранилища для товаров, оборудования и ресурсов. Могут содержать ценные предметы или быть пустыми.', 150, true, 1800)
ON CONFLICT (interactive_name) DO UPDATE SET
    display_name = EXCLUDED.display_name,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    base_health = EXCLUDED.base_health,
    is_destructible = EXCLUDED.is_destructible,
    respawn_time_seconds = EXCLUDED.respawn_time_seconds,
    updated_at = NOW();

-- Insert faction blockpost types
INSERT INTO interactive_types (interactive_id, type_name, variant_name, controlling_faction, control_radius_meters, price_modifier_percent, access_requirement, takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max, takeover_success_rate_min, takeover_success_rate_max, takeover_detection_risk_percent, takeover_time_seconds, takeover_alarm_probability_percent) VALUES
-- Corporate control variants
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'corporate_control', 'arasaka', 'arasaka', 750, 25, 'corporate_id_or_bribe', 'bribery', 800, 1500, 75, 85, 25, NULL, NULL),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'corporate_control', 'militech', 'militech', 800, 30, 'corporate_id_or_bribe', 'bribery', 1000, 1800, 70, 80, 30, NULL, NULL),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'corporate_control', 'biotechnica', 'biotechnica', 700, 20, 'corporate_id_or_bribe', 'bribery', 600, 1200, 80, 90, 20, NULL, NULL),

-- Gang territory variants
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'gang_territory', 'valentinos', 'valentinos', 350, 15, 'gang_loyalty_or_force', 'assault', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'gang_territory', 'maelstrom', 'maelstrom', 400, 20, 'gang_loyalty_or_force', 'assault', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'gang_territory', 'animals', 'animals', 300, 10, 'gang_loyalty_or_force', 'assault', NULL, NULL, NULL, NULL, NULL, NULL, NULL),

-- Government checkpoint variants
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'government_checkpoint', 'ncpd', 'ncpd', 600, 15, 'official_permit', 'hacking', NULL, NULL, 45, 70, 50, 120, 40),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'government_checkpoint', 'max_tac', 'max_tac', 500, 25, 'official_permit', 'hacking', NULL, NULL, 35, 60, 70, 180, 60),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'government_checkpoint', 'trauma_team', 'trauma_team', 550, 20, 'official_permit', 'hacking', NULL, NULL, 50, 75, 45, 90, 30)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    controlling_faction = EXCLUDED.controlling_faction,
    control_radius_meters = EXCLUDED.control_radius_meters,
    price_modifier_percent = EXCLUDED.price_modifier_percent,
    access_requirement = EXCLUDED.access_requirement,
    takeover_method = EXCLUDED.takeover_method,
    takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
    takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
    takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
    takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
    takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
    takeover_time_seconds = EXCLUDED.takeover_time_seconds,
    takeover_alarm_probability_percent = EXCLUDED.takeover_alarm_probability_percent,
    updated_at = NOW();

-- Insert communication relay types
INSERT INTO interactive_types (interactive_id, type_name, variant_name, signal_strength, encryption_level, jamming_resistance, bandwidth_mbps) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'corporate_network', 'executive', 95, 'military', 80, 1000),
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'corporate_network', 'standard', 85, 'advanced', 60, 500),
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'public_network', 'neighborhood', 70, 'basic', 30, 100),
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'public_network', 'district', 80, 'basic', 40, 250),
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'military_network', 'classified', 98, 'military', 95, 2000),
((SELECT id FROM world_interactives WHERE interactive_name = 'communication_relay'), 'gang_network', 'encrypted', 75, 'advanced', 50, 150)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    signal_strength = EXCLUDED.signal_strength,
    encryption_level = EXCLUDED.encryption_level,
    jamming_resistance = EXCLUDED.jamming_resistance,
    bandwidth_mbps = EXCLUDED.bandwidth_mbps,
    updated_at = NOW();

-- Insert medical station types
INSERT INTO interactive_types (interactive_id, type_name, variant_name, healing_rate_per_second, cyberware_repair, trauma_team_available) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'field_hospital', 'mobile_medical_units', 15, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'corporate_clinic', 'high-tech_medical_facilities', 25, true, true),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'street_doc', 'underground_clinics', 10, true, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'trauma_center', 'emergency_response', 30, true, true),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'ripperdoc', 'cyberware_specialists', 8, true, false)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    healing_rate_per_second = EXCLUDED.healing_rate_per_second,
    cyberware_repair = EXCLUDED.cyberware_repair,
    trauma_team_available = EXCLUDED.trauma_team_available,
    updated_at = NOW();

-- Insert logistics container types
INSERT INTO interactive_types (interactive_id, type_name, variant_name, storage_capacity, security_level, loot_quality) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'shipping_containers', 'standard_iso_containers', 1000, 'none', 'common'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'military_crates', 'reinforced_storage_units', 500, 'advanced', 'rare'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'black_market_stashes', 'hidden_underground_caches', 200, 'basic', 'uncommon'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'corporate_shipments', 'secure_transport', 750, 'military', 'epic'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'abandoned_cargo', 'forgotten_storage', 300, 'none', 'trash'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'gang_cache', 'hidden_stash', 150, 'basic', 'uncommon')
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    storage_capacity = EXCLUDED.storage_capacity,
    security_level = EXCLUDED.security_level,
    loot_quality = EXCLUDED.loot_quality,
    updated_at = NOW();

--rollback DELETE FROM interactive_types WHERE interactive_id IN (SELECT id FROM world_interactives WHERE interactive_name IN ('faction_blockpost', 'communication_relay', 'medical_station', 'logistics_container'));
--rollback DELETE FROM world_interactives WHERE interactive_name IN ('faction_blockpost', 'communication_relay', 'medical_station', 'logistics_container');