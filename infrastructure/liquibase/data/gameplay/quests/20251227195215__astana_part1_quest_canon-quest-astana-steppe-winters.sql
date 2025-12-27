--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-steppe-winters runOnChange:true

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
    '0d43203e-72ce-445b-bf51-c80dffec4dbc',
    'canon-quest-astana-steppe-winters',
    'Астана 2020-2029 — Степные зимы',
    'Астана 2020-2029 — Степные зимы',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    3,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 3,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": [
      "winter_gear"
    ]
  },
  "objectives": [
    {
      "id": "prepare_winter_gear",
      "text": "Подготовить зимнее снаряжение для экстремальных условий",
      "type": "interact",
      "target": "winter_gear_preparation",
      "count": 1,
      "optional": false
    },
    {
      "id": "face_extreme_cold",
      "text": "Столкнуться с экстремальными условиями при -40°C",
      "type": "interact",
      "target": "extreme_cold_experience",
      "count": 1,
      "optional": false
    },
    {
      "id": "adapt_to_climate",
      "text": "Адаптироваться к суровому степному климату",
      "type": "interact",
      "target": "steppe_climate_adaptation",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 2000,
    "money": -30,
    "reputation": {
      "survival": 15
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Степной Морозоустойчивый"
      ],
      "flags": [
        "steppe_winter_survived"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — Степные зимы",
    "body": "Этот side квест \"Астана 2020-2029 — Степные зимы\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Подготовить зимнее снаряжение для экстремальных условий\n2. Столкнуться с экстремальными условиями при -40°C\n3. Адаптироваться к суровому степному климату\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Экстремальный квест выживания в степных зимах Астаны при температуре -40°C.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Подготовить зимнее снаряжение для экстремальных условий.\n2. Столкнуться с экстремальными условиями при -40°C.\n3. Адаптироваться к суровому степному климату.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "2 000 XP, -30 едди, +15 к параметру «Выживание» и достижение «Степной Морозоустойчивый».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '8b7414d6b26a0b36bb541484c575be05269ccdc77e5f81b05737c29697c19656',
    '26a5b5b05cb4fc3ea4c0ea54256a12ac85080ee2fa1d423445493e6b82a74f99',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-005-steppe-winters.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-steppe-winters';

