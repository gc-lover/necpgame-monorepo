--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-2029-yakuza-corporate-initiation runOnChange:true

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
    '43385594-79b5-4247-98e3-435c48b3b0f4',
    'canon-quest-tokyo-2029-yakuza-corporate-initiation',
    'Tokyo 2020-2029 - Yakuza Corporate Initiation',
    'Tokyo 2020-2029 - Yakuza Corporate Initiation',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    38,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 38,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "receive_enhanced_tattoo",
      "text": "Receive the enhanced Yakuza tattoo with corporate tracking chips",
      "type": "receive",
      "target": "enhanced_tattoo",
      "count": 1,
      "optional": false
    },
    {
      "id": "complete_corporate_espionage",
      "text": "Complete corporate espionage mission for initiation",
      "type": "complete",
      "target": "corporate_espionage",
      "count": 1,
      "optional": false
    },
    {
      "id": "navigate_loyalty_conflict",
      "text": "Navigate the conflict between traditional honor and corporate loyalty",
      "type": "navigate",
      "target": "loyalty_conflict",
      "count": 1,
      "optional": false
    },
    {
      "id": "prove_initiation_worth",
      "text": "Prove initiation worth by challenging corporate Yakuza corruption",
      "type": "prove",
      "target": "initiation_worth",
      "count": 1,
      "optional": false
    },
    {
      "id": "choose_faction_loyalty",
      "text": "Choose between corporate-backed Yakuza or traditional purist faction",
      "type": "choose",
      "target": "faction_loyalty",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 32000,
    "money": 5400,
    "items": [
      {
        "item_id": "corporate_tracking_chip",
        "quantity": 1
      },
      {
        "item_id": "yakuza_loyalty_token",
        "quantity": 1
      }
    ],
    "reputation": {
      "yakuza_corporate": 65,
      "yakuza_traditional": 50,
      "corporations": 45
    },
    "unlocks": [
      "yakuza_network_access",
      "corporate_espionage_training"
    ]
  },
  "branches": [
    {
      "branch_id": "embrace_corporate_power",
      "description": "Embrace corporate power and rise through corporate Yakuza ranks",
      "consequences": {
        "reputation": {
          "yakuza_corporate": 80,
          "corporations": 70
        },
        "unlocks": [
          "corporate_yakuza_privileges",
          "executive_protection_contracts"
        ]
      }
    },
    {
      "branch_id": "preserve_traditional_honor",
      "description": "Preserve traditional honor and join underground Yakuza purists",
      "consequences": {
        "reputation": {
          "yakuza_traditional": 85,
          "corporations": -60
        },
        "unlocks": [
          "traditional_yakuza_network",
          "honor_based_operations"
        ]
      }
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Description",
    "body": "Tokyo's legendary Yakuza clans have evolved into corporate enforcers, where traditional\ninitiation rituals now involve corporate espionage and loyalty to profit over honor.\nPlayers must navigate this complex underworld where ancient traditions clash with\nmodern corporate interests.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Stages",
    "body": "1. Receive the enhanced Yakuza tattoo with corporate tracking chips\n2. Complete corporate espionage mission for initiation\n3. Navigate the conflict between traditional honor and corporate loyalty\n4. Prove initiation worth by challenging corporate Yakuza corruption\n5. Choose between corporate-backed Yakuza or traditional purist faction\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Regional Details",
    "body": "Tokyo 2020-2029 represents the merger of traditional Japanese organized crime with\ncorporate interests. Yakuza clans serve as private security for megacorporations while\nmaintaining ancient rituals and tattoos. Underground movements preserve pure Yakuza\ntraditions free from corporate influence.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Rewards",
    "body": "- Experience: 32,000 XP\n- Money: 5,400 Eddies\n- Corporate tracking chip\n- Yakuza loyalty token\n- Yakuza corporate reputation: +65\n- Corporations reputation: +45\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "mechanics",
    "title": "Mechanics",
    "body": "Quest combines stealth espionage with moral choice systems. Player performs corporate\ninfiltration missions while managing reputation with different Yakuza factions and\nmaking choices that affect traditional honor vs. corporate profit.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "connections",
    "title": "Connections",
    "body": "Opens organized crime quest series. Connects to corporate security networks,\ntraditional Yakuza purists, and underground criminal economies across Tokyo.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'abfe256c109682c7ba20c5224d9c58662bfe7752b44de776416365f7a439f038',
    '10b44b056ac8ddb88527603ae09faeb96ad0fd8099597422f190435d77850fdf',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-002-yakuza-corporate-initiation.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-2029-yakuza-corporate-initiation';

