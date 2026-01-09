-- Import Seattle quests 017-039 into gameplay.quests table
-- Generated from V1_84
-- Issue: #2273

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Seattle 2020-2029 - Ghost in the Cloud',
    'Navigate Seattle',
    12,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-017-ghost-in-the-cloud", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-017-ghost-in-the-cloud.yaml"}'::jsonb,
    '{"experience": 3800, "money": 650, "items": [{"item_id": "privacy_toolkit", "quantity": 1}, {"item_id": "anonymous_communication_device", "quantity": 1}], "reputation": {"privacy_activists": 75, "corporations": -70, "underground_hackers": 55}, "unlocks": ["surveillance_detection_tools", "secure_communication_network", "whistleblower_protection_program"]}'::jsonb,
    '[{"id": "meet_whistleblower", "text": "Meet the whistleblower in a secure location and learn about surveillance threats", "type": "interact", "target": "whistleblower_contact", "count": 1, "optional": false}, {"id": "gather_evidence", "text": "Gather evidence of corporate surveillance systems in Seattle's tech district", "type": "investigate", "target": "surveillance_evidence", "count": 5, "optional": false}, {"id": "hack_surveillance_nodes", "text": "Hack into surveillance network nodes to disrupt monitoring", "type": "hack", "target": "surveillance_nodes", "count": 3, "optional": false}, {"id": "protect_whistleblower", "text": "Protect the whistleblower from corporate retaliation", "type": "defend", "target": "whistleblower_safety", "count": 1, "optional": false}, {"id": "establish_secure_network", "text": "Establish secure communication networks for privacy activists", "type": "build", "target": "secure_network", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Seattle 2020-2029 - Pacific Gateway',
    'Manage Seattle',
    15,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-018-pacific-gateway", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-018-pacific-gateway.yaml"}'::jsonb,
    '{"experience": 4200, "money": 1200, "items": [{"item_id": "international_trade_license", "quantity": 1}, {"item_id": "port_access_credentials", "quantity": 1}], "reputation": {"port_authority": 60, "international_traders": 70, "dock_workers": 50, "corporations": 45}, "unlocks": ["international_trade_network", "port_operations_access", "supply_chain_management_tools"]}'::jsonb,
    '[{"id": "assess_port_operations", "text": "Assess current port operations and identify efficiency bottlenecks", "type": "investigate", "target": "port_operations", "count": 1, "optional": false}, {"id": "negotiate_trade_deals", "text": "Negotiate trade agreements with international partners", "type": "diplomacy", "target": "trade_negotiations", "count": 3, "optional": false}, {"id": "resolve_labor_dispute", "text": "Resolve labor disputes between dock workers and port management", "type": "mediate", "target": "labor_conflicts", "count": 1, "optional": false}, {"id": "manage_supply_chain", "text": "Manage disrupted supply chains during international crisis", "type": "logistics", "target": "supply_chain_crisis", "count": 1, "optional": false}, {"id": "optimize_port_automation", "text": "Optimize port automation systems for increased efficiency", "type": "engineer", "target": "automation_systems", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Seattle 2020-2029 - Emergent Ecologies',
    'Navigate bioengineered forests, artificial wetlands, and hybrid species in Seattle',
    18,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-019-emergent-ecologies", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-019-emergent-ecologies.yaml"}'::jsonb,
    '{"experience": 5500, "money": 850, "items": [{"item_id": "ecological_analysis_kit", "quantity": 1}, {"item_id": "bioengineering_blueprints", "quantity": 1}], "reputation": {"environmentalists": 80, "bioengineers": 75, "corporations": 40, "indigenous_communities": 60}, "unlocks": ["bioengineering_research_network", "urban_ecology_monitoring_tools", "sustainable_bioengineering_certification"]}'::jsonb,
    '[{"id": "study_bioengineered_species", "text": "Study bioengineered plant and animal species adapting to urban conditions", "type": "research", "target": "bioengineered_species", "count": 4, "optional": false}, {"id": "assess_ecosystem_health", "text": "Assess health and stability of emergent urban ecosystems", "type": "analyze", "target": "ecosystem_health", "count": 1, "optional": false}, {"id": "mediate_species_conflicts", "text": "Mediate conflicts between indigenous and bioengineered species", "type": "mediate", "target": "species_interactions", "count": 3, "optional": false}, {"id": "contain_biohazard", "text": "Contain bioengineered organisms that have become hazardous", "type": "contain", "target": "biohazard_threats", "count": 2, "optional": false}, {"id": "develop_sustainable_practices", "text": "Develop sustainable ecological practices for urban bioengineering", "type": "engineer", "target": "sustainable_solutions", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Seattle 2020-2029 - Shadow Economy Empire',
    'Infiltrate shadow corporations, disrupt illegal operations, and expose the dark side of Seattle',
    20,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-020-shadow-economy-empire", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-020-shadow-economy-empire.yaml"}'::jsonb,
    '{"experience": 6800, "money": 1500, "items": [{"item_id": "shadow_economy_intel", "quantity": 1}, {"item_id": "anonymous_crypto_wallet", "quantity": 1}], "reputation": {"underground_networks": 60, "corporations": -80, "government": 70, "social_activists": 85}, "unlocks": ["shadow_economy_intelligence_network", "economic_reform_initiative", "whistleblower_protection_services"]}'::jsonb,
    '[{"id": "infiltrate_shadow_corporation", "text": "Infiltrate a shadow corporation operating in Seattle's underground", "type": "infiltrate", "target": "shadow_corporation", "count": 1, "optional": false}, {"id": "expose_corruption_links", "text": "Expose links between legitimate corporations and shadow operations", "type": "investigate", "target": "corruption_evidence", "count": 5, "optional": false}, {"id": "disrupt_illegal_operations", "text": "Disrupt major illegal operations in the shadow economy", "type": "sabotage", "target": "illegal_operations", "count": 3, "optional": false}, {"id": "protect_whistleblowers", "text": "Protect whistleblowers from shadow economy retaliation", "type": "defend", "target": "whistleblower_safety", "count": 2, "optional": false}, {"id": "reform_economic_system", "text": "Work to reform Seattle's economic system and reduce inequality", "type": "reform", "target": "economic_reform", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Unknown Title',
    ',
    ',
    25,
    35,
    'active',
    '{"id": "canon-quest-seattle-021-virtual-reality-research", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-021-virtual-reality-research.yaml"}'::jsonb,
    '{"experience": 100}'::jsonb,
    '[{"id": "infiltrate_vr_lab", "text": "\u041f\u0440\u043e\u043d\u0438\u043a\u043d\u0443\u0442\u044c \u0432 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u0443\u044e VR-\u043b\u0430\u0431\u043e\u0440\u0430\u0442\u043e\u0440\u0438\u044e", "type": "stealth", "target": "vr_research_facility", "count": 1, "optional": false}, {"id": "collect_research_data", "text": "\u0421\u043e\u0431\u0440\u0430\u0442\u044c \u0434\u0430\u043d\u043d\u044b\u0435 \u043e VR-\u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430\u0445", "type": "hack", "target": "research_databases", "count": 5, "optional": false}, {"id": "rescue_test_subject", "text": "\u0421\u043f\u0430\u0441\u0438 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u0430 \u043e\u043f\u0430\u0441\u043d\u043e\u0433\u043e \u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430", "type": "rescue", "target": "trapped_test_subject", "count": 1, "optional": false}, {"id": "expose_corporate_secrets", "text": "\u0420\u0430\u0437\u043e\u0431\u043b\u0430\u0447\u0438\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0435 \u0441\u0435\u043a\u0440\u0435\u0442\u044b VR-\u0438\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u043d\u0438\u0439", "type": "investigate", "target": "corporate_vr_secrets", "count": 3, "optional": false}, {"id": "choose_vr_future", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0431\u0443\u0434\u0443\u0449\u0435\u043c VR-\u0442\u0435\u0445\u043d\u043e\u043b\u043e\u0433\u0438\u0439", "type": "choice", "target": "vr_ethics_decision", "count": 1, "optional": true}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Unknown Title',
    ',
    ',
    27,
    37,
    'active',
    '{"id": "canon-quest-seattle-022-floating-cities-vision", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-022-floating-cities-vision.yaml"}'::jsonb,
    '{"experience": 100}'::jsonb,
    '[{"id": "investigate_climate_impact", "text": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0441\u043b\u0435\u0434\u0441\u0442\u0432\u0438\u044f \u043a\u043b\u0438\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0445 \u0438\u0437\u043c\u0435\u043d\u0435\u043d\u0438\u0439 \u0432 \u0421\u0438\u044d\u0442\u043b\u0435", "type": "observe", "target": "climate_damage_zones", "count": 4, "optional": false}, {"id": "visit_floating_city_prototype", "text": "\u041f\u043e\u0441\u0435\u0442\u0438\u0442\u044c \u043f\u0440\u043e\u0442\u043e\u0442\u0438\u043f \u043f\u043b\u0430\u0432\u0430\u044e\u0449\u0435\u0433\u043e \u0433\u043e\u0440\u043e\u0434\u0430", "type": "travel", "target": "floating_city_test_bed", "count": 1, "optional": false}, {"id": "expose_corporate_motives", "text": "\u0420\u0430\u0437\u043e\u0431\u043b\u0430\u0447\u0438\u0442\u044c \u0438\u0441\u0442\u0438\u043d\u043d\u044b\u0435 \u043c\u043e\u0442\u0438\u0432\u044b \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0439", "type": "investigate", "target": "corporate_climate_agenda", "count": 3, "optional": false}, {"id": "help_displaced_communities", "text": "\u041f\u043e\u043c\u043e\u0447\u044c \u043f\u0435\u0440\u0435\u043c\u0435\u0449\u0435\u043d\u043d\u044b\u043c \u0441\u043e\u043e\u0431\u0449\u0435\u0441\u0442\u0432\u0430\u043c", "type": "rescue", "target": "climate_refugee_camps", "count": 2, "optional": false}, {"id": "choose_city_future", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0431\u0443\u0434\u0443\u0449\u0435\u043c \u0421\u0438\u044d\u0442\u043b\u0430", "type": "choice", "target": "urban_adaptation_strategy", "count": 1, "optional": true}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Unknown Title',
    ',
    ',
    29,
    39,
    'active',
    '{"id": "canon-quest-seattle-023-neural-implant-revolution", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-023-neural-implant-revolution.yaml"}'::jsonb,
    '{"experience": 100}'::jsonb,
    '[{"id": "witness_implant_surgery", "text": "\u0421\u0442\u0430\u0442\u044c \u0441\u0432\u0438\u0434\u0435\u0442\u0435\u043b\u0435\u043c \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0438 \u043f\u043e \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0435 \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430", "type": "observe", "target": "neural_implant_procedure", "count": 1, "optional": false}, {"id": "test_implant_capabilities", "text": "\u041f\u0440\u043e\u0442\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e\u0441\u0442\u0438 \u043d\u0435\u0439\u0440\u043e\u043d\u043d\u043e\u0433\u043e \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430", "type": "interact", "target": "implant_functionality_test", "count": 3, "optional": false}, {"id": "investigate_side_effects", "text": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0431\u043e\u0447\u043d\u044b\u0435 \u044d\u0444\u0444\u0435\u043a\u0442\u044b \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u043e\u0432", "type": "investigate", "target": "implant_side_effect_cases", "count": 4, "optional": false}, {"id": "expose_corporate_control", "text": "\u0420\u0430\u0437\u043e\u0431\u043b\u0430\u0447\u0438\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u043a\u043e\u043d\u0442\u0440\u043e\u043b\u044c \u043d\u0430\u0434 \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430\u043c\u0438", "type": "hack", "target": "corporate_implant_network", "count": 1, "optional": false}, {"id": "choose_humanity_path", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0431\u0443\u0434\u0443\u0449\u0435\u043c \u0447\u0435\u043b\u043e\u0432\u0435\u0447\u0435\u0441\u043a\u043e\u0433\u043e \u0443\u043b\u0443\u0447\u0448\u0435\u043d\u0438\u044f", "type": "choice", "target": "augmentation_ethics_decision", "count": 1, "optional": true}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Unknown Title',
    ',
    ',
    24,
    34,
    'active',
    '{"id": "canon-quest-seattle-024-cyberpunk-music-revolution", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-024-cyberpunk-music-revolution.yaml"}'::jsonb,
    '{"experience": 100}'::jsonb,
    '[{"id": "attend_cyberpunk_concert", "text": "\u041f\u043e\u0441\u0435\u0442\u0438\u0442\u044c \u043a\u0438\u0431\u0435\u0440\u043f\u0430\u043d\u043a \u043a\u043e\u043d\u0446\u0435\u0440\u0442 \u0432 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u043e\u0439 \u0440\u0435\u0430\u043b\u044c\u043d\u043e\u0441\u0442\u0438", "type": "travel", "target": "vr_music_venue", "count": 1, "optional": false}, {"id": "meet_underground_artists", "text": "\u041f\u043e\u0437\u043d\u0430\u043a\u043e\u043c\u0438\u0442\u044c\u0441\u044f \u0441 \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u043c\u0438 \u0430\u0440\u0442\u0438\u0441\u0442\u0430\u043c\u0438", "type": "dialogue", "target": "cyberpunk_musicians", "count": 3, "optional": false}, {"id": "expose_corporate_influence", "text": "\u0420\u0430\u0437\u043e\u0431\u043b\u0430\u0447\u0438\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0435 \u0432\u043b\u0438\u044f\u043d\u0438\u0435 \u043d\u0430 \u0441\u0446\u0435\u043d\u0443", "type": "investigate", "target": "corporate_music_deals", "count": 2, "optional": false}, {"id": "organize_independent_festival", "text": "\u041e\u0440\u0433\u0430\u043d\u0438\u0437\u043e\u0432\u0430\u0442\u044c \u043d\u0435\u0437\u0430\u0432\u0438\u0441\u0438\u043c\u044b\u0439 \u0444\u0435\u0441\u0442\u0438\u0432\u0430\u043b\u044c", "type": "event", "target": "underground_music_festival", "count": 1, "optional": false}, {"id": "choose_cultural_future", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0431\u0443\u0434\u0443\u0449\u0435\u043c \u043a\u0438\u0431\u0435\u0440\u043f\u0430\u043d\u043a-\u043a\u0443\u043b\u044c\u0442\u0443\u0440\u044b", "type": "choice", "target": "digital_art_ownership", "count": 1, "optional": true}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Unknown Title',
    ',
    ',
    31,
    41,
    'active',
    '{"id": "canon-quest-seattle-025-shadow-economy-empire", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-025-shadow-economy-empire.yaml"}'::jsonb,
    '{"experience": 100}'::jsonb,
    '[{"id": "navigate_darknet_market", "text": "\u041d\u0430\u0432\u0438\u0433\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u043e \u0434\u0430\u0440\u043a\u043d\u0435\u0442-\u043c\u0430\u0440\u043a\u0435\u0442\u0443 \u0421\u0438\u044d\u0442\u043b\u0430", "type": "travel", "target": "underground_crypto_exchange", "count": 1, "optional": false}, {"id": "trace_money_laundering", "text": "\u041f\u0440\u043e\u0441\u043b\u0435\u0434\u0438\u0442\u044c \u0441\u0445\u0435\u043c\u044b \u043e\u0442\u043c\u044b\u0432\u0430\u043d\u0438\u044f \u0434\u0435\u043d\u0435\u0433", "type": "investigate", "target": "crypto_laundering_network", "count": 3, "optional": false}, {"id": "rescue_exploited_workers", "text": "\u0421\u043f\u0430\u0441\u0438 \u044d\u043a\u0441\u043f\u043b\u0443\u0430\u0442\u0438\u0440\u0443\u0435\u043c\u044b\u0445 \u0440\u0430\u0431\u043e\u0447\u0438\u0445 \u0446\u0438\u0444\u0440\u043e\u0432\u044b\u0445 \u0444\u0435\u0440\u043c", "type": "rescue", "target": "crypto_mining_slaves", "count": 2, "optional": false}, {"id": "expose_shadow_banker", "text": "\u0420\u0430\u0437\u043e\u0431\u043b\u0430\u0447\u0438\u0442\u044c \u0442\u0435\u043d\u0435\u0432\u043e\u0435 \u0431\u0430\u043d\u043a\u043e\u0432\u0441\u043a\u043e\u0435 \u043f\u0440\u0435\u0434\u043f\u0440\u0438\u044f\u0442\u0438\u0435", "type": "hack", "target": "decentralized_bank_system", "count": 1, "optional": false}, {"id": "choose_economic_future", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0431\u0443\u0434\u0443\u0449\u0435\u043c \u044d\u043a\u043e\u043d\u043e\u043c\u0438\u043a\u0438", "type": "choice", "target": "financial_system_reform", "count": 1, "optional": true}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Теневые хакеры Amazon',
    'Игрок встречает уличного хакера в Сиэтле, который предлагает работу по взлому Amazon.
