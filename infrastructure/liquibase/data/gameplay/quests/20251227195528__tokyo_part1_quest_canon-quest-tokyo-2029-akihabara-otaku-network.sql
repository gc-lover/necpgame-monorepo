--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-2029-akihabara-otaku-network runOnChange:true

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
    '67235c74-dd8e-4af8-a53a-db7edde8ff96',
    'canon-quest-tokyo-2029-akihabara-otaku-network',
    'Tokyo 2020-2029 - Akihabara Otaku Network',
    'Tokyo 2020-2029 - Akihabara Otaku Network',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    35,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 35,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "access_underground_otaku_club",
      "text": "Access the underground otaku club hidden beneath Akihabara's corporate facade",
      "type": "access",
      "target": "underground_otaku_club",
      "count": 1,
      "optional": false
    },
    {
      "id": "collect_rare_anime_artifacts",
      "text": "Collect rare anime artifacts from pre-corporate era",
      "type": "collect",
      "target": "rare_anime_artifacts",
      "count": 1,
      "optional": false
    },
    {
      "id": "expose_corporate_infiltration",
      "text": "Expose corporate infiltration of otaku communities",
      "type": "expose",
      "target": "corporate_infiltration",
      "count": 1,
      "optional": false
    },
    {
      "id": "hack_figure_manufacturing",
      "text": "Hack corporate figure manufacturing to restore authentic production",
      "type": "hack",
      "target": "figure_manufacturing",
      "count": 1,
      "optional": false
    },
    {
      "id": "unite_otaku_factions",
      "text": "Unite different otaku factions against corporate exploitation",
      "type": "unite",
      "target": "otaku_factions",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 30000,
    "money": 4800,
    "items": [
      {
        "item_id": "rare_anime_figure",
        "quantity": 1
      },
      {
        "item_id": "otaku_network_pass",
        "quantity": 1
      }
    ],
    "reputation": {
      "otaku_communities": 70,
      "corporations": -60,
      "geek_culture": 55
    },
    "unlocks": [
      "akihabara_underground_access",
      "authentic_subculture_network"
    ]
  },
  "branches": [
    {
      "branch_id": "establish_otaku_sovereignty",
      "description": "Establish otaku sovereignty zones free from corporate control",
      "consequences": {
        "reputation": {
          "otaku_communities": 85,
          "geek_culture": 75
        },
        "unlocks": [
          "otaku_sovereignty_zones",
          "authentic_figure_market"
        ]
      }
    },
    {
      "branch_id": "destroy_corporate_anime",
      "description": "Destroy corporate anime production and restore independent creators",
      "consequences": {
        "reputation": {
          "corporations": -80,
          "otaku_communities": 65
        },
        "unlocks": [
          "indie_anime_network",
          "creator_economy_support"
        ]
      }
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Description",
    "body": "Tokyo's Akihabara district has been transformed from a genuine otaku paradise into a\ncorporate-controlled entertainment complex where authentic subculture is exploited for\nprofit. Players must navigate the underground networks of true otaku enthusiasts while\nbattling corporate co-optation of geek culture.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Stages",
    "body": "1. Access the underground otaku club hidden beneath Akihabara's corporate facade\n2. Collect rare anime artifacts from pre-corporate era\n3. Expose corporate infiltration of otaku communities\n4. Hack corporate figure manufacturing to restore authentic production\n5. Unite different otaku factions against corporate exploitation\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Regional Details",
    "body": "Tokyo 2020-2029 marks the corporate takeover of Japan's otaku subculture. Akihabara\nhas become a neon-lit shopping mall where corporations mass-produce \"authentic\" anime\nmerchandise while underground collectives preserve the true spirit of geek enthusiasm\nand creative expression.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Rewards",
    "body": "- Experience: 30,000 XP\n- Money: 4,800 Eddies\n- Rare anime figure\n- Otaku network pass\n- Otaku communities reputation: +70\n- Corporations reputation: -60\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "mechanics",
    "title": "Mechanics",
    "body": "Quest combines collection mechanics with social networking. Player explores Akihabara's\nmulti-level districts, collects rare items, builds relationships with different otaku\nfactions, and makes choices about preserving authenticity vs. commercial appeal.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "connections",
    "title": "Connections",
    "body": "Opens subculture preservation quest series. Connects to underground anime collectives,\nindie game developers, and anti-corporate creative movements throughout Tokyo.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '3534b20b1fd39a9f8f2af70f9bdee7f38881740771a5318e9d5aa6c8678e2272',
    '865f51f8e60c2b8b5ed276ef3b7c69c9fd73dc2aec737a98d8741e01807ce258',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-003-akihabara-otaku-network.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-2029-akihabara-otaku-network';

