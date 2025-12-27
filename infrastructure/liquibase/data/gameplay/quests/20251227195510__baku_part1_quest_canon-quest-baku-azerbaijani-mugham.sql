--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-azerbaijani-mugham runOnChange:true

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
    'ff0956c5-afc3-44de-9fde-db455f570ffc',
    'canon-quest-baku-azerbaijani-mugham',
    'Баку 2020-2029 — «Азербайджанский мугам»',
    'Баку 2020-2029 — «Азербайджанский мугам»',
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
      "id": "visit_mugham_center",
      "text": "Посетить Международный центр мугама и получить приглашение на концерт",
      "type": "interact",
      "target": "mugham_center_visit",
      "count": 1,
      "optional": false
    },
    {
      "id": "learn_mugham_history",
      "text": "Выслушать рассказ куратора об истории жанра и включении в список ЮНЕСКО",
      "type": "interact",
      "target": "mugham_history_lesson",
      "count": 1,
      "optional": false
    },
    {
      "id": "observe_instruments",
      "text": "Наблюдать за взаимодействием инструментов: тар, кеманча и дэф",
      "type": "interact",
      "target": "mugham_instruments_observation",
      "count": 1,
      "optional": false
    },
    {
      "id": "record_improvisation",
      "text": "Записать импровизацию вокалиста для последующих проектов",
      "type": "interact",
      "target": "mugham_recording",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 1000,
    "money": -30,
    "reputation": {
      "culture": 15
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Ценитель мугама"
      ],
      "flags": [
        "mugham_concert_attended"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — «Азербайджанский мугам»",
    "body": "Этот side квест \"Баку 2020-2029 — «Азербайджанский мугам»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Посетить Международный центр мугама и получить приглашение на концерт\n2. Выслушать рассказ куратора об истории жанра и включении в список ЮНЕСКО\n3. Наблюдать за взаимодействием инструментов: тар, кеманча и дэф\n4. Записать импровизацию вокалиста для последующих проектов\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "introduction",
    "title": "Знакомство с мугамом",
    "body": "Игрок получает приглашение в Международный центр мугама. Перед концертом куратор рассказывает историю жанра, роль\nмастеров и причины включения в список ЮНЕСКО.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "performance",
    "title": "Концерт и инструменты",
    "body": "Во время выступления игрок наблюдает за взаимодействием исполнителей: тар задаёт мелодию, кеманча отвечает, дэф держит ритм.\nВокалист ведёт импровизацию, а игрок может записывать мотивы для последующих проектов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и влияние",
    "body": "- 1 000 XP, -30 едди (стоимость билета) и +15 к параметру «Культура».\n- Ачивка «Ценитель мугама» и доступ к музыкальным событиям Баку.\n- Запись концерта, дающая бонус к эмоциональным сценам в будущих квестах.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '19d6142522071790001c0a6d0b9367d770511334647f0da13f4bfb81660a2ad5',
    '2894fc861dbd3a09a0ccc699736ca5b7b9169e320cf53201454e0694ecba2334',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-007-azerbaijani-mugham.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-azerbaijani-mugham';