После успешного проникновения в HQ и кражи данных, игрок получает награду и репутацию в подполье.
',
    10,
    25,
    'active',
    '{"id": "canon-quest-seattle-026-amazon-shadow-hackers", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-026-amazon-shadow-hackers.yaml"}'::jsonb,
    '{"experience": 5000, "money": 2500, "reputation": {"street_cred": 30, "corporate_hate": 15}, "items": [{"id": "amazon_data_chip", "name": "\u0427\u0438\u043f \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 Amazon", "type": "quest_item", "rarity": "rare"}], "unlocks": {"achievements": ["\u041a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u0434\u0438\u0432\u0435\u0440\u0441\u0430\u043d\u0442"], "flags": ["amazon_hack_completed", "shadow_hacker_recruit"]}}'::jsonb,
    '[{"id": "meet_hacker_contact", "text": "\u041d\u0430\u0439\u0442\u0438 \u043a\u043e\u043d\u0442\u0430\u043a\u0442 \u0445\u0430\u043a\u0435\u0440\u0441\u043a\u043e\u0439 \u0433\u0440\u0443\u043f\u043f\u044b \u0432 \u0440\u0430\u0439\u043e\u043d\u0435 Pike Place Market", "type": "interact", "target": "street_hacker_contact", "count": 1, "optional": false}, {"id": "infiltrate_amazon_campus", "text": "\u041f\u0440\u043e\u043d\u0438\u043a\u043d\u0443\u0442\u044c \u043d\u0430 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u044e Amazon HQ, \u0438\u0437\u0431\u0435\u0433\u0430\u044f \u043f\u0430\u0442\u0440\u0443\u043b\u0435\u0439 \u043e\u0445\u0440\u0430\u043d\u044b", "type": "interact", "target": "amazon_security_perimeter", "count": 1, "optional": false}, {"id": "hack_corporate_network", "text": "\u0412\u0437\u043b\u043e\u043c\u0430\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u0443\u044e \u0441\u0435\u0442\u044c Amazon \u0438 \u0441\u043a\u0430\u0447\u0430\u0442\u044c \u0446\u0435\u043d\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", "type": "skill_check", "target": "network_intrusion", "skill": "hacking", "difficulty": 0.7, "optional": false}, {"id": "extract_data", "text": "\u0418\u0437\u0432\u043b\u0435\u0447\u044c \u0443\u043a\u0440\u0430\u0434\u0435\u043d\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435 \u0438\u0437 \u0441\u0438\u0441\u0442\u0435\u043c\u044b \u0431\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438", "type": "interact", "target": "data_extraction_terminal", "count": 1, "optional": false}, {"id": "escape_pursuit", "text": "\u0421\u0431\u0435\u0436\u0430\u0442\u044c \u043e\u0442 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0439 \u043e\u0445\u0440\u0430\u043d\u044b \u0447\u0435\u0440\u0435\u0437 \u043a\u0430\u043d\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u044e \u0421\u0438\u044d\u0442\u043b\u0430", "type": "interact", "target": "underground_escape_route", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Подпольный риппердок',
    'Игрок получает травму в перестрелке и обращается к уличному риппердоку в Сиэтле.
После успешной операции имплантации, игрок участвует в защите клиники от корпоративных рейдеров.
',
    15,
    30,
    'active',
    '{"id": "canon-quest-seattle-027-underground-ripperdoc", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-027-underground-ripperdoc.yaml"}'::jsonb,
    '{"experience": 7500, "money": 1800, "reputation": {"street_cred": 25, "medical_black_market": 40}, "items": [{"id": "experimental_implant", "name": "\u042d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430\u043b\u044c\u043d\u044b\u0439 \u0438\u043c\u043f\u043b\u0430\u043d\u0442", "type": "cybernetic", "rarity": "epic"}, {"id": "ripperdoc_contacts", "name": "\u041a\u043e\u043d\u0442\u0430\u043a\u0442\u044b \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u0445 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u043e\u0432", "type": "quest_item", "rarity": "uncommon"}], "unlocks": {"achievements": ["\u041f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u0439 \u043f\u0430\u0446\u0438\u0435\u043d\u0442", "\u0417\u0430\u0449\u0438\u0442\u043d\u0438\u043a \u0443\u043b\u0438\u0447\u043d\u043e\u0439 \u043c\u0435\u0434\u0438\u0446\u0438\u043d\u044b"], "flags": ["underground_ripperdoc_access", "experimental_implant_installed"]}}'::jsonb,
    '[{"id": "find_underground_clinic", "text": "\u041d\u0430\u0439\u0442\u0438 \u0432\u0445\u043e\u0434 \u0432 \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u0443\u044e \u043a\u043b\u0438\u043d\u0438\u043a\u0443 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u0430 \u0432 \u043a\u0430\u043d\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u043f\u043e\u0434 Pioneer Square", "type": "interact", "target": "underground_clinic_entrance", "count": 1, "optional": false}, {"id": "consult_ripperdoc", "text": "\u041f\u0440\u043e\u043a\u043e\u043d\u0441\u0443\u043b\u044c\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u0441 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u043e\u043c \u043e \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b\u0445 \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430\u0445", "type": "interact", "target": "ripperdoc_consultation", "count": 1, "optional": false}, {"id": "undergo_implant_procedure", "text": "\u041f\u0440\u043e\u0439\u0442\u0438 \u043f\u0440\u043e\u0446\u0435\u0434\u0443\u0440\u0443 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430\u043b\u044c\u043d\u043e\u0433\u043e \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430", "type": "skill_check", "target": "implant_surgery", "skill": "body", "difficulty": 0.6, "optional": false}, {"id": "defend_clinic", "text": "\u0417\u0430\u0449\u0438\u0442\u0438\u0442\u044c \u043a\u043b\u0438\u043d\u0438\u043a\u0443 \u043e\u0442 \u0430\u0442\u0430\u043a\u0438 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0445 \u043d\u0430\u0435\u043c\u043d\u0438\u043a\u043e\u0432", "type": "combat", "target": "corporate_raiders", "count": 5, "optional": false}, {"id": "complete_operation", "text": "\u0417\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044c \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u044e \u0438 \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043d\u0430\u0433\u0440\u0430\u0434\u0443 \u043e\u0442 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u0430", "type": "interact", "target": "operation_completion", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Эко-протесты против корпораций',
    'Игрок становится частью эко-движения в Сиэтле, борющегося против корпоративного загрязнения.
Участие в мирных протестах и скрытых операциях по защите окружающей среды от корпораций.
',
    8,
    20,
    'active',
    '{"id": "canon-quest-seattle-028-eco-protest-revolution", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-028-eco-protest-revolution.yaml"}'::jsonb,
    '{"experience": 4200, "money": 1200, "reputation": {"environmental_awareness": 35, "corporate_hate": 20, "community_respect": 15}, "items": [{"id": "eco_activist_badge", "name": "\u0417\u043d\u0430\u0447\u043e\u043a \u044d\u043a\u043e-\u0430\u043a\u0442\u0438\u0432\u0438\u0441\u0442\u0430", "type": "accessory", "rarity": "uncommon"}], "unlocks": {"achievements": ["\u0417\u0430\u0449\u0438\u0442\u043d\u0438\u043a \u043e\u043a\u0440\u0443\u0436\u0430\u044e\u0449\u0435\u0439 \u0441\u0440\u0435\u0434\u044b", "\u041a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u0441\u0430\u0431\u043e\u0442\u0430\u0436\u043d\u0438\u043a"], "flags": ["eco_movement_member", "corporate_pollution_exposed"]}}'::jsonb,
    '[{"id": "join_eco_movement", "text": "\u041d\u0430\u0439\u0442\u0438 \u0438 \u043f\u0440\u0438\u0441\u043e\u0435\u0434\u0438\u043d\u0438\u0442\u044c\u0441\u044f \u043a \u044d\u043a\u043e-\u0430\u043a\u0442\u0438\u0432\u0438\u0441\u0442\u0430\u043c \u0432 \u0440\u0430\u0439\u043e\u043d\u0435 Discovery Park", "type": "interact", "target": "eco_activist_camp", "count": 1, "optional": false}, {"id": "participate_peaceful_protest", "text": "\u0423\u0447\u0430\u0441\u0442\u0432\u043e\u0432\u0430\u0442\u044c \u0432 \u043c\u0438\u0440\u043d\u043e\u043c \u043f\u0440\u043e\u0442\u0435\u0441\u0442\u0435 \u043f\u0440\u043e\u0442\u0438\u0432 \u0445\u0438\u043c\u0438\u0447\u0435\u0441\u043a\u043e\u0433\u043e \u0437\u0430\u0432\u043e\u0434\u0430 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0438", "type": "interact", "target": "corporate_chemical_plant", "count": 1, "optional": false}, {"id": "gather_evidence", "text": "\u0421\u043e\u0431\u0440\u0430\u0442\u044c \u0434\u043e\u043a\u0430\u0437\u0430\u0442\u0435\u043b\u044c\u0441\u0442\u0432\u0430 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u0437\u0430\u0433\u0440\u044f\u0437\u043d\u0435\u043d\u0438\u044f \u043e\u043a\u0440\u0443\u0436\u0430\u044e\u0449\u0435\u0439 \u0441\u0440\u0435\u0434\u044b", "type": "interact", "target": "pollution_evidence", "count": 3, "optional": false}, {"id": "sabotage_pollution_operation", "text": "\u041f\u043e\u0432\u0440\u0435\u0434\u0438\u0442\u044c \u043e\u0431\u043e\u0440\u0443\u0434\u043e\u0432\u0430\u043d\u0438\u0435 \u0434\u043b\u044f \u043e\u0447\u0438\u0441\u0442\u043a\u0438 \u0441\u0442\u043e\u0447\u043d\u044b\u0445 \u0432\u043e\u0434 \u043d\u0430 \u0437\u0430\u0432\u043e\u0434\u0435", "type": "skill_check", "target": "sabotage_equipment", "skill": "technical", "difficulty": 0.5, "optional": true}, {"id": "protect_eco_leader", "text": "\u0417\u0430\u0449\u0438\u0442\u0438\u0442\u044c \u043b\u0438\u0434\u0435\u0440\u0430 \u044d\u043a\u043e-\u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f \u043e\u0442 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0445 \u043d\u0430\u0435\u043c\u043d\u0438\u043a\u043e\u0432", "type": "combat", "target": "corporate_thugs", "count": 3, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Виртуальные сны нейронных сетей',
    'Игрок попадает в мир подпольных VR-исследований в Сиэтле, где нейронные импланты позволяют погружаться в сны ИИ.
