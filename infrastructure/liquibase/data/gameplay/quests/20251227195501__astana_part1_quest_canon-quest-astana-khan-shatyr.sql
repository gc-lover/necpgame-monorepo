--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-khan-shatyr runOnChange:true

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
    '20a1e0f4-cef0-4ae8-b444-08c5e49e3b27',
    'canon-quest-astana-khan-shatyr',
    'Астана 2020-2029 — Хан Шатыр',
    'Астана 2020-2029 — Хан Шатыр',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    1,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
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
      "id": "enter_tent",
      "text": "Войти в шатёр Хан Шатыр и исследовать торговые пространства",
      "type": "interact",
      "target": "khan_shatyr_entrance",
      "count": 1,
      "optional": false
    },
    {
      "id": "visit_tropical_park",
      "text": "Посетить тропический парк, пляж и аттракционы",
      "type": "interact",
      "target": "tropical_park_visit",
      "count": 1,
      "optional": false
    },
    {
      "id": "go_upper_level",
      "text": "Подняться на верхний уровень, оценить вид и завершить шопинг",
      "type": "interact",
      "target": "khan_shatyr_upper_level",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 600,
    "money": -50,
    "reputation": {
      "shopping": 5
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Хозяин Шатра"
      ],
      "flags": [
        "khan_shatyr_visited"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — Хан Шатыр",
    "body": "Этот side квест \"Астана 2020-2029 — Хан Шатыр\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Войти в шатёр Хан Шатыр и исследовать торговые пространства\n2. Посетить тропический парк, пляж и аттракционы\n3. Подняться на верхний уровень, оценить вид и завершить шопинг\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Гигантский прозрачный шатёр Хан Шатыр удерживает тропический климат даже при -40°С; внутри парк, магазины и пляж.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Войти в шатёр и исследовать торговые пространства.\n2. Посетить тропический парк, пляж и аттракционы.\n3. Подняться на верхний уровень, оценить вид и завершить шопинг.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "600 XP, -50 едди, +5 к параметру «Шопинг» и достижение «Хозяин Шатра».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '9a974ac6ab5563ee31337dcdf258d4ea3623290e6936d7dc9fde0d025110b346',
    '37ce3db2baa8230d2939c137acfd2926e4de6ee79123304dd411ad97ab92becc',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-002-khan-shatyr.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-khan-shatyr';

