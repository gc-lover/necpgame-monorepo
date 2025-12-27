--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-maglev-transport-network runOnChange:true

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
    '4802daef-b5f4-4374-9b25-8fb40ab8c241',
    'canon-quest-tokyo-maglev-transport-network',
    'Токио 2020-2029 — Сеть маглев-транспорта',
    'Токио 2020-2029 — Сеть маглев-транспорта',
    'infrastructure',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    10,
    25,
    'active',
    '1.0.0',
    '{
  "quest_type": "infrastructure",
  "level_min": 10,
  "level_max": 25,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "join_transport_crew",
      "type": "interaction",
      "description": "Устроиться техником в транспортную компанию Токио",
      "required": true
    },
    {
      "id": "maintain_maglev_tracks",
      "type": "repair",
      "description": "Выполнить техническое обслуживание маглев-рельсов",
      "required": true
    },
    {
      "id": "investigate_anomaly",
      "type": "investigation",
      "description": "Расследовать странные сбои в транспортной сети",
      "required": true
    },
    {
      "id": "uncover_conspiracy",
      "type": "investigation",
      "description": "Обнаружить корпоративный заговор по саботажу конкурентов",
      "required": true
    },
    {
      "id": "make_decision",
      "type": "choice",
      "description": "Выбрать, сообщить ли о заговоре властям",
      "required": true
    }
  ],
  "rewards": {
    "experience": 2200,
    "money": {
      "type": "eddies",
      "value": 3500
    },
    "reputation": {
      "infrastructure": 12,
      "corporate": -5
    },
    "unlocks": {
      "achievements": [
        {
          "id": "rail_master",
          "name": "Мастер рельсов"
        }
      ],
      "flags": [
        "tokyo_transport_network_unlocked"
      ],
      "items": [
        {
          "id": "maglev_access_card",
          "name": "Карта доступа к маглев-сети"
        }
      ]
    }
  },
  "branches": [
    {
      "id": "loyal_employee",
      "name": "Лояльный сотрудник",
      "description": "Участвовать в корпоративном заговоре"
    },
    {
      "id": "whistleblower",
      "name": "Разоблачитель",
      "description": "Сообщить о заговоре и остановить саботаж"
    },
    {
      "id": "neutral_observer",
      "name": "Нейтральный наблюдатель",
      "description": "Не вмешиваться, но использовать знания в своих целях"
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Кратко",
    "body": "Квест-id: `TOKYO-2029-014`\nФормат: инфраструктурное расследование, сложность средняя, solo, длительность 1.5–3 часа.\nЛокация: транспортные узлы и подземные туннели Токио.\n",
    "mechanics_links": [
      "mechanics/infrastructure/transportation-systems.yaml"
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
    "title": "Транспортная сеть",
    "body": "1. Устройство на работу в транспортную корпорацию.\n2. Обслуживание высокоскоростных маглев-поездов.\n3. Расследование системных сбоев и аномалий.\n4. Обнаружение корпоративного заговора.\n5. Этический выбор между прибылью компании и общественной безопасностью.\n",
    "mechanics_links": [
      "mechanics/investigation/investigation-mechanics.yaml"
    ]
  },
  {
    "id": "choices",
    "title": "Решения игрока",
    "body": "- Стать частью корпоративного заговора для личной выгоды.\n- Разоблачить заговор и предотвратить катастрофу.\n- Использовать знания для создания собственной транспортной сети.\n- Продать информацию конкурентам.\n",
    "mechanics_links": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "- 2200 XP, доход 3500 едди.\n- Бафф «Infrastructure Insight» (+15% эффективность ремонта).\n- Доступ к транспортной сети Токио и специальным маршрутам.\n- Возможные изменения репутации с корпорациями.\n",
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
    'a1ef249b5f4f55d8e24b9e847a9e0099a458064b14d289075619cacbc03d2a05',
    '5d7cde689b92202bd144183211deba9ea2738e49a755f1c1fe816e502e501e04',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-014-maglev-transport-network.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-maglev-transport-network';