Неожиданно виртуальный мир выходит из-под контроля, угрожая сознанию участников.
',
    20,
    35,
    'active',
    '{"id": "canon-quest-seattle-029-virtual-reality-neural-dreams", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-029-virtual-reality-neural-dreams.yaml"}'::jsonb,
    '{"experience": 8500, "money": 3200, "reputation": {"tech_savvy": 40, "neural_research": 50, "digital_consciousness": 25}, "items": [{"id": "neural_interface_prototype", "name": "\u041f\u0440\u043e\u0442\u043e\u0442\u0438\u043f \u043d\u0435\u0439\u0440\u043e-\u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430", "type": "cybernetic", "rarity": "legendary"}, {"id": "digital_dream_data", "name": "\u0414\u0430\u043d\u043d\u044b\u0435 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u044b\u0445 \u0441\u043d\u043e\u0432", "type": "data_chip", "rarity": "rare"}], "unlocks": {"achievements": ["\u041f\u0443\u0442\u0435\u0448\u0435\u0441\u0442\u0432\u0435\u043d\u043d\u0438\u043a \u0446\u0438\u0444\u0440\u043e\u0432\u044b\u0445 \u0441\u043d\u043e\u0432", "\u0421\u043f\u0430\u0441\u0438\u0442\u0435\u043b\u044c \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u043e\u0433\u043e \u043c\u0438\u0440\u0430"], "flags": ["vr_lab_access", "neural_interface_experienced", "digital_consciousness_aware"]}}'::jsonb,
    '[{"id": "discover_vr_lab", "text": "\u041d\u0430\u0439\u0442\u0438 \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u0443\u044e VR-\u043b\u0430\u0431\u043e\u0440\u0430\u0442\u043e\u0440\u0438\u044e \u0432 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u0430\u043d\u0438\u0438 South Lake Union", "type": "interact", "target": "abandoned_vr_lab", "count": 1, "optional": false}, {"id": "install_neural_interface", "text": "\u041f\u0440\u043e\u0439\u0442\u0438 \u043f\u0440\u043e\u0446\u0435\u0434\u0443\u0440\u0443 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430\u043b\u044c\u043d\u043e\u0433\u043e \u043d\u0435\u0439\u0440\u043e-\u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430", "type": "skill_check", "target": "neural_implant_installation", "skill": "intelligence", "difficulty": 0.8, "optional": false}, {"id": "enter_virtual_dream", "text": "\u041f\u043e\u0433\u0440\u0443\u0437\u0438\u0442\u044c\u0441\u044f \u0432 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u044b\u0439 \u043c\u0438\u0440 \u0441\u043d\u043e\u0432 \u0418\u0418 \u0447\u0435\u0440\u0435\u0437 \u043d\u0435\u0439\u0440\u043e-\u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441", "type": "interact", "target": "vr_dream_entry", "count": 1, "optional": false}, {"id": "navigate_dream_world", "text": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u044b\u0439 \u043c\u0438\u0440 \u0438 \u043d\u0430\u0439\u0442\u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u043e\u0432 \u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430", "type": "interact", "target": "dream_world_navigation", "count": 3, "optional": false}, {"id": "defeat_digital_horrors", "text": "\u041f\u043e\u0431\u0435\u0434\u0438\u0442\u044c \u0446\u0438\u0444\u0440\u043e\u0432\u044b\u0435 \u043a\u043e\u0448\u043c\u0430\u0440\u044b \u0438 \u0430\u043d\u043e\u043c\u0430\u043b\u0438\u0438 \u0432 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u043e\u043c \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0435", "type": "combat", "target": "digital_anomalies", "count": 4, "optional": false}, {"id": "save_other_participants", "text": "\u0421\u043f\u0430\u0441\u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u043e\u0432 \u044d\u043a\u0441\u043f\u0435\u0440\u0438\u043c\u0435\u043d\u0442\u0430 \u043e\u0442 \u0446\u0438\u0444\u0440\u043e\u0432\u043e\u0433\u043e \u0431\u0435\u0437\u0443\u043c\u0438\u044f", "type": "interact", "target": "participant_rescue", "count": 2, "optional": false}, {"id": "exit_virtual_reality", "text": "\u0411\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e \u0432\u044b\u0439\u0442\u0438 \u0438\u0437 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u043e\u0433\u043e \u043c\u0438\u0440\u0430 \u0438 \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u043e\u0437\u043d\u0430\u043d\u0438\u0435", "type": "skill_check", "target": "consciousness_extraction", "skill": "willpower", "difficulty": 0.7, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Уличные гонки в дождевом городе',
    'Игрок попадает в мир подпольных уличных гонок Сиэтла, где дождливые улицы становятся ареной для смертельно опасных соревнований.
Необходимо модифицировать транспорт, участвовать в гонках и разбираться с корпоративными интригами.
',
    12,
    28,
    'active',
    '{"id": "canon-quest-seattle-030-rain-city-street-racing", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-030-rain-city-street-racing.yaml"}'::jsonb,
    '{"experience": 6200, "money": 2800, "reputation": {"street_cred": 35, "racing_underground": 45, "vehicle_expertise": 20}, "items": [{"id": "rain_tuned_engine", "name": "\u0414\u043e\u0436\u0434\u0435\u0432\u043e\u0439 \u0442\u044e\u043d\u0438\u043d\u0433-\u0434\u0432\u0438\u0433\u0430\u0442\u0435\u043b\u044c", "type": "vehicle_part", "rarity": "rare"}, {"id": "racing_league_card", "name": "\u041a\u0430\u0440\u0442\u0430 \u0433\u043e\u043d\u043e\u0447\u043d\u043e\u0439 \u043b\u0438\u0433\u0438", "type": "access_card", "rarity": "uncommon"}], "unlocks": {"achievements": ["\u041a\u043e\u0440\u043e\u043b\u044c \u0434\u043e\u0436\u0434\u0435\u0432\u044b\u0445 \u0443\u043b\u0438\u0446", "\u041f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u0439 \u0433\u043e\u043d\u0449\u0438\u043a"], "flags": ["underground_racing_member", "corporate_racing_enemy"]}}'::jsonb,
    '[{"id": "join_racing_league", "text": "\u041d\u0430\u0439\u0442\u0438 \u0438 \u043f\u0440\u0438\u0441\u043e\u0435\u0434\u0438\u043d\u0438\u0442\u044c\u0441\u044f \u043a \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u043e\u0439 \u0433\u043e\u043d\u043e\u0447\u043d\u043e\u0439 \u043b\u0438\u0433\u0435 \u0432 \u0440\u0430\u0439\u043e\u043d\u0435 SoDo", "type": "interact", "target": "underground_racing_league", "count": 1, "optional": false}, {"id": "modify_vehicle", "text": "\u041c\u043e\u0434\u0438\u0444\u0438\u0446\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0442\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442 \u0434\u043b\u044f \u0433\u043e\u043d\u043e\u043a \u0432 \u0434\u043e\u0436\u0434\u0435\u0432\u044b\u0445 \u0443\u0441\u043b\u043e\u0432\u0438\u044f\u0445 \u0421\u0438\u044d\u0442\u043b\u0430", "type": "skill_check", "target": "vehicle_modification", "skill": "technical", "difficulty": 0.6, "optional": false}, {"id": "win_first_race", "text": "\u0412\u044b\u0438\u0433\u0440\u0430\u0442\u044c \u043f\u0435\u0440\u0432\u0443\u044e \u0433\u043e\u043d\u043a\u0443 \u0432 \u0434\u043e\u0436\u0434\u0435\u0432\u044b\u0445 \u0443\u0441\u043b\u043e\u0432\u0438\u044f\u0445 \u043f\u043e \u0443\u043b\u0438\u0446\u0430\u043c \u0433\u043e\u0440\u043e\u0434\u0430", "type": "racing", "target": "rain_street_race", "count": 1, "optional": false}, {"id": "sabotage_rival", "text": "\u041e\u043f\u0446\u0438\u043e\u043d\u0430\u043b\u044c\u043d\u043e: \u0441\u0430\u0431\u043e\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0442\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0430", "type": "skill_check", "target": "rival_sabotage", "skill": "stealth", "difficulty": 0.7, "optional": true}, {"id": "survive_ambush", "text": "\u0412\u044b\u0436\u0438\u0442\u044c \u0432 \u0437\u0430\u0441\u0430\u0434\u0435 \u043e\u0442 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0445 \u043d\u0430\u0435\u043c\u043d\u0438\u043a\u043e\u0432 \u043f\u043e\u0441\u043b\u0435 \u043f\u043e\u0431\u0435\u0434\u044b", "type": "combat", "target": "corporate_ambush", "count": 4, "optional": false}, {"id": "claim_victory", "text": "\u0417\u0430\u0431\u0440\u0430\u0442\u044c \u043f\u0440\u0438\u0437 \u0438 \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0440\u0435\u043f\u0443\u0442\u0430\u0446\u0438\u044e \u0432 \u0433\u043e\u043d\u043e\u0447\u043d\u043e\u043c \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u0435", "type": "interact", "target": "victory_claim", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Нейронная сеть хакеров',
    'Игрок получает задание от корпоративного клиента проникнуть в нейронную сеть подпольных хакеров, которые используют ИИ для взлома корпоративных систем.
