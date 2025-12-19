--liquibase formatted sql

--changeset necp:data_world_common_interactives_v1_0_0 runOnChange:false
--comment: Import world common interactives data from YAML specification

-- Insert main world common interactive objects
INSERT INTO world_interactives (interactive_name, display_name, category, description, base_health, is_destructible, respawn_time_seconds) VALUES
('faction_blockpost', 'Блок-пост фракций', 'faction_control', 'Контрольно-пропускные пункты различных фракций и корпораций. Определяют зоны контроля, влияют на цены и доступ к территориям.', 500, true, 600),
('comm_relay', 'Коммуникационный ретранслятор', 'communication', 'Устройства усиления связи и разведки. Увеличивают радиус связи сквада, раскрывают карту района, но привлекают патрули.', 200, true, 300),
('medical_station', 'Медицинская станция', 'medical', 'Автоматизированные медицинские пункты с ограниченными зарядами. Быстрое лечение, но шум привлекает врагов.', 100, true, 1800),
('logistics_container', 'Логистический контейнер', 'logistics', 'Транспортные контейнеры с грузом. Могут содержать ценные ресурсы или ловушки/сигналки/мины.', 150, true, 900)
ON CONFLICT (interactive_name) DO UPDATE SET
    display_name = EXCLUDED.display_name,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    base_health = EXCLUDED.base_health,
    is_destructible = EXCLUDED.is_destructible,
    respawn_time_seconds = EXCLUDED.respawn_time_seconds,
    updated_at = NOW();

-- Insert faction blockpost types (using faction blockpost takeover mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max, takeover_success_rate_min, takeover_success_rate_max, takeover_detection_risk_percent, takeover_time_seconds) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'corporate_checkpoint', 'arasaka_control', 'bribery', 1500, 3000, 60, 85, 40, 120),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'gang_outpost', 'maelstrom_territory', 'assault', 200, 800, 70, 95, 25, 90),
((SELECT id FROM world_interactives WHERE interactive_name = 'faction_blockpost'), 'government_barrier', 'ncpd_checkpoint', 'hacking', 300, 1000, 50, 80, 70, 180)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    takeover_method = EXCLUDED.takeover_method,
    takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
    takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
    takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
    takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
    takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
    takeover_time_seconds = EXCLUDED.takeover_time_seconds,
    updated_at = NOW();

-- Insert comm relay types (using comm relay signal mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, signal_strength, encryption_level, jamming_resistance, bandwidth_mbps) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'comm_relay'), 'urban_comms', 'city_center', 75, 'standard', 50, 100),
((SELECT id FROM world_interactives WHERE interactive_name = 'comm_relay'), 'highway_relay', 'interstate', 60, 'standard', 40, 75),
((SELECT id FROM world_interactives WHERE interactive_name = 'comm_relay'), 'remote_outpost', 'desert', 80, 'advanced', 60, 125)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    signal_strength = EXCLUDED.signal_strength,
    encryption_level = EXCLUDED.encryption_level,
    jamming_resistance = EXCLUDED.jamming_resistance,
    bandwidth_mbps = EXCLUDED.bandwidth_mbps,
    updated_at = NOW();

-- Insert medical station types (using medical station healing mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, healing_rate_per_second, cyberware_repair, trauma_team_available) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'urban_clinic', 'city_medical', 10, true, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'emergency_post', 'battlefield_medical', 15, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'medical_station'), 'corporate_medical', 'executive_care', 20, true, true)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    healing_rate_per_second = EXCLUDED.healing_rate_per_second,
    cyberware_repair = EXCLUDED.cyberware_repair,
    trauma_team_available = EXCLUDED.trauma_team_available,
    updated_at = NOW();

-- Insert logistics container types (using logistics container cargo mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, storage_capacity, security_level, loot_quality) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'standard_shipping', 'commercial_cargo', 8, 'low', 'common'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'military_transport', 'ordnance_supply', 12, 'high', 'rare'),
((SELECT id FROM world_interactives WHERE interactive_name = 'logistics_container'), 'black_market_cargo', 'smuggled_goods', 6, 'medium', 'epic')
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    storage_capacity = EXCLUDED.storage_capacity,
    security_level = EXCLUDED.security_level,
    loot_quality = EXCLUDED.loot_quality,
    updated_at = NOW();

--rollback DELETE FROM interactive_types WHERE interactive_id IN (SELECT id FROM world_interactives WHERE interactive_name IN ('faction_blockpost', 'comm_relay', 'medical_station', 'logistics_container'));
--rollback DELETE FROM world_interactives WHERE interactive_name IN ('faction_blockpost', 'comm_relay', 'medical_station', 'logistics_container');