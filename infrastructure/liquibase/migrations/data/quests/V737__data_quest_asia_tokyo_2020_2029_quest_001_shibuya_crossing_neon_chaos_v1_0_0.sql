-- Issue: #backend-quest-update-shibuya-crossing-neon-chaos
-- PERFORMANCE: Optimized update for Shibuya Crossing quest with neon chaos theme
-- SOLID: Single Responsibility - update shibuya crossing quest with neon chaos elements

-- Update existing quest with neon chaos theme
UPDATE gameplay.quest_definitions SET
    title = 'Tokyo 2020-2029 - Shibuya Crossing Neon Chaos',
    description = 'Shibuya Crossing has become a corporate-controlled neon chaos where pedestrians are manipulated by digital advertising. Navigate the crossing''s digital maze and expose corporate manipulation of pedestrian behavior.',
    quest_type = 'main',
    level_min = 42,
    level_max = NULL,
    estimated_duration_minutes = 240,
    experience_reward = 8500,
    currency_reward = 4200,
    item_rewards = '[{"item_id": "neon_hacking_device", "quantity": 1}, {"item_id": "corporate_access_card", "quantity": 1}]',
    skill_requirements = '[{"skill": "hacking", "level": 12}, {"skill": "stealth", "level": 10}, {"skill": "negotiation", "level": 8}]',
    objectives = '[
        {
            "id": "cross_digital_barrier",
            "description": "Cross the digital barrier at Shibuya Crossing entrance",
            "type": "navigation",
            "target": "digital_barrier",
            "location": "shibuya_crossing_entrance",
            "status": "available"
        },
        {
            "id": "navigate_neon_maze",
            "description": "Navigate through the neon maze of corporate billboards",
            "type": "puzzle",
            "target": "neon_maze",
            "location": "shibuya_crossing_main",
            "status": "locked"
        },
        {
            "id": "hack_advertising_network",
            "description": "Hack the central advertising network controlling the crossing",
            "type": "hacking",
            "target": "advertising_network",
            "location": "shibuya_command_center",
            "status": "locked"
        },
        {
            "id": "confront_marketing_executive",
            "description": "Confront the corporate marketing executive at the crossing command center",
            "type": "combat",
            "target": "marketing_executive",
            "location": "shibuya_command_center",
            "status": "locked"
        },
        {
            "id": "free_pedestrians",
            "description": "Free pedestrians from digital mind control",
            "type": "rescue",
            "target": "mind_controlled_pedestrians",
            "location": "shibuya_crossing_main",
            "count": 50,
            "status": "locked"
        }
    ]',
    dialogues = '[
        {
            "npc_id": "underground_hacker",
            "dialogue_id": "shibuya_hacker_greeting",
            "location": "shibuya_underground"
        },
        {
            "npc_id": "corporate_executive",
            "dialogue_id": "executive_threat",
            "location": "shibuya_command_center"
        },
        {
            "npc_id": "mind_controlled_pedestrian",
            "dialogue_id": "pedestrian_awakening",
            "location": "shibuya_crossing_main"
        }
    ]',
    locations = '[
        {
            "location_id": "shibuya_crossing_entrance",
            "name": "Shibuya Crossing Entrance",
            "coordinates": {"lat": 35.6590, "lng": 139.7006},
            "type": "entrance"
        },
        {
            "location_id": "shibuya_crossing_main",
            "name": "Shibuya Crossing Main Plaza",
            "coordinates": {"lat": 35.6595, "lng": 139.7006},
            "type": "plaza"
        },
        {
            "location_id": "shibuya_command_center",
            "name": "Corporate Command Center",
            "coordinates": {"lat": 35.6600, "lng": 139.7010},
            "type": "building"
        },
        {
            "location_id": "shibuya_underground",
            "name": "Shibuya Underground Network",
            "coordinates": {"lat": 35.6585, "lng": 139.7000},
            "type": "underground"
        }
    ]',
    npcs_involved = '[
        {
            "npc_id": "underground_hacker",
            "name": "Zero Cool",
            "role": "resistance_leader",
            "archetype": "cyberpunk_hacker"
        },
        {
            "npc_id": "corporate_executive",
            "name": "Takashi Mori",
            "role": "antagonist",
            "archetype": "corporate_villain"
        },
        {
            "npc_id": "mind_controlled_pedestrian",
            "name": "Various Civilians",
            "role": "victims",
            "archetype": "innocent_bystanders"
        }
    ]',
    updated_at = NOW(),
    version = '1.0.0'
WHERE quest_id = 'quest-tokyo-2029-shibuya-crossing';

-- PERFORMANCE: Update quest progress for existing players
UPDATE gameplay.player_quest_progress SET
    status = 'available',
    updated_at = NOW()
WHERE quest_id = 'quest-tokyo-2029-shibuya-crossing'
  AND status IN ('completed', 'failed');

-- PERFORMANCE: Add new quest progress for players who don't have it
INSERT INTO gameplay.player_quest_progress (
    player_id,
    quest_id,
    status,
    started_at,
    created_at,
    updated_at
) SELECT
    p.id,
    'quest-tokyo-2029-shibuya-crossing',
    'available',
    NOW(),
    NOW(),
    NOW()
FROM gameplay.players p
WHERE p.level >= 42
  AND p.location_city = 'Tokyo'
  AND NOT EXISTS (
      SELECT 1 FROM gameplay.player_quest_progress pqp
      WHERE pqp.player_id = p.id
        AND pqp.quest_id = 'quest-tokyo-2029-shibuya-crossing'
  );

-- PERFORMANCE: Update quest statistics
UPDATE gameplay.quest_statistics SET
    updated_at = NOW()
WHERE quest_id = 'quest-tokyo-2029-shibuya-crossing';
