--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-oil-boom runOnChange:true

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
    'c55e2583-6851-4f39-8d74-7fb1cc3915f6',
    'canon-quest-baku-oil-boom',
    'Баку 2020-2029 — Нефтяной Бум',
    'Баку 2020-2029 — Нефтяной Бум',
    'faction',
    'Baku',
    '2020-2029',
    'easy',
    '30-60 минут',
    10,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "faction",
  "level_min": 10,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "pass_job_interview",
      "text": "Пройти собеседование в нефтяной компании"
    },
    {
      "id": "work_offshore_platform",
      "text": "Работать на морской платформе SOCAR"
    },
    {
      "id": "face_industry_risks",
      "text": "Столкнуться с рисками нефтяной индустрии"
    },
    {
      "id": "choose_career_path",
      "text": "Выбрать дальнейшую карьеру в нефтяной отрасли"
    }
  ],
  "rewards": {
    "experience": 5000,
    "money": {
      "min": 8000,
      "max": 12000
    },
    "items": [
      "oil_worker_skill",
      "industry_connections",
      "offshore_survivor_badge"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — Нефтяной Бум",
    "body": "Этот faction квест \"Баку 2020-2029 — Нефтяной Бум\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Пройти собеседование в нефтяной компании\n2. Работать на морской платформе SOCAR\n3. Столкнуться с рисками нефтяной индустрии\n4. Выбрать дальнейшую карьеру в нефтяной отрасли\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Чёрное золото Каспия требует инженерных навыков и устойчивости: игрок знакомится с наследием Нобелей и современными методами добычи.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Пройти отбор в SOCAR и получить доступ на морскую платформу.\n2. Выполнить цикл вахты: управление оборудованием, реагирование на шторм и пожарные тренировки.\n3. Принять решение: остаться инженером, перейти в управление или уйти из индустрии с репутацией инсайдера.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "5 000 XP, 10 000 едди (помесячно), +20 к навыку «Нефтедобыча» и репутация «Нефтяники» +10.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'd01de4954013d1c1e09fbaf4932bb5a61e2182981638223573ef903bc135b91a',
    '68b0e0e31e45fb2cd338c34a6042f2eea2580afe8edd02cbaaa9eac5c8b00fff',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-005-oil-boom.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-oil-boom';

