--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-akihabara-otaku runOnChange:true

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
    '3c23c512-581d-471c-850b-efa405caf773',
    'canon-quest-tokyo-akihabara-otaku',
    'Токио 2020-2029 — Отаку Акихабары',
    'Токио 2020-2029 — Отаку Акихабары',
    'social',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    1,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "social",
  "level_min": 1,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "explore_akihabara",
      "text": "Исследовать район Акихабара и его магазины"
    },
    {
      "id": "visit_maid_cafe",
      "text": "Посетить мейд-кафе и пообщаться с персоналом"
    },
    {
      "id": "play_gacha_machines",
      "text": "Поиграть в гача-автоматы и собрать коллекцию"
    },
    {
      "id": "network_with_otaku",
      "text": "Завести знакомства в отаку-сообществе"
    }
  ],
  "rewards": {
    "experience": 600,
    "money": {
      "min": 200,
      "max": 800
    },
    "items": [
      "otaku_reputation",
      "random_figure_collection"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Отаку Акихабары",
    "body": "Этот social квест \"Токио 2020-2029 — Отаку Акихабары\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Исследовать район Акихабара и его магазины\n2. Посетить мейд-кафе и пообщаться с персоналом\n3. Поиграть в гача-автоматы и собрать коллекцию\n4. Завести знакомства в отаку-сообществе\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Акихабара — квартал электроники и кибер-отаку культуры, где витрины хранят ностальгию, а AR-вывески уводят в альтернативные аниме-реальности.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Прогуляться по торговым улицам, исследуя магазины электроники и аниме-магазины.\n2. Посетить мейд-кафе, выполнить сюжетный ролевой сценарий и получить тематический напиток.\n3. Активировать гача-автомат, определить редкость фигурки и зафиксировать трофей в коллекции.\n4. Приобрести лимитированную мангу и пообщаться с сообществом отаку.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "В районе звучит 8-битная музыка аркад, AR-анимешные персонажи приветствуют гостей, на улицах встречаются косплееры и любители ретро-комплектующих.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 600 XP, тратит до 2 000 едди, извлекает случайную фигурку (Common-Legendary) и +5 к репутации отаку-сообщества, открывая тематические ивенты.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'e13261a7e4e6835dce040a7248b5b70447736063c3b7fa0f8c0a4d1e7012e949',
    '02f098bc2ba6dc08f8269a5c285cd7f6bb0a817b1efe918dfcf40af93d3e270c',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-003-akihabara-otaku.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-akihabara-otaku';

