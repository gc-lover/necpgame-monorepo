--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-old-city runOnChange:true

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
    'da8e20af-4b9d-423b-a191-2167b857a150',
    'canon-quest-baku-old-city',
    'Баку — Старый город Ичери Шехер',
    'Баку — Старый город Ичери Шехер',
    'side',
    'Baku',
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
      "id": "enter_old_city",
      "text": "Войти в Старый город через главные ворота"
    },
    {
      "id": "visit_maiden_tower",
      "text": "Посетить Девичью башню и узнать ее легенды"
    },
    {
      "id": "explore_narrow_streets",
      "text": "Исследовать узкие улочки и исторические здания"
    },
    {
      "id": "find_hidden_artifacts",
      "text": "Найти спрятанные артефакты прошлого"
    }
  ],
  "rewards": {
    "experience": 400,
    "money": {
      "min": 100,
      "max": 250
    },
    "items": [
      "history_knowledge",
      "cultural_insight",
      "old_city_explorer_badge"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку — Старый город Ичери Шехер",
    "body": "Этот side квест \"Баку — Старый город Ичери Шехер\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_profile",
    "title": "Параметры квеста",
    "body": "Тип: side. Сложность: easy. Формат: solo. Длительность: 2–4 часа.\nНаграды: 1,500 XP, -10 едди, +15 к культуре. Ачивка «Хранитель Ичери Шехер».\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы",
    "body": "1. Войти через крепостные ворота Старого города.\n2. Исследовать лабиринт узких улочек и рынков.\n3. Подняться на Девичью башню и прослушать легенду.\n4. Посетить дворец Ширваншахов и изучить экспонаты.\n5. Завершить прогулку с контрастом между древним городом и современным Баку.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Ичери Шехер включён в список наследия UNESCO. Девичья башня и стены XII века создают атмосферу Средневековья, а песчаник и узкие улочки подчёркивают особую эстетику.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "systems_hooks",
    "title": "Системные крючки",
    "body": "exploration-system (поиск точек интереса), lore-codex (легенды и исторические записи), эмоциональные сцены для фотомода.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 1,500 XP, -10 едди (билеты и сувениры), +15 очков культуры и ачивку «Хранитель Ичери Шехер».\nОткрываются дополнительные миссии об истории Баку.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'd7c168d1d2407efb7045c440660c27c83424b0216e21336b6f468398144bffc5',
    '3bf3011d9fd879c77a2112e4090c8ce67a237d2b34ceb8634ec1af32b20a987c',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-002-old-city-icheri-sheher.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-old-city';