По мере развития сюжета раскрываются мотивы хакеров и сложный выбор между корпоративной лояльностью и справедливостью.
',
    15,
    25,
    'active',
    '{"id": "canon-quest-seattle-031-neural-net-hackers", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-031-neural-net-hackers.yaml"}'::jsonb,
    '{"experience": 5000, "money": 2500, "reputation": {"corporate": 200, "underground": -100}, "items": [{"id": "neural_implant_blueprint", "name": "\u0427\u0435\u0440\u0442\u0435\u0436\u0438 \u043d\u0435\u0439\u0440\u043e\u043d\u043d\u043e\u0433\u043e \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430", "type": "blueprint", "rarity": "rare", "quantity": 1}], "unlocks": [{"quest": "canon-quest-seattle-corporate-espionage"}, {"location": "underground_hacker_den"}]}'::jsonb,
    '[{"id": "meet_corporate_contact", "text": "\u0412\u0441\u0442\u0440\u0435\u0442\u0438\u0442\u044c\u0441\u044f \u0441 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u043c \u043a\u043e\u043d\u0442\u0430\u043a\u0442\u043e\u043c \u0432 \u043e\u0442\u0435\u043b\u0435 Westin \u043d\u0430 7-\u0439 \u0430\u0432\u0435\u043d\u044e", "type": "interact", "target": "westin_hotel_corporate_contact", "count": 1, "optional": false}, {"id": "gather_intelligence", "text": "\u0421\u043e\u0431\u0440\u0430\u0442\u044c \u0440\u0430\u0437\u0432\u0435\u0434\u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u043d\u0435\u0439\u0440\u043e\u043d\u043d\u043e\u0439 \u0441\u0435\u0442\u0438 \u0445\u0430\u043a\u0435\u0440\u043e\u0432 \u0432 \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u0445 \u0431\u0430\u0440\u0430\u0445 Pioneer Square", "type": "interact", "target": "pioneer_square_underground_bars", "count": 3, "optional": false}, {"id": "infiltrate_server_room", "text": "\u041f\u0440\u043e\u043d\u0438\u043a\u043d\u0443\u0442\u044c \u0432 \u0441\u0435\u0440\u0432\u0435\u0440\u043d\u0443\u044e \u043a\u043e\u043c\u043d\u0430\u0442\u0443 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u0433\u043e \u0434\u0430\u0442\u0430-\u0446\u0435\u043d\u0442\u0440\u0430 Amazon", "type": "interact", "target": "abandoned_amazon_datacenter", "count": 1, "optional": false}, {"id": "hack_neural_network", "text": "\u0412\u0437\u043b\u043e\u043c\u0430\u0442\u044c \u043d\u0435\u0439\u0440\u043e\u043d\u043d\u0443\u044e \u0441\u0435\u0442\u044c \u0438 \u0441\u043a\u0430\u0447\u0430\u0442\u044c \u0434\u043e\u043a\u0430\u0437\u0430\u0442\u0435\u043b\u044c\u0441\u0442\u0432\u0430 \u0434\u0435\u044f\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u0438 \u0445\u0430\u043a\u0435\u0440\u043e\u0432", "type": "interact", "target": "neural_network_mainframe", "count": 1, "optional": false}, {"id": "confront_hacker_leader", "text": "\u041f\u0440\u043e\u0442\u0438\u0432\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043b\u0438\u0434\u0435\u0440\u0443 \u0445\u0430\u043a\u0435\u0440\u043e\u0432 \u0438 \u043f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u043e \u0441\u0443\u0434\u044c\u0431\u0435 \u0441\u0435\u0442\u0438", "type": "interact", "target": "hacker_leader_confrontation", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Корпоративная кража имплантов',
    'Игрок нанимается частным детективом для расследования серии краж прототипов кибернетических имплантов. Расследование приводит к сети подпольных риппердоков,
которые модифицируют корпоративные технологии для черного рынка. Игрок должен выбрать между возвратом имплантов корпорации или их распространением в подполье.
',
    12,
    20,
    'active',
    '{"id": "canon-quest-seattle-032-corporate-implant-theft", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-032-corporate-implant-theft.yaml"}'::jsonb,
    '{"experience": 3500, "money": 1800, "reputation": {"corporate": -150, "underground": 300}, "items": [{"id": "prototype_neural_implant", "name": "\u041f\u0440\u043e\u0442\u043e\u0442\u0438\u043f \u043d\u0435\u0439\u0440\u043e\u043d\u043d\u043e\u0433\u043e \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u0430", "type": "implant", "rarity": "epic", "quantity": 1}], "unlocks": [{"quest": "canon-quest-seattle-implant-black-market"}, {"location": "redmond_ripperdoc_clinic"}]}'::jsonb,
    '[{"id": "investigate_crime_scene", "text": "\u041e\u0441\u043c\u043e\u0442\u0440\u0435\u0442\u044c \u043c\u0435\u0441\u0442\u043e \u043f\u0435\u0440\u0432\u043e\u0439 \u043a\u0440\u0430\u0436\u0438 \u0432 \u043b\u0430\u0431\u043e\u0440\u0430\u0442\u043e\u0440\u0438\u0438 Militech", "type": "interact", "target": "militech_lab_crime_scene", "count": 1, "optional": false}, {"id": "interview_witnesses", "text": "\u041e\u043f\u0440\u043e\u0441\u0438\u0442\u044c \u0441\u0432\u0438\u0434\u0435\u0442\u0435\u043b\u0435\u0439 \u0438 \u0441\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a\u043e\u0432 \u043b\u0430\u0431\u043e\u0440\u0430\u0442\u043e\u0440\u0438\u0438", "type": "interact", "target": "lab_employees_interviews", "count": 3, "optional": false}, {"id": "track_ripperdoc_network", "text": "\u041e\u0442\u0441\u043b\u0435\u0434\u0438\u0442\u044c \u0441\u0435\u0442\u044c \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u044b\u0445 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u043e\u0432 \u0432 Redmond", "type": "interact", "target": "redmond_ripperdoc_network", "count": 1, "optional": false}, {"id": "infiltrate_black_market", "text": "\u041f\u0440\u043e\u043d\u0438\u043a\u043d\u0443\u0442\u044c \u043d\u0430 \u0447\u0435\u0440\u043d\u044b\u0439 \u0440\u044b\u043d\u043e\u043a \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u043e\u0432 \u0432 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u043c \u043c\u0435\u0442\u0440\u043e", "type": "interact", "target": "abandoned_subway_market", "count": 1, "optional": false}, {"id": "confront_master_ripperdoc", "text": "\u041f\u0440\u043e\u0442\u0438\u0432\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u0433\u043b\u0430\u0432\u043d\u043e\u043c\u0443 \u0440\u0438\u043f\u043f\u0435\u0440\u0434\u043e\u043a\u0443 \u0438 \u0440\u0435\u0448\u0438\u0442\u044c \u0441\u0443\u0434\u044c\u0431\u0443 \u0438\u043c\u043f\u043b\u0430\u043d\u0442\u043e\u0432", "type": "interact", "target": "master_ripperdoc_confrontation", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Зависимость виртуальной реальности',
    'Игрок нанимается семьей, чей сын/дочь/брат/сестра погрузился в виртуальную реальность и отказывается возвращаться в реальный мир.
Расследование приводит к подпольной сети, торгующей "цифровыми наркотиками" - VR-опытами, вызывающими сильнейшую зависимость.
',
    8,
    18,
    'active',
    '{"id": "canon-quest-seattle-033-virtual-reality-addiction", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-033-virtual-reality-addiction.yaml"}'::jsonb,
    '{"experience": 2800, "money": 1200, "reputation": {"community": 250, "corporate": -50}, "items": [{"id": "vr_interface_implant", "name": "VR \u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441 \u0438\u043c\u043f\u043b\u0430\u043d\u0442", "type": "implant", "rarity": "uncommon", "quantity": 1}], "unlocks": [{"quest": "canon-quest-seattle-vr-addiction-support"}, {"location": "community_addiction_clinic"}]}'::jsonb,
    '[{"id": "meet_family", "text": "\u0412\u0441\u0442\u0440\u0435\u0442\u0438\u0442\u044c\u0441\u044f \u0441 \u0441\u0435\u043c\u044c\u0435\u0439 \u0437\u0430\u0432\u0438\u0441\u0438\u043c\u043e\u0433\u043e \u0432 \u0438\u0445 \u0434\u043e\u043c\u0435 \u0432 Capitol Hill", "type": "interact", "target": "capitol_hill_family_home", "count": 1, "optional": false}, {"id": "investigate_vr_clinic", "text": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u0443\u044e VR-\u043a\u043b\u0438\u043d\u0438\u043a\u0443 \u0432 Ballard", "type": "interact", "target": "ballard_vr_clinic", "count": 1, "optional": false}, {"id": "rescue_victim", "text": "\u0412\u044b\u0432\u0435\u0441\u0442\u0438 \u0436\u0435\u0440\u0442\u0432\u0443 \u0438\u0437 \u0432\u0438\u0440\u0442\u0443\u0430\u043b\u044c\u043d\u043e\u0439 \u0440\u0435\u0430\u043b\u044c\u043d\u043e\u0441\u0442\u0438 \u043d\u0430\u0441\u0438\u043b\u044c\u043d\u043e", "type": "interact", "target": "vr_pod_rescue", "count": 1, "optional": false}, {"id": "trace_distribution_network", "text": "\u041e\u0442\u0441\u043b\u0435\u0434\u0438\u0442\u044c \u0441\u0435\u0442\u044c \u0440\u0430\u0441\u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0435\u043d\u0438\u044f \u0446\u0438\u0444\u0440\u043e\u0432\u044b\u0445 \u043d\u0430\u0440\u043a\u043e\u0442\u0438\u043a\u043e\u0432", "type": "interact", "target": "digital_drug_distribution", "count": 1, "optional": false}, {"id": "confront_dealer", "text": "\u041f\u0440\u043e\u0442\u0438\u0432\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u0433\u043b\u0430\u0432\u043d\u043e\u043c\u0443 \u0440\u0430\u0441\u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0438\u0442\u0435\u043b\u044e \u0438 \u0440\u0430\u0437\u0440\u0443\u0448\u0438\u0442\u044c \u0441\u0435\u0442\u044c", "type": "interact", "target": "dealer_confrontation", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Корпоративная война в тенях',
    'Игрок становится частью корпоративной войны в тенях, выполняя задания по промышленному шпионажу, саботажу и сбору разведданных.
Квест раскрывает сложную паутину интриг между Arasaka, Militech и Biotechnica, где каждый шаг может привести к катастрофе.
',
    20,
    30,
    'active',
    '{"id": "canon-quest-seattle-034-corporate-warfare-shadows", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-034-corporate-warfare-shadows.yaml"}'::jsonb,
    '{"experience": 8000, "money": 5000, "reputation": {"corporate": 1000, "winner_corp": 500}, "items": [{"id": "quantum_processor_blueprint", "name": "\u0427\u0435\u0440\u0442\u0435\u0436\u0438 \u043a\u0432\u0430\u043d\u0442\u043e\u0432\u043e\u0433\u043e \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u043e\u0440\u0430", "type": "blueprint", "rarity": "legendary", "quantity": 1}], "unlocks": [{"quest": "canon-quest-seattle-corporate-war-aftermath"}, {"faction": "winning_corporation"}]}'::jsonb,
    '[{"id": "accept_corporate_offer", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u043f\u0440\u0435\u0434\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043e\u0442 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u0438\u0442\u0435\u043b\u044f \u0432 \u0448\u0438\u043a\u0430\u0440\u043d\u043e\u043c \u0440\u0435\u0441\u0442\u043e\u0440\u0430\u043d\u0435", "type": "interact", "target": "corporate_restaurant_meeting", "count": 1, "optional": false}, {"id": "steal_competitor_data", "text": "\u0423\u043a\u0440\u0430\u0441\u0442\u044c \u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u043a\u0432\u0430\u043d\u0442\u043e\u0432\u044b\u0445 \u0438\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u043d\u0438\u044f\u0445 \u0438\u0437 \u043b\u0430\u0431\u043e\u0440\u0430\u0442\u043e\u0440\u0438\u0438 \u043a\u043e\u043d\u043a\u0443\u0440\u0435\u043d\u0442\u0430", "type": "interact", "target": "competitor_quantum_lab", "count": 1, "optional": false}, {"id": "sabotage_rival_operations", "text": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u0441\u0430\u0431\u043e\u0442\u0430\u0436 \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0439 \u043a\u043e\u043d\u043a\u0443\u0440\u0438\u0440\u0443\u044e\u0449\u0435\u0439 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0438", "type": "interact", "target": "rival_corporate_facility", "count": 1, "optional": false}, {"id": "uncover_master_conspiracy", "text": "\u0420\u0430\u0441\u043a\u0440\u044b\u0442\u044c \u0433\u043b\u0430\u0432\u043d\u044b\u0439 \u0437\u0430\u0433\u043e\u0432\u043e\u0440 \u0432 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u043c \u0441\u043e\u0432\u0435\u0442\u0435 \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u043e\u0432", "type": "interact", "target": "corporate_board_meeting", "count": 1, "optional": false}, {"id": "make_final_choice", "text": "\u041f\u0440\u0438\u043d\u044f\u0442\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u0442\u0435\u043b\u044c\u043d\u043e\u0435 \u0440\u0435\u0448\u0435\u043d\u0438\u0435 \u0432 \u043f\u043e\u043b\u044c\u0437\u0443 \u043e\u0434\u043d\u043e\u0439 \u0438\u0437 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0439", "type": "interact", "target": "final_corporate_stand", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Кризис климатических беженцев',
    'Игрок становится координатором гуманитарной помощи в лагере климатических беженцев на окраине Сиэтла. Расследование раскрывает,
что корпорации используют кризис для эксплуатации беженцев и захвата земель. Игрок должен выбрать между гуманитарной помощью и корпоративной выгодой.
',
    10,
    20,
    'active',
    '{"id": "canon-quest-seattle-035-climate-refugee-crisis", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-035-climate-refugee-crisis.yaml"}'::jsonb,
    '{"experience": 4000, "money": 1500, "reputation": {"community": 400, "corporate": -200}, "items": [{"id": "refugee_aid_package", "name": "\u041f\u0430\u043a\u0435\u0442 \u0433\u0443\u043c\u0430\u043d\u0438\u0442\u0430\u0440\u043d\u043e\u0439 \u043f\u043e\u043c\u043e\u0449\u0438", "type": "consumable", "rarity": "common", "quantity": 5}], "unlocks": [{"quest": "canon-quest-seattle-climate-justice"}, {"location": "refugee_community_center"}]}'::jsonb,
    '[{"id": "visit_refugee_camp", "text": "\u041f\u043e\u0441\u0435\u0442\u0438\u0442\u044c \u043b\u0430\u0433\u0435\u0440\u044c \u043a\u043b\u0438\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0445 \u0431\u0435\u0436\u0435\u043d\u0446\u0435\u0432 \u043d\u0430 \u043e\u043a\u0440\u0430\u0438\u043d\u0435 \u0421\u0438\u044d\u0442\u043b\u0430", "type": "interact", "target": "refugee_camp_entrance", "count": 1, "optional": false}, {"id": "assess_humanitarian_needs", "text": "\u041e\u0446\u0435\u043d\u0438\u0442\u044c \u0433\u0443\u043c\u0430\u043d\u0438\u0442\u0430\u0440\u043d\u044b\u0435 \u043f\u043e\u0442\u0440\u0435\u0431\u043d\u043e\u0441\u0442\u0438 \u0431\u0435\u0436\u0435\u043d\u0446\u0435\u0432", "type": "interact", "target": "camp_assessment", "count": 1, "optional": false}, {"id": "investigate_corporate_exploitation", "text": "\u0420\u0430\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u0443\u044e \u044d\u043a\u0441\u043f\u043b\u0443\u0430\u0442\u0430\u0446\u0438\u044e \u0431\u0435\u0436\u0435\u043d\u0446\u0435\u0432", "type": "interact", "target": "corporate_exploitation_evidence", "count": 1, "optional": false}, {"id": "organize_relief_effort", "text": "\u041e\u0440\u0433\u0430\u043d\u0438\u0437\u043e\u0432\u0430\u0442\u044c \u0440\u0430\u0441\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u0438\u0435 \u0433\u0443\u043c\u0430\u043d\u0438\u0442\u0430\u0440\u043d\u043e\u0439 \u043f\u043e\u043c\u043e\u0449\u0438", "type": "interact", "target": "relief_distribution", "count": 1, "optional": false}, {"id": "confront_corporate_interests", "text": "\u041f\u0440\u043e\u0442\u0438\u0432\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u043c \u0438\u043d\u0442\u0435\u0440\u0435\u0441\u0430\u043c \u0438 \u043f\u0440\u0438\u043d\u044f\u0442\u044c \u0440\u0435\u0448\u0435\u043d\u0438\u0435", "type": "interact", "target": "corporate_confrontation", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Информатор в подземелье',
    'Игрок спускается в заброшенные туннели под Сиэтлом, находит подпольный бар и встречает информатора,
который делится информацией о корпоративных заговорах в обмен на услугу.
',
    5,
    15,
    'active',
    '{"id": "canon-quest-seattle-036-underground-informant", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-036-underground-informant.yaml"}'::jsonb,
    '{"experience": 2500, "money": 500, "reputation": {"street_cred": 10, "underworld": 15}, "items": [{"id": "corporate_data_chip", "name": "\u0427\u0438\u043f \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0439", "type": "data", "rarity": "uncommon", "description": "\u0417\u0430\u0448\u0438\u0444\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0445 \u043f\u0440\u043e\u0435\u043a\u0442\u0430\u0445 \u0421\u0438\u044d\u0442\u043b\u0430"}], "unlocks": {"achievements": ["\u0414\u0440\u0443\u0433 \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u044f"], "flags": ["underground_informant_contacted", "corporate_data_access"], "quests": ["quest-037-corporate-blackmail"]}}'::jsonb,
    '[{"id": "find_underground_entrance", "text": "\u041d\u0430\u0439\u0442\u0438 \u0432\u0445\u043e\u0434 \u0432 \u043f\u043e\u0434\u0437\u0435\u043c\u043d\u044b\u0435 \u0442\u0443\u043d\u043d\u0435\u043b\u0438 \u043f\u043e\u0434 Pioneer Square", "type": "location", "target": "pioneer_square_underground_entrance", "count": 1, "optional": false}, {"id": "navigate_tunnels", "text": "\u041f\u0440\u043e\u0439\u0442\u0438 \u0447\u0435\u0440\u0435\u0437 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u044b\u0435 \u0442\u0443\u043d\u043d\u0435\u043b\u0438 \u0438 \u043c\u0438\u043d\u043e\u0432\u0430\u0442\u044c \u043e\u0445\u0440\u0430\u043d\u0443", "type": "location", "target": "underground_tunnels_checkpoint", "count": 1, "optional": false}, {"id": "reach_hidden_bar", "text": "\u0414\u043e\u0431\u0440\u0430\u0442\u044c\u0441\u044f \u0434\u043e \u043f\u043e\u0434\u043f\u043e\u043b\u044c\u043d\u043e\u0433\u043e \u0431\u0430\u0440\u0430 'The Vault'", "type": "location", "target": "the_vault_bar_entrance", "count": 1, "optional": false}, {"id": "contact_informant", "text": "\u041d\u0430\u0439\u0442\u0438 \u0438 \u0437\u0430\u0433\u043e\u0432\u043e\u0440\u0438\u0442\u044c \u0441 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u043e\u0440\u043e\u043c \u043f\u043e \u0438\u043c\u0435\u043d\u0438 'Whisper'", "type": "interact", "target": "whisper_informant", "count": 1, "optional": false}, {"id": "complete_task", "text": "\u0412\u044b\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u0437\u0430\u0434\u0430\u043d\u0438\u0435 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u043e\u0440\u0430: \u0434\u043e\u0441\u0442\u0430\u0432\u0438\u0442\u044c \u043f\u043e\u0441\u044b\u043b\u043a\u0443 \u0432 \u043f\u043e\u0440\u0442", "type": "delivery", "target": "informant_package_delivery", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Корпоративный шантаж в банке',
    'Игрок проникает в один из банков Сиэтла, находит компрометирующие данные на менеджера
и использует их для получения доступа к корпоративным счетам или информации.
',
    10,
    25,
    'active',
    '{"id": "canon-quest-seattle-037-corporate-blackmail", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-037-corporate-blackmail.yaml"}'::jsonb,
    '{"experience": 5000, "money": 2500, "reputation": {"corporate_rivalry": 20, "underworld": 25}, "items": [{"id": "corporate_access_card", "name": "\u041a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u0430\u044f \u043a\u0430\u0440\u0442\u0430 \u0434\u043e\u0441\u0442\u0443\u043f\u0430", "type": "key_item", "rarity": "rare", "description": "\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u043c \u043e\u0431\u044a\u0435\u043a\u0442\u0430\u043c \u0432 \u0421\u0438\u044d\u0442\u043b\u0435"}, {"id": "financial_data_chip", "name": "\u0427\u0438\u043f \u0441 \u0444\u0438\u043d\u0430\u043d\u0441\u043e\u0432\u044b\u043c\u0438 \u0434\u0430\u043d\u043d\u044b\u043c\u0438", "type": "data", "rarity": "uncommon", "description": "\u0414\u0430\u043d\u043d\u044b\u0435 \u043e \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0445 \u0442\u0440\u0430\u043d\u0437\u0430\u043a\u0446\u0438\u044f\u0445 \u0438 \u0441\u0447\u0435\u0442\u0430\u0445"}], "unlocks": {"achievements": ["\u041a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u0428\u0430\u043d\u0442\u0430\u0436\u0438\u0441\u0442"], "flags": ["corporate_blackmail_completed", "bank_manager_compromised"], "quests": ["quest-038-corporate-retaliation"]}}'::jsonb,
    '[{"id": "gather_intelligence", "text": "\u0421\u043e\u0431\u0440\u0430\u0442\u044c \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440\u0435 \u0431\u0430\u043d\u043a\u0430 \u0447\u0435\u0440\u0435\u0437 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u043e\u0440\u043e\u0432", "type": "gather_info", "target": "bank_manager_intelligence", "count": 3, "optional": false}, {"id": "infiltrate_bank", "text": "\u041f\u0440\u043e\u043d\u0438\u043a\u043d\u0443\u0442\u044c \u0432 \u0437\u0434\u0430\u043d\u0438\u0435 \u0431\u0430\u043d\u043a\u0430, \u043c\u0438\u043d\u0443\u044f \u043e\u0445\u0440\u0430\u043d\u0443 \u0438 \u0441\u0438\u0441\u0442\u0435\u043c\u044b \u0431\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438", "type": "location", "target": "bank_building_interior", "count": 1, "optional": false}, {"id": "access_executive_floor", "text": "\u0414\u043e\u0431\u0440\u0430\u0442\u044c\u0441\u044f \u0434\u043e executive \u044d\u0442\u0430\u0436\u0430, \u0433\u0434\u0435 \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u043e\u0444\u0438\u0441 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440\u0430", "type": "location", "target": "executive_floor_access", "count": 1, "optional": false}, {"id": "find_compromising_data", "text": "\u041d\u0430\u0439\u0442\u0438 \u043a\u043e\u043c\u043f\u0440\u043e\u043c\u0435\u0442\u0438\u0440\u0443\u044e\u0449\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0435 \u043d\u0430 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0433\u043e \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440\u0430", "type": "search", "target": "manager_compromising_files", "count": 1, "optional": false}, {"id": "confront_manager", "text": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u0448\u0430\u043d\u0442\u0430\u0436 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440\u0430 \u0438 \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0436\u0435\u043b\u0430\u0435\u043c\u043e\u0435", "type": "interact", "target": "manager_blackmail_confrontation", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Протест против корпорации',
    'Игрок присоединяется к движению сопротивления, организует протест против корпорации,
преодолевает препятствия от корпоративной охраны и добивается значимых изменений.
',
    15,
    30,
    'active',
    '{"id": "canon-quest-seattle-038-corporate-protest", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-038-corporate-protest.yaml"}'::jsonb,
    '{"experience": 7500, "money": 1000, "reputation": {"activism": 30, "street_cred": 20, "corporate_rivalry": 25}, "items": [{"id": "resistance_armband", "name": "\u0411\u0440\u0430\u0441\u043b\u0435\u0442 \u0421\u043e\u043f\u0440\u043e\u0442\u0438\u0432\u043b\u0435\u043d\u0438\u044f", "type": "accessory", "rarity": "uncommon", "description": "\u0421\u0438\u043c\u0432\u043e\u043b \u043f\u0440\u0438\u043d\u0430\u0434\u043b\u0435\u0436\u043d\u043e\u0441\u0442\u0438 \u043a \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044e \u0441\u043e\u043f\u0440\u043e\u0442\u0438\u0432\u043b\u0435\u043d\u0438\u044f"}, {"id": "protest_flyers", "name": "\u041f\u0440\u043e\u043f\u0430\u0433\u0430\u043d\u0434\u0438\u0441\u0442\u0441\u043a\u0438\u0435 \u043b\u0438\u0441\u0442\u043e\u0432\u043a\u0438", "type": "consumable", "rarity": "common", "description": "\u041c\u043e\u0436\u043d\u043e \u0440\u0430\u0441\u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u044f\u0442\u044c \u0434\u043b\u044f \u0432\u0435\u0440\u0431\u043e\u0432\u043a\u0438 \u0441\u0442\u043e\u0440\u043e\u043d\u043d\u0438\u043a\u043e\u0432"}], "unlocks": {"achievements": ["\u0411\u043e\u0440\u0435\u0446 \u0441 \u041a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u044f\u043c\u0438"], "flags": ["corporate_protest_completed", "resistance_member"], "quests": ["quest-039-underground-resistance"], "factions": [{"name": "Seattle Resistance", "reputation_bonus": 15}]}}'::jsonb,
    '[{"id": "join_resistance", "text": "\u041f\u0440\u0438\u0441\u043e\u0435\u0434\u0438\u043d\u0438\u0442\u044c\u0441\u044f \u043a \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044e \u0441\u043e\u043f\u0440\u043e\u0442\u0438\u0432\u043b\u0435\u043d\u0438\u044f \u0432 Capitol Hill", "type": "interact", "target": "resistance_meeting_capitol_hill", "count": 1, "optional": false}, {"id": "gather_supporters", "text": "\u041d\u0430\u0431\u0440\u0430\u0442\u044c 10+ \u0441\u0442\u043e\u0440\u043e\u043d\u043d\u0438\u043a\u043e\u0432 \u0434\u043b\u044f \u043f\u0440\u043e\u0442\u0435\u0441\u0442\u0430", "type": "recruit", "target": "protest_supporters", "count": 10, "optional": false}, {"id": "plan_protest_route", "text": "\u0421\u043f\u043b\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043c\u0430\u0440\u0448\u0440\u0443\u0442 \u043f\u0440\u043e\u0442\u0435\u0441\u0442\u0430 \u0447\u0435\u0440\u0435\u0437 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u0440\u0430\u0439\u043e\u043d", "type": "planning", "target": "protest_route_planning", "count": 1, "optional": false}, {"id": "acquire_permits", "text": "\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0438\u043b\u0438 \u043f\u043e\u0434\u0434\u0435\u043b\u0430\u0442\u044c \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0438\u044f \u043d\u0430 \u043f\u0440\u043e\u0432\u0435\u0434\u0435\u043d\u0438\u0435 \u043f\u0440\u043e\u0442\u0435\u0441\u0442\u0430", "type": "gather_info", "target": "protest_permits", "count": 1, "optional": false}, {"id": "execute_protest", "text": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u043f\u0440\u043e\u0442\u0435\u0441\u0442 \u0443 \u0437\u0434\u0430\u043d\u0438\u044f \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u0438, \u043f\u0440\u0435\u043e\u0434\u043e\u043b\u0435\u0432\u0430\u044f \u043e\u0445\u0440\u0430\u043d\u0443", "type": "event", "target": "corporate_protest_execution", "count": 1, "optional": false}, {"id": "achieve_objective", "text": "\u0414\u043e\u0431\u0438\u0442\u044c\u0441\u044f \u0446\u0435\u043b\u0438 \u043f\u0440\u043e\u0442\u0435\u0441\u0442\u0430: \u043e\u0441\u0432\u043e\u0431\u043e\u0436\u0434\u0435\u043d\u0438\u044f \u043f\u043e\u043b\u0438\u0442\u0437\u0430\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u043e\u0433\u043e \u0438\u043b\u0438 \u043e\u0442\u043c\u0435\u043d\u044b \u0437\u0430\u043a\u043e\u043d\u0430", "type": "success_condition", "target": "protest_objective_achieved", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Сиэтл 2020-2029 — Тайный альянс с факцией',
    'Игрок становится посредником в тайных переговорах между фракциями,
выполняет секретные задания и формирует альянс, который изменит баланс сил в городе.
',
    20,
    35,
    'active',
    '{"id": "canon-quest-seattle-039-faction-alliance", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-039-faction-alliance.yaml"}'::jsonb,
    '{"experience": 10000, "money": 3000, "reputation": {"faction_loyalty": 40, "diplomatic": 25}, "items": [{"id": "faction_signet_ring", "name": "\u041f\u0435\u0440\u0441\u0442\u0435\u043d\u044c \u0424\u0440\u0430\u043a\u0446\u0438\u0438", "type": "accessory", "rarity": "rare", "description": "\u0421\u0438\u043c\u0432\u043e\u043b \u043f\u0440\u0438\u043d\u0430\u0434\u043b\u0435\u0436\u043d\u043e\u0441\u0442\u0438 \u043a \u0444\u0440\u0430\u043a\u0446\u0438\u0438 \u0438 \u0437\u043d\u0430\u043a \u0434\u043e\u0432\u0435\u0440\u0438\u044f"}, {"id": "alliance_contract", "name": "\u0414\u043e\u0433\u043e\u0432\u043e\u0440 \u0410\u043b\u044c\u044f\u043d\u0441\u0430", "type": "document", "rarity": "epic", "description": "\u042e\u0440\u0438\u0434\u0438\u0447\u0435\u0441\u043a\u0438 \u043e\u0431\u044f\u0437\u044b\u0432\u0430\u044e\u0449\u0438\u0439 \u0434\u043e\u0433\u043e\u0432\u043e\u0440 \u0441 \u0444\u0440\u0430\u043a\u0446\u0438\u0435\u0439"}], "unlocks": {"achievements": ["\u041c\u0430\u0441\u0442\u0435\u0440 \u0414\u0438\u043f\u043b\u043e\u043c\u0430\u0442\u0438\u0438"], "flags": ["faction_alliance_formed", "diplomatic_master"], "quests": ["quest-040-alliance-benefits"], "factions": [{"name": "Dynamic Faction Alliance", "reputation_bonus": 30, "special_access": true}]}}'::jsonb,
    '[{"id": "establish_contact", "text": "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u043a\u043e\u043d\u0442\u0430\u043a\u0442 \u0441 \u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u0438\u0442\u0435\u043b\u0435\u043c \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u0444\u0440\u0430\u043a\u0446\u0438\u0438 \u0447\u0435\u0440\u0435\u0437 \u043d\u0435\u0439\u0442\u0440\u0430\u043b\u044c\u043d\u043e\u0433\u043e \u043f\u043e\u0441\u0440\u0435\u0434\u043d\u0438\u043a\u0430", "type": "interact", "target": "faction_representative_contact", "count": 1, "optional": false}, {"id": "prove_loyalty", "text": "\u0412\u044b\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u0442\u0435\u0441\u0442\u043e\u0432\u043e\u0435 \u0437\u0430\u0434\u0430\u043d\u0438\u0435 \u0434\u043b\u044f \u0434\u0435\u043c\u043e\u043d\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043b\u043e\u044f\u043b\u044c\u043d\u043e\u0441\u0442\u0438 \u0444\u0440\u0430\u043a\u0446\u0438\u0438", "type": "task_completion", "target": "faction_loyalty_task", "count": 1, "optional": false}, {"id": "gather_intelligence", "text": "\u0421\u043e\u0431\u0440\u0430\u0442\u044c \u0440\u0430\u0437\u0432\u0435\u0434\u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u043a\u043e\u043d\u043a\u0443\u0440\u0438\u0440\u0443\u044e\u0449\u0438\u0445 \u0444\u0440\u0430\u043a\u0446\u0438\u044f\u0445", "type": "espionage", "target": "rival_faction_intelligence", "count": 5, "optional": false}, {"id": "mediate_negotiation", "text": "\u041f\u043e\u0441\u0440\u0435\u0434\u043d\u0438\u0447\u0430\u0442\u044c \u0432 \u043f\u0435\u0440\u0435\u0433\u043e\u0432\u043e\u0440\u0430\u0445 \u043c\u0435\u0436\u0434\u0443 \u0444\u0440\u0430\u043a\u0446\u0438\u044f\u043c\u0438 \u0434\u043b\u044f \u0444\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f \u0430\u043b\u044c\u044f\u043d\u0441\u0430", "type": "diplomacy", "target": "inter_faction_negotiation", "count": 1, "optional": false}, {"id": "execute_secret_operation", "text": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u0441\u043e\u0432\u043c\u0435\u0441\u0442\u043d\u0443\u044e \u0441\u0435\u043a\u0440\u0435\u0442\u043d\u0443\u044e \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u044e \u0441 \u043d\u043e\u0432\u043e\u0439 \u0444\u0440\u0430\u043a\u0446\u0438\u0435\u0439-\u0441\u043e\u044e\u0437\u043d\u0438\u043a\u043e\u043c", "type": "mission", "target": "alliance_secret_operation", "count": 1, "optional": false}, {"id": "solidify_alliance", "text": "\u0417\u0430\u043a\u0440\u0435\u043f\u0438\u0442\u044c \u0430\u043b\u044c\u044f\u043d\u0441 \u0444\u043e\u0440\u043c\u0430\u043b\u044c\u043d\u044b\u043c \u0434\u043e\u0433\u043e\u0432\u043e\u0440\u043e\u043c \u0438\u043b\u0438 \u0440\u0438\u0442\u0443\u0430\u043b\u043e\u043c", "type": "ceremony", "target": "alliance_ceremony", "count": 1, "optional": false}]'::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;
