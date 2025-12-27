--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-palace-of-peace runOnChange:true

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
    'd74a01a8-13f1-4ba1-abbc-b0e27419ea74',
    'canon-quest-astana-palace-of-peace',
    'Астана 2020-2029 — «Дворец мира и согласия»',
    'Астана 2020-2029 — «Дворец мира и согласия»',
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
      "id": "explore_palace",
      "type": "explore",
      "description": "Исследовать архитектуру Дворца мира и согласия",
      "required": true
    },
    {
      "id": "attend_congress",
      "type": "interact",
      "description": "Участвовать в сессии Конгресса мировых религий",
      "required": true
    },
    {
      "id": "view_panorama",
      "type": "interact",
      "description": "Зафиксировать панораму Астаны со смотровой площадки",
      "required": true
    }
  ],
  "rewards": {
    "experience": 1000,
    "money": {
      "type": "eddies",
      "value": -15
    },
    "reputation": {
      "culture": 10
    },
    "unlocks": {
      "achievements": [
        {
          "id": "peacemaker",
          "name": "Миротворец"
        }
      ],
      "flags": [
        "interfaith_events_unlocked"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — «Дворец мира и согласия»",
    "body": "Этот side квест \"Астана 2020-2029 — «Дворец мира и согласия»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Objective 1\n2. Objective 2\n3. Objective 3\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "architecture",
    "title": "Архитектурное вступление",
    "body": "Экскурсия начинается у входа в пирамиду. Игрок узнаёт о геометрии здания, системе вентиляции, символике витражей и о том,\nкак Норман Фостер адаптировал традицию юрты к стеклу и металлу. Проводник выдаёт аудиогид с историей форума.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "congress",
    "title": "Конгресс мировых религий",
    "body": "В верхнем зале проходит заседание лидеров конфессий. Игрок может участвовать в диалогах, задавать вопросы и\nполучить временные баффы к дипломатии. Параллельно отображается интерактивная хроника конгрессов 2003–2090 годов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "panorama",
    "title": "Финал экскурсии",
    "body": "Завершение происходит на смотровой площадке с видом на Астану и Есиль. Игрок фиксирует культурную память, получает\n1 000 XP, -15 едди (стоимость экскурсии) и +10 к показателю «Культура», а также ачивку «Миротворец».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '740ea52593b9657e56527426625f3a657ca1453f6c380ba91e02b746ba3ef320',
    'cb1b132ce75a647210bb53fcee4b2f8128cc8eeaa4d90967db9f5fd344c27c48',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-007-palace-of-peace.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-palace-of-peace';

