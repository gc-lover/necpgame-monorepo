--liquibase formatted sql

--changeset necp:data_underground_ghetto_interactives_v1_0_0 runOnChange:false
--comment: Import underground ghetto interactives data from YAML specification

-- Insert main underground ghetto interactive objects
INSERT INTO world_interactives (interactive_name, display_name, category, description, base_health, is_destructible, respawn_time_seconds) VALUES
('black_market_hub', 'Чёрный рынок', 'underground_economy', 'Тайные торговые точки в бэкрумах и подвалах, где обмениваются редкостями, информацией и запрещенными товарами. Места встреч подпольных дельцов и квестовых NPC.', 200, true, 600),
('improvised_lab', 'Импровизированная лаборатория', 'underground_manufacturing', 'Самодельные лаборатории в подвалах и заброшенных зданиях, где подпольные химики и технари создают экспериментальные импланты, наркотики и оружие.', 300, true, 900),
('smuggling_hatch', 'Контрабандный люк', 'underground_transport', 'Скрытые люки и тоннели для контрабанды и быстрого перемещения. Случайные встречи, засады и тайные маршруты.', 150, true, 300)
ON CONFLICT (interactive_name) DO UPDATE SET
    display_name = EXCLUDED.display_name,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    base_health = EXCLUDED.base_health,
    is_destructible = EXCLUDED.is_destructible,
    respawn_time_seconds = EXCLUDED.respawn_time_seconds,
    updated_at = NOW();

-- Insert black market hub types (using faction blockpost takeover mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max, takeover_success_rate_min, takeover_success_rate_max, takeover_detection_risk_percent, takeover_time_seconds) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'black_market_hub'), 'street_level', 'backroom_trading', 'negotiation', 100, 300, 60, 85, 20, 30),
((SELECT id FROM world_interactives WHERE interactive_name = 'black_market_hub'), 'underground_network', 'smuggler_meetup', 'negotiation', 200, 500, 45, 70, 35, 60),
((SELECT id FROM world_interactives WHERE interactive_name = 'black_market_hub'), 'exclusive_club', 'elite_contacts', 'negotiation', 500, 1500, 25, 50, 60, 120)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    takeover_method = EXCLUDED.takeover_method,
    takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
    takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
    takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
    takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
    takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
    takeover_time_seconds = EXCLUDED.takeover_time_seconds,
    updated_at = NOW();

-- Insert improvised lab types (using medical station schema for crafting)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, healing_rate_per_second, cyberware_repair, trauma_team_available) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'improvised_lab'), 'chemical_lab', 'drug_synthesis', 0, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'improvised_lab'), 'cyber_lab', 'implant_modding', 0, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'improvised_lab'), 'weapon_lab', 'illegal_firearms', 0, false, false)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    healing_rate_per_second = EXCLUDED.healing_rate_per_second,
    cyberware_repair = EXCLUDED.cyberware_repair,
    trauma_team_available = EXCLUDED.trauma_team_available,
    updated_at = NOW();

-- Insert smuggling hatch types (using logistics container schema)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, storage_capacity, security_level, loot_quality) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'smuggling_hatch'), 'maintenance_tunnel', 'sewer_access', 3, 'low', 'common'),
((SELECT id FROM world_interactives WHERE interactive_name = 'smuggling_hatch'), 'abandoned_metro', 'subway_escape', 5, 'medium', 'uncommon'),
((SELECT id FROM world_interactives WHERE interactive_name = 'smuggling_hatch'), 'hidden_passage', 'roof_access', 2, 'high', 'rare')
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    storage_capacity = EXCLUDED.storage_capacity,
    security_level = EXCLUDED.security_level,
    loot_quality = EXCLUDED.loot_quality,
    updated_at = NOW();

--rollback DELETE FROM interactive_types WHERE interactive_id IN (SELECT id FROM world_interactives WHERE interactive_name IN ('black_market_hub', 'improvised_lab', 'smuggling_hatch'));
--rollback DELETE FROM world_interactives WHERE interactive_name IN ('black_market_hub', 'improvised_lab', 'smuggling_hatch');