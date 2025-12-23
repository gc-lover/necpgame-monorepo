-- Issue: #backend-quest-import-ramen-master-challenge
-- PERFORMANCE: Batch insert for single quest import
-- SOLID: Single Responsibility - import ramen master challenge quest

INSERT INTO gameplay.quest_definitions (
    quest_id,
    title,
    description,
    quest_type,
    status,
    level_min,
    level_max,
    estimated_duration_minutes,
    experience_reward,
    currency_reward,
    item_rewards,
    skill_requirements,
    prerequisites,
    objectives,
    dialogues,
    locations,
    npcs_involved,
    created_at,
    updated_at,
    version
) VALUES (
    'quest-tokyo-2029-ramen-master-challenge',
    'Tokyo 2020-2029 - Ramen Master Challenge',
    'Corporate ramen chains threaten traditional ramen masters and authentic street food culture. Become a ramen master and protect Tokyo''s culinary heritage from corporate standardization.',
    'main_story',
    'available',
    15,
    25,
    180,
    5000,
    2500,
    '[{"item_id": "ramen_master_certificate", "quantity": 1}, {"item_id": "artisanal_chopsticks", "quantity": 1}]',
    '[{"skill": "cooking", "level": 8}, {"skill": "negotiation", "level": 5}]',
    '[]',
    '[
        {
            "id": "learn_ramen_basics",
            "description": "Learn the fundamentals of ramen preparation from Master Tanaka",
            "type": "talk_to_npc",
            "target": "npc-master-tanaka",
            "location": "tokyo_ramen_district",
            "status": "available"
        },
        {
            "id": "gather_ingredients",
            "description": "Collect fresh ingredients from Tokyo markets",
            "type": "collect_items",
            "items": [
                {"item_id": "premium_pork_bones", "quantity": 5},
                {"item_id": "organic_kelp", "quantity": 3},
                {"item_id": "aged_soy_sauce", "quantity": 2}
            ],
            "location": "tokyo_tsukiji_market",
            "status": "locked"
        },
        {
            "id": "prepare_broth",
            "description": "Prepare the perfect tonkotsu broth",
            "type": "crafting",
            "recipe_id": "tonkotsu_broth_recipe",
            "location": "player_workshop",
            "status": "locked"
        },
        {
            "id": "challenge_corporate_rival",
            "description": "Defeat corporate ramen chain representative in cooking duel",
            "type": "combat",
            "enemy_id": "corporate_ramen_exec",
            "location": "tokyo_food_festival",
            "status": "locked"
        },
        {
            "id": "open_artisan_shop",
            "description": "Open your own ramen shop in Shibuya",
            "type": "business_setup",
            "location": "shibuya_district",
            "requirements": {"currency": 10000, "reputation": 100},
            "status": "locked"
        }
    ]',
    '[
        {
            "npc_id": "master_tanaka",
            "dialogue_id": "ramen_master_greeting",
            "location": "tokyo_ramen_district"
        },
        {
            "npc_id": "market_vendor",
            "dialogue_id": "ingredient_purchase",
            "location": "tokyo_tsukiji_market"
        },
        {
            "npc_id": "corporate_exec",
            "dialogue_id": "cooking_duel_challenge",
            "location": "tokyo_food_festival"
        }
    ]',
    '[
        {
            "location_id": "tokyo_ramen_district",
            "name": "Tokyo Ramen District",
            "coordinates": {"lat": 35.6895, "lng": 139.6917},
            "type": "district"
        },
        {
            "location_id": "tokyo_tsukiji_market",
            "name": "Tsukiji Outer Market",
            "coordinates": {"lat": 35.6658, "lng": 139.7706},
            "type": "market"
        },
        {
            "location_id": "tokyo_food_festival",
            "name": "Tokyo Food Festival Grounds",
            "coordinates": {"lat": 35.6586, "lng": 139.7454},
            "type": "event_venue"
        },
        {
            "location_id": "shibuya_district",
            "name": "Shibuya Entertainment District",
            "coordinates": {"lat": 35.6618, "lng": 139.7041},
            "type": "district"
        }
    ]',
    '[
        {
            "npc_id": "master_tanaka",
            "name": "Master Tanaka",
            "role": "ramen_master",
            "archetype": "wise_mentor"
        },
        {
            "npc_id": "market_vendor",
            "name": "Old Man Sato",
            "role": "ingredient_supplier",
            "archetype": "gruff_merchant"
        },
        {
            "npc_id": "corporate_exec",
            "name": "Kenji Nakamura",
            "role": "corporate_rival",
            "archetype": "smug_businessman"
        }
    ]',
    NOW(),
    NOW(),
    '1.0.0'
);

-- PERFORMANCE: Create indexes for quest lookups
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_quest_tokyo_ramen_challenge
ON gameplay.quest_definitions (quest_id)
WHERE quest_id = 'quest-tokyo-2029-ramen-master-challenge';

-- PERFORMANCE: Add quest to player quest progress tracking
INSERT INTO gameplay.player_quest_progress (
    player_id,
    quest_id,
    status,
    started_at,
    created_at,
    updated_at
) SELECT
    p.id,
    'quest-tokyo-2029-ramen-master-challenge',
    'available',
    NOW(),
    NOW(),
    NOW()
FROM gameplay.players p
WHERE p.level >= 15
  AND p.location_city = 'Tokyo'
  AND NOT EXISTS (
      SELECT 1 FROM gameplay.player_quest_progress pqp
      WHERE pqp.player_id = p.id
        AND pqp.quest_id = 'quest-tokyo-2029-ramen-master-challenge'
  );

-- PERFORMANCE: Update quest statistics
INSERT INTO gameplay.quest_statistics (
    quest_id,
    total_players_started,
    total_players_completed,
    average_completion_time_minutes,
    created_at,
    updated_at
) VALUES (
    'quest-tokyo-2029-ramen-master-challenge',
    0,
    0,
    180,
    NOW(),
    NOW()
) ON CONFLICT (quest_id) DO NOTHING;
