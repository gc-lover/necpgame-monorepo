--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-traditional-future-blend runOnChange:true

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
    '24285a3f-fd3c-40cd-a4a9-a2fbc6ee94e9',
    'canon-quest-tokyo-traditional-future-blend',
    'Токио 2020-2029 — Слияние традиций и будущего',
    'Токио 2020-2029 — Слияние традиций и будущего',
    'cultural',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    20,
    'active',
    '1.0.0',
    '{
  "quest_type": "cultural",
  "level_min": 5,
  "level_max": 20,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "discover_ancient_temple",
      "type": "exploration",
      "description": "Найти древний храм, скрытый в современных небоскребах Токио",
      "required": true
    },
    {
      "id": "learn_traditions",
      "type": "interaction",
      "description": "Изучить традиционные ритуалы и церемонии храма",
      "required": true
    },
    {
      "id": "technological_threat",
      "type": "challenge",
      "description": "Противостоять корпоративной угрозе, планирующей снести храм",
      "required": true
    },
    {
      "id": "blend_technologies",
      "type": "crafting",
      "description": "Интегрировать современные технологии для сохранения традиций",
      "required": true
    },
    {
      "id": "preserve_culture",
      "type": "choice",
      "description": "Выбрать способ сохранения культурного наследия",
      "required": true
    }
  ],
  "rewards": {
    "experience": 1600,
    "money": {
      "type": "eddies",
      "value": 2000
    },
    "reputation": {
      "cultural": 15,
      "traditional": 10
    },
    "unlocks": {
      "achievements": [
        {
          "id": "tradition_guardian",
          "name": "Хранитель традиций"
        }
      ],
      "flags": [
        "tokyo_cultural_heritage_unlocked"
      ],
      "items": [
        {
          "id": "traditional_artifact",
          "name": "Цифровой амулет традиции"
        }
      ]
    }
  },
  "branches": [
    {
      "id": "pure_traditional",
      "name": "Чистая традиция",
      "description": "Сохранить храм без технологических модификаций"
    },
    {
      "id": "techno_traditional",
      "name": "Техно-традиция",
      "description": "Интегрировать технологии для усиления традиций"
    },
    {
      "id": "modern_adaptation",
      "name": "Современная адаптация",
      "description": "Перевести традиции в цифровой формат"
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Кратко",
    "body": "Квест-id: `TOKYO-2029-013`\nФормат: культурное исследование, сложность средняя, solo, длительность 1–2 часа.\nЛокация: скрытый храм в современном Токио.\n",
    "mechanics_links": [
      "mechanics/culture/cultural-preservation.yaml"
    ]
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Objective 1\n2. Objective 2\n3. Objective 3\n4. Objective 4\n5. Objective 5\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "workshop",
    "title": "Слияние эпох",
    "body": "1. Открытие древнего храма среди неоновых небоскребов.\n2. Изучение традиционных церемоний и ритуалов.\n3. Противостояние корпоративным разработчикам.\n4. Интеграция технологий для защиты культурного наследия.\n5. Выбор между сохранением чистоты традиций или их эволюцией.\n",
    "mechanics_links": [
      "mechanics/crafting/technological-integration.yaml"
    ]
  },
  {
    "id": "choices",
    "title": "Решения игрока",
    "body": "- Сохранить традиции в первозданном виде без компромиссов.\n- Использовать технологии для усиления и распространения традиций.\n- Цифровая адаптация - перенос ритуалов в виртуальную реальность.\n- Коммерциализация традиций для финансирования сохранения.\n",
    "mechanics_links": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "- 1600 XP, доход 2000 едди.\n- Бафф «Cultural Harmony» (+20% эффективность культурных взаимодействий).\n- Доступ к линии «Tokyo Cultural Heritage».\n- Возможные союзники среди традиционалистов или техно-энтузиастов.\n",
    "mechanics_links": [
      "mechanics/progression/progression-rewards.yaml"
    ]
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '13efc5928c11f595a3299e89aff792d1e9737031cbd0b654ffc7b3bb21dfa768',
    '9257edf9d38a6f3593c8a9b1aeaf8744d76e504c4b2fddfe61a9cc3c2e68d356',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-013-traditional-future-blend.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-traditional-future-blend';

