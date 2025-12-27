--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-neo-vr-nightlife runOnChange:true

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
    '25d3ba66-5990-4bfd-a91f-0c024842cd9a',
    'canon-quest-tokyo-neo-vr-nightlife',
    'Токио 2020-2029 — VR-ночной клуб Нео-Токио',
    'Токио 2020-2029 — VR-ночной клуб Нео-Токио',
    'exploration',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    8,
    25,
    'active',
    '1.0.0',
    '{
  "quest_type": "exploration",
  "level_min": 8,
  "level_max": 25,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "enter_vr_club",
      "type": "location",
      "description": "Найти и войти в VR-ночной клуб Нео-Токио в районе Шибуя",
      "required": true
    },
    {
      "id": "navigate_virtual_world",
      "type": "interaction",
      "description": "Ориентироваться в виртуальном пространстве клуба",
      "required": true
    },
    {
      "id": "social_interactions",
      "type": "interaction",
      "description": "Взаимодействовать с аватарами других посетителей",
      "required": true
    },
    {
      "id": "uncover_mystery",
      "type": "investigation",
      "description": "Расследовать странные события в виртуальном мире",
      "required": true
    },
    {
      "id": "choose_reality",
      "type": "choice",
      "description": "Решить, остаться ли в виртуальном мире или вернуться в реальность",
      "required": true
    }
  ],
  "rewards": {
    "experience": 1800,
    "money": {
      "type": "eddies",
      "value": 2500
    },
    "reputation": {
      "digital": 10,
      "social": 5
    },
    "unlocks": {
      "achievements": [
        {
          "id": "vr_explorer",
          "name": "VR Исследователь"
        }
      ],
      "flags": [
        "neo_tokyo_vr_unlocked"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Кратко",
    "body": "Квест-id: `TOKYO-2029-011`\nФормат: исследование виртуальной реальности, сложность средняя, solo, длительность 1.5–3 часа.\nЛокация: VR-ночной клуб в Шибуя, Токио.\n",
    "mechanics_links": [
      "mechanics/vr/vr-exploration.yaml"
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
    "title": "Виртуальный клуб",
    "body": "1. Вход в VR-систему через специальный шлем в клубе.\n2. Навигация по постоянно меняющимся виртуальным ландшафтам.\n3. Взаимодействие с ИИ-аватарами и другими игроками.\n4. Расследование цифровых аномалий и скрытых истин.\n",
    "mechanics_links": [
      "mechanics/social/social-interactions.yaml"
    ]
  },
  {
    "id": "choices",
    "title": "Решения игрока",
    "body": "- Остаться в виртуальном раю и потерять связь с реальностью.\n- Вернуться в реальный мир, сохранив цифровые воспоминания.\n- Использовать находки для улучшения собственной VR-системы.\n",
    "mechanics_links": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "- 1800 XP, доход 2500 едди.\n- Бафф «Digital Insight» (+15% эффективность VR-взаимодействий на 24 часа).\n- Разблокировка линии «Cyber Tokyo Nights».\n",
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
    '66fa1a8517e1958baa829033186f14f8e052729473ea019764238ff457dbf1d6',
    'b99defde3acf21923531806bd44cba4e0dd55cd078774c2e2fdee4813a0721bf',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-011-neo-tokyo-vr-nightlife.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-neo-vr-nightlife';

