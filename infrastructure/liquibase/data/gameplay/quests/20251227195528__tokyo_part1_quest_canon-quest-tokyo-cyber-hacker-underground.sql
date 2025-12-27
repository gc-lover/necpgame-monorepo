--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-cyber-hacker-underground runOnChange:true

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
    '711009de-cd6b-4c0b-b79d-0a5a62d712bd',
    'canon-quest-tokyo-cyber-hacker-underground',
    'Токио 2020-2029 — Кибер-хакерское подполье',
    'Токио 2020-2029 — Кибер-хакерское подполье',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    12,
    30,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 12,
  "level_max": 30,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "infiltrate_hacker_den",
      "type": "location",
      "description": "Найти и проникнуть в убежище хакерской группировки в подземельях Токио",
      "required": true
    },
    {
      "id": "prove_skills",
      "type": "challenge",
      "description": "Продемонстрировать хакерские навыки в тестовом задании",
      "required": true
    },
    {
      "id": "corporate_hack",
      "type": "hacking",
      "description": "Взломать корпоративную систему для получения данных",
      "required": true
    },
    {
      "id": "moral_choice",
      "type": "choice",
      "description": "Выбрать, использовать ли данные для личной выгоды или общественного блага",
      "required": true
    },
    {
      "id": "escape_pursuit",
      "type": "combat",
      "description": "Сбежать от корпоративной охраны после взлома",
      "required": true
    }
  ],
  "rewards": {
    "experience": 2800,
    "money": {
      "type": "eddies",
      "value": 4500
    },
    "reputation": {
      "hacker": 20,
      "corporate": -10
    },
    "unlocks": {
      "achievements": [
        {
          "id": "shadow_runner",
          "name": "Shadow Runner"
        }
      ],
      "flags": [
        "tokyo_hacker_network_unlocked"
      ],
      "items": [
        {
          "id": "hacking_deck",
          "name": "Базовый хакерский deck"
        }
      ]
    }
  },
  "branches": [
    {
      "id": "corporate_spy",
      "name": "Корпоративный шпион",
      "description": "Работать на корпорации против хакеров"
    },
    {
      "id": "freedom_fighter",
      "name": "Борец за свободу",
      "description": "Использовать навыки для борьбы с корпорациями"
    },
    {
      "id": "neutral_operator",
      "name": "Нейтральный оператор",
      "description": "Работать только за деньги, без идеологии"
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Кратко",
    "body": "Квест-id: `TOKYO-2029-012`\nФормат: хакерское приключение, сложность высокая, solo/co-op, длительность 2–4 часа.\nЛокация: подземное хакерское убежище и виртуальные сети Токио.\n",
    "mechanics_links": [
      "mechanics/hacking/hacking-mechanics.yaml"
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
    "title": "Хакерское подполье",
    "body": "1. Проникновение в скрытое убежище хакеров в канализации Токио.\n2. Демонстрация навыков в виртуальных тренировочных симуляциях.\n3. Взлом корпоративных систем с риском обнаружения.\n4. Этический выбор между прибылью и принципами.\n5. Побег от преследования корпоративной безопасности.\n",
    "mechanics_links": [
      "mechanics/combat/combat-mechanics.yaml"
    ]
  },
  {
    "id": "choices",
    "title": "Решения игрока",
    "body": "- Стать корпоративным шпионом и предать хакерское сообщество.\n- Борьба за цифровую свободу против корпораций.\n- Нейтральная позиция - работа только за оплату.\n- Использовать полученные данные для шантажа.\n",
    "mechanics_links": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "- 2800 XP, доход 4500 едди.\n- Бафф «Hacker's Edge» (+25% эффективность взлома на 36 часов).\n- Доступ к хакерской сети Токио и специальным контрактам.\n- Возможное снижение репутации с корпорациями.\n",
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
    '4494bbf1352b01cb6452977bf5679a926632ade87ed29cfcab7ce43d76980813',
    'd7daf930acef593285de0ef552aac90964fce2189238c2bbd90efc70bae5cbc1',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-012-cyber-hacker-underground.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-cyber-hacker-underground';

