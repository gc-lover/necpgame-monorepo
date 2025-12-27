--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-2029-shibuya-crossing-neon-chaos runOnChange:true

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
    '0a201b74-d8b7-4a39-9839-5f478706d0b4',
    'canon-quest-tokyo-2029-shibuya-crossing-neon-chaos',
    'Tokyo 2020-2029 - Shibuya Crossing Neon Chaos',
    'Tokyo 2020-2029 - Shibuya Crossing Neon Chaos',
    'main',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    42,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "main",
  "level_min": 42,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "cross_digital_barrier",
      "text": "Cross the digital barrier at Shibuya Crossing entrance",
      "type": "cross",
      "target": "digital_barrier",
      "count": 1,
      "optional": false
    },
    {
      "id": "navigate_neon_maze",
      "text": "Navigate through the neon maze of corporate billboards",
      "type": "navigate",
      "target": "neon_maze",
      "count": 1,
      "optional": false
    },
    {
      "id": "hack_advertising_network",
      "text": "Hack the central advertising network controlling the crossing",
      "type": "hack",
      "target": "advertising_network",
      "count": 1,
      "optional": false
    },
    {
      "id": "confront_marketing_executive",
      "text": "Confront the corporate marketing executive at the crossing command center",
      "type": "confront",
      "target": "marketing_executive",
      "count": 1,
      "optional": false
    },
    {
      "id": "restore_pedestrian_autonomy",
      "text": "Restore pedestrian autonomy by shutting down mind control algorithms",
      "type": "restore",
      "target": "pedestrian_autonomy",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 36000,
    "money": 6200,
    "items": [
      {
        "item_id": "neon_jammer",
        "quantity": 1
      },
      {
        "item_id": "anti_advertising_implant",
        "quantity": 1
      }
    ],
    "reputation": {
      "corporations": -70,
      "pedestrians": 75,
      "hackers": 50
    },
    "unlocks": [
      "shibuya_underground_access",
      "advertising_countermeasures"
    ]
  },
  "branches": [
    {
      "branch_id": "seize_advertising_control",
      "description": "Seize control of advertising network for citizen messaging",
      "consequences": {
        "reputation": {
          "corporations": -90,
          "pedestrians": 60
        },
        "unlocks": [
          "citizen_advertising_network",
          "neon_district_influence"
        ]
      }
    },
    {
      "branch_id": "destroy_billboard_infrastructure",
      "description": "Destroy the billboard infrastructure to end corporate manipulation",
      "consequences": {
        "reputation": {
          "corporations": -80,
          "pedestrians": 55
        },
        "unlocks": [
          "billboard_free_zones",
          "analog_communication_network"
        ]
      }
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Description",
    "body": "Tokyo's iconic Shibuya Crossing has evolved into a neon-drenched corporate battlefield\nwhere massive digital billboards manipulate pedestrian movement and implant subliminal\nmessages. The player must navigate this chaotic intersection while battling corporate\ncontrol over human behavior and advertising dominance.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Stages",
    "body": "1. Cross the digital barrier at Shibuya Crossing entrance\n2. Navigate through the neon maze of corporate billboards\n3. Hack the central advertising network controlling the crossing\n4. Confront the corporate marketing executive at the crossing command center\n5. Restore pedestrian autonomy by shutting down mind control algorithms\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Regional Details",
    "body": "Tokyo 2020-2029 represents the peak of corporate advertising dominance. Shibuya Crossing\nserves as ground zero for digital manipulation, where pedestrian traffic patterns are\ncontrolled by algorithms, and every surface displays corporate messaging. Underground\nresistance movements work to preserve human autonomy in the face of technological control.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Rewards",
    "body": "- Experience: 36,000 XP\n- Money: 6,200 Eddies\n- Neon jammer\n- Anti-advertising implant\n- Pedestrians reputation: +75\n- Corporations reputation: -70\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "mechanics",
    "title": "Mechanics",
    "body": "Quest combines crowd navigation with digital hacking. Player must time crossings with\ntraffic lights, avoid corporate security drones, and hack billboards in real-time while\nmanaging exposure to subliminal messaging.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "connections",
    "title": "Connections",
    "body": "Opens corporate advertising quest series. Connects to hacker collectives, pedestrian\nrights activists, and anti-corporate resistance movements throughout Tokyo's districts.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'c8af370dbf74f83e74b04976e34e0a6698e2f8ed35c1b05a864e1b3ca38a446a',
    '781b1d4a7784b2c97c0e13bcffebddc44fc6559c1bd24b7bf10ef4ba446fbe9d',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-001-shibuya-crossing-neon-chaos.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-2029-shibuya-crossing-neon-chaos';

