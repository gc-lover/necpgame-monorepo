--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-2029-ramen-master-challenge runOnChange:true

INSERT INTO gameplay.quests (
    id,
    metadata_id,
    title,
    english_title,
    type,
    location,
    time_period,
    difficulty,
    estimated_duration,
    player_level_min,
    player_level_max,
    status,
    version,
    quest_definition,
    narrative_context,
    gameplay_mechanics,
    additional_npcs,
    environmental_challenges,
    visual_design,
    cultural_elements,
    metadata_hash,
    content_hash,
    created_at,
    updated_at,
    source_file
) VALUES (
    'f9e327c2-5474-4432-8d1c-f0ef39a57e71',
    'canon-quest-tokyo-2029-ramen-master-challenge',
    'Tokyo 2020-2029 - Ramen Master Challenge',
    'Tokyo 2020-2029 - Ramen Master Challenge',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    40,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 40,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "apprentice_under_master",
      "text": "Apprentice under legendary ramen master in Tokyo's undercity",
      "type": "apprentice",
      "target": "master",
      "count": 1,
      "optional": false
    },
    {
      "id": "gather_rare_ingredients",
      "text": "Gather rare ingredients from corporate-controlled food markets",
      "type": "gather",
      "target": "rare_ingredients",
      "count": 1,
      "optional": false
    },
    {
      "id": "perfect_broth_recipe",
      "text": "Perfect the ancient broth recipe passed down through generations",
      "type": "perfect",
      "target": "broth_recipe",
      "count": 1,
      "optional": false
    },
    {
      "id": "defeat_corporate_chain",
      "text": "Defeat corporate ramen chain in neighborhood ramen battle",
      "type": "defeat",
      "target": "corporate_chain",
      "count": 1,
      "optional": false
    },
    {
      "id": "establish_ramen_school",
      "text": "Establish underground ramen school to preserve traditional techniques",
      "type": "establish",
      "target": "ramen_school",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 34000,
    "money": 5800,
    "items": [
      {
        "item_id": "master_ramen_recipe",
        "quantity": 1
      },
      {
        "item_id": "artisan_cooking_tools",
        "quantity": 1
      }
    ],
    "reputation": {
      "ramen_masters": 75,
      "corporations": -65,
      "culinary_traditionalists": 55
    },
    "unlocks": [
      "underground_ramen_network",
      "authentic_ingredient_sources"
    ]
  },
  "branches": [
    {
      "branch_id": "create_ramen_empire",
      "description": "Create ramen empire by opening chain of authentic ramen shops",
      "consequences": {
        "reputation": {
          "ramen_masters": 90,
          "culinary_traditionalists": 70
        },
        "unlocks": [
          "ramen_franchise_network",
          "traditional_culinary_empire"
        ]
      }
    },
    {
      "branch_id": "destroy_corporate_food",
      "description": "Destroy corporate food monopolies and restore street food autonomy",
      "consequences": {
        "reputation": {
          "corporations": -85,
          "ramen_masters": 65
        },
        "unlocks": [
          "street_food_sovereignty",
          "artisan_food_network"
        ]
      }
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Description",
    "body": "Tokyo's legendary ramen culture faces extinction as corporate chains mass-produce\nstandardized instant ramen, threatening the centuries-old tradition of master craftsmen.\nPlayers must apprentice under legendary ramen masters and battle corporate food\nmonopolies to preserve authentic Japanese culinary heritage.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Stages",
    "body": "1. Apprentice under legendary ramen master in Tokyo's undercity\n2. Gather rare ingredients from corporate-controlled food markets\n3. Perfect the ancient broth recipe passed down through generations\n4. Defeat corporate ramen chain in neighborhood ramen battle\n5. Establish underground ramen school to preserve traditional techniques\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Regional Details",
    "body": "Tokyo 2020-2029 represents the peak of corporate food industrialization. Traditional\nramen shops are replaced by automated ramen vending machines and corporate food courts.\nUnderground networks of master craftsmen preserve authentic recipes and techniques,\npassing knowledge through secretive apprenticeships.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Rewards",
    "body": "- Experience: 34,000 XP\n- Money: 5,800 Eddies\n- Master ramen recipe\n- Artisan cooking tools\n- Ramen masters reputation: +75\n- Corporations reputation: -65\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "mechanics",
    "title": "Mechanics",
    "body": "Quest combines cooking simulation with corporate infiltration. Player learns ramen\ncrafting techniques, gathers ingredients from restricted markets, participates in\ncooking competitions, and makes choices about tradition vs. modernization.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "connections",
    "title": "Connections",
    "body": "Opens culinary preservation quest series. Connects to underground chef networks,\ntraditional food artisans, and anti-corporate food sovereignty movements across Tokyo.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '2c24b3b28645544170b91cacab0160e9144d662768f2e543d3829d7ea5c3377f',
    '7a21498f1f6ff4684ae852b7c030332199a17f42ab098f430afafa05c2ce44ad',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-004-ramen-master-challenge.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-2029-ramen-master-challenge';

