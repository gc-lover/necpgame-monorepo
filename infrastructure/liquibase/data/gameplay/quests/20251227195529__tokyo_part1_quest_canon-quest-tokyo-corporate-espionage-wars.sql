--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-corporate-espionage-wars runOnChange:true

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
    'c96e81f1-1e4a-4f35-97e1-5a4d9ed31a23',
    'canon-quest-tokyo-corporate-espionage-wars',
    'Токио 2020-2029 — Корпоративные войны шпионажа',
    'Токио 2020-2029 — Корпоративные войны шпионажа',
    'espionage',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    15,
    35,
    'active',
    '1.0.0',
    '{
  "quest_type": "espionage",
  "level_min": 15,
  "level_max": 35,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "join_corporation",
      "type": "interaction",
      "description": "Устроиться на работу в целевую корпорацию под прикрытием",
      "required": true
    },
    {
      "id": "gather_intelligence",
      "type": "investigation",
      "description": "Собрать разведданные о технологических секретах",
      "required": true
    },
    {
      "id": "infiltrate_secure_area",
      "type": "stealth",
      "description": "Проникнуть в защищенную зону с секретными разработками",
      "required": true
    },
    {
      "id": "steal_technology",
      "type": "challenge",
      "description": "Украсть или скопировать технологические данные",
      "required": true
    },
    {
      "id": "escape_detection",
      "type": "stealth",
      "description": "Покинуть территорию корпорации незамеченным",
      "required": true
    }
  ],
  "rewards": {
    "experience": 3500,
    "money": {
      "type": "eddies",
      "value": 8000
    },
    "reputation": {
      "espionage": 25,
      "corporate": -20
    },
    "unlocks": {
      "achievements": [
        {
          "id": "shadow_executive",
          "name": "Теневой исполнитель"
        }
      ],
      "flags": [
        "tokyo_corporate_espionage_unlocked"
      ],
      "items": [
        {
          "id": "corporate_secrets",
          "name": "Пакет корпоративных секретов"
        }
      ]
    }
  },
  "branches": [
    {
      "id": "loyal_spy",
      "name": "Лояльный шпион",
      "description": "Работать на одну корпорацию против другой"
    },
    {
      "id": "double_agent",
      "name": "Двойной агент",
      "description": "Работать на обе стороны для максимальной выгоды"
    },
    {
      "id": "whistleblower",
      "name": "Разоблачитель",
      "description": "Собрать доказательства и разоблачить обе корпорации"
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Кратко",
    "body": "Квест-id: `TOKYO-2029-015`\nФормат: корпоративный шпионаж, сложность высокая, solo, длительность 3–5 часов.\nЛокация: корпоративные небоскребы и секретные лаборатории Токио.\n",
    "mechanics_links": [
      "mechanics/espionage/corporate-spionage.yaml"
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
    "title": "Корпоративные войны",
    "body": "1. Устройство на работу в технологическую корпорацию под фальшивым именем.\n2. Постепенное продвижение по карьерной лестнице для доступа к секретам.\n3. Сбор разведданных через социальную инженерию и технические средства.\n4. Проникновение в высокозащищенные зоны с экспериментальными технологиями.\n5. Украсть ценные данные и покинуть территорию без обнаружения.\n6. Решение, кому продать украденную информацию.\n",
    "mechanics_links": [
      "mechanics/stealth/stealth-mechanics.yaml"
    ]
  },
  {
    "id": "choices",
    "title": "Решения игрока",
    "body": "- Стать лояльным шпионом одной корпорации.\n- Играть на обе стороны для максимальной выгоды.\n- Собрать доказательства коррупции и разоблачить обе корпорации.\n- Использовать технологии для создания собственной корпорации.\n- Уничтожить данные, чтобы предотвратить их использование.\n",
    "mechanics_links": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "- 3500 XP, доход 8000 едди.\n- Бафф «Espionage Master» (+30% эффективность шпионажа на 48 часов).\n- Доступ к черному рынку технологий и корпоративным контрактам.\n- Значительные изменения репутации в корпоративном мире.\n",
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
    '246b2d2a450355ed0dba07602aa983a0d1d88a654f73c71ed20e29e7c2df74b1',
    'af78828d266c5cdc7c3c85921c401954853ba01aa29d23a4ddf6407335209803',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-015-corporate-espionage-wars.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-corporate-espionage-wars';

