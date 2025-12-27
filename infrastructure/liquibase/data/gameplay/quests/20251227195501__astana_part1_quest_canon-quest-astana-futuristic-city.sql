--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-futuristic-city runOnChange:true

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
    '3d0c5eeb-0f31-485e-9c99-9a905c39795f',
    'canon-quest-astana-futuristic-city',
    'Астана 2020-2029 — Город Будущего',
    'Астана 2020-2029 — Город Будущего',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    20,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 5,
  "level_max": 20,
  "requirements": [
    {
      "type": "location",
      "value": "astana"
    }
  ],
  "objectives": [
    {
      "id": "get_equipment",
      "type": "collect",
      "description": "Собрать дрон-фотооборудование",
      "required": true
    },
    {
      "id": "visit_landmarks",
      "type": "explore",
      "description": "Посетить архитектурные точки (Байтерек, Хан Шатыр, Дворец Мира, Abu Dhabi Plaza)",
      "required": true
    },
    {
      "id": "complete_challenges",
      "type": "complete",
      "description": "Выполнить фото-челленджи",
      "required": true
    },
    {
      "id": "create_album",
      "type": "complete",
      "description": "Составить цифровой альбом",
      "required": true
    }
  ],
  "rewards": {
    "experience": 2000,
    "money": {
      "type": "eddies",
      "value": 0
    },
    "reputation": {
      "aesthetics": 20
    },
    "unlocks": {
      "achievements": [
        {
          "id": "future_explorer",
          "name": "Исследователь Будущего"
        }
      ],
      "flags": [
        "premium_tourism_routes_unlocked"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — Город Будущего",
    "body": "Этот side квест \"Астана 2020-2029 — Город Будущего\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Objective 1\n2. Objective 2\n3. Objective 3\n4. Objective 4\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Экскурсия превращает Астану в открытую галерею футуристической архитектуры, подчёркивая её рождение посреди степи.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Собрать дрон-фотооборудование и получить маршрут от гида.\n2. Посетить архитектурные точки, выполнить фото-челленджи и собрать истории создания объектов.\n3. Составить цифровой альбом и поделиться в сетевой сети, получив эстетический бафф.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "2 000 XP, +20 к параметру «Эстетика», достижение «Исследователь Будущего» и доступ к премиальным туристическим маршрутам.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '889b2bd35ffbb14392afd07f3029c15c26ea339f1079301864ce01134822d2f2',
    '7619e1ca4d149df553de4c5af966b766759da4d99065922360a8968a40d0dbfe',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-010-futuristic-city.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-futuristic-city';

