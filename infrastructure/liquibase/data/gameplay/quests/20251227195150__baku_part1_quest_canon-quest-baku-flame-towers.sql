--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-flame-towers runOnChange:true

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
    '00072f23-e999-4f80-a9a6-5fa153b56f5c',
    'canon-quest-baku-flame-towers',
    'Баку — Пламенные башни',
    'Баку — Пламенные башни',
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
  "level_max": null
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку — Пламенные башни",
    "body": "Этот side квест \"Баку — Пламенные башни\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_profile",
    "title": "Параметры квеста",
    "body": "Тип: social. Сложность: easy. Формат: solo. Длительность: 1–2 часа.\nНаграды: 800 XP, -20 едди (расходы), +10 эстетики. Ачивка «Хранитель Пламени».\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы",
    "body": "1. Прибыть к Flame Towers днём и изучить архитектуру.\n2. Дождаться вечернего LED-шоу на фасадах башен.\n3. Подняться на смотровую площадку ночью и получить панораму Баку и Каспия.\n4. Зафиксировать контраст с историческим центром внизу.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Башни высотой 190 м символизируют «Землю огня». Ночные LED-анимации усиливают атмосферу неонового Баку.\nУпомянуть соседство со Старым городом и нефтяным наследием региона.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "systems_hooks",
    "title": "Системные крючки",
    "body": "Эксплоринговые задачи для exploration-system, настроение «Вдохновение» в mood-system, архитектурная линия Баку.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 800 XP, -20 едди (поездка и билеты), +10 эстетики, ачивку «Хранитель Пламени».\nРазблокируется доступ к квестам о современной архитектуре Баку.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'b31bd4eb5f045e53800b633da0870752dc1fa0e4150b91f8c2cf3a8325e00735',
    '30b6ef117de8ccfc3a6efc93066aba3c6b3a1b9ab4e38ab5ebe825e3296220eb',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-001-flame-towers.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-flame-towers';

