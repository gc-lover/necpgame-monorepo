--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-nazarbayev-legacy runOnChange:true

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
    'f2313d51-35e9-42e0-a3ed-a406f09c707c',
    'canon-quest-astana-nazarbayev-legacy',
    'Астана 2020-2029 — «Наследие Назарбаева»',
    'Астана 2020-2029 — «Наследие Назарбаева»',
    'main',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    10,
    25,
    'active',
    '1.0.0',
    '{
  "quest_type": "main",
  "level_min": 10,
  "level_max": 25,
  "requirements": [
    {
      "type": "location",
      "value": "astana"
    },
    {
      "type": "quest_completed",
      "value": "canon-quest-astana-capital-move"
    }
  ],
  "objectives": [
    {
      "id": "study_archives",
      "type": "explore",
      "description": "Изучить архивы библиотеки Елбасы",
      "required": true
    },
    {
      "id": "meet_critics",
      "type": "interact",
      "description": "Встретиться с активистами и журналистами",
      "required": true
    },
    {
      "id": "create_report",
      "type": "complete",
      "description": "Сформировать итоговый доклад о наследии Назарбаева",
      "required": true
    }
  ],
  "rewards": {
    "experience": 4000,
    "money": {
      "type": "eddies",
      "value": 0
    },
    "reputation": {
      "politics": 15
    },
    "unlocks": {
      "achievements": [
        {
          "id": "history_expert",
          "name": "Знаток истории"
        }
      ],
      "flags": [
        "astana_political_scenarios_unlocked"
      ]
    }
  },
  "branches": [
    {
      "id": "continue_course",
      "name": "Продолжение курса",
      "description": "Поддержать политику Назарбаева"
    },
    {
      "id": "cautious_modernization",
      "name": "Осторожная модернизация",
      "description": "Балансировать между традицией и реформами"
    },
    {
      "id": "radical_reforms",
      "name": "Радикальные реформы",
      "description": "Выступить за кардинальные изменения"
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — «Наследие Назарбаева»",
    "body": "Этот main квест \"Астана 2020-2029 — «Наследие Назарбаева»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
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
    "id": "archives",
    "title": "Архивы и достижения",
    "body": "Игрок посещает библиотеку Елбасы, изучает документы об атомном разоружении, переносе столицы и экономических реформах.\nПоявляются интерактивные экспозиции, сравнивающие Казахстан 1991 и 2029 годов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "dissent",
    "title": "Голоса критики",
    "body": "Встречи с активистами, журналистами и академиками раскрывают вопросы авторитаризма, культа личности и коррупционных скандалов.\nИгрок собирает материалы для доклада, балансируя репутацию между государством и оппозицией.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "judgement",
    "title": "Итоговая оценка",
    "body": "Финальная сцена проходит в аналитическом центре. Игрок формирует доклад, выбирая один из трёх нарративов (продолжение курса,\nосторожная модернизация, радикальные реформы). Выбор влияет на дипломатические проверки и открывает ветви кампании.\nНаграды: 4 000 XP, ачивка «Знаток истории» и доступ к политическим сценариям Астаны.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '308a07070a920980fef8d635c912721306345d0d869a69571834db42edb57a1e',
    '7bdcd2f255c2fb79fec1ef8d7d402997a6e49358883519c47118ccec619b11cf',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-009-nazarbayev-legacy.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-nazarbayev-legacy';

