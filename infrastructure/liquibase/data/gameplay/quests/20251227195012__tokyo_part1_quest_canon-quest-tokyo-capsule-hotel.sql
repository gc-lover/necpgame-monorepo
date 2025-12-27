--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-capsule-hotel runOnChange:true

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
    'fda02398-4c58-4787-8d8e-999e7736672f',
    'canon-quest-tokyo-capsule-hotel',
    'Токио 2020-2029 — Отель-Капсула',
    'Токио 2020-2029 — Отель-Капсула',
    'side',
    'Tokyo',
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
      "id": "main_objective",
      "text": "Выполнить основную цель квеста: Перевести опыт проживания в капсуле в структурированный YAML и подчеркнуть UX-эффекты для экономного..."
    }
  ],
  "rewards": {
    "experience": 300,
    "money": {
      "min": 50,
      "max": 200
    },
    "items": []
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Отель-Капсула",
    "body": "Этот side квест \"Токио 2020-2029 — Отель-Капсула\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Перевести опыт проживания в капсуле в структурированный YAML и подчеркнуть UX-эффекты для экономного...\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Капсульные отели Токио обслуживают салариманов и туристов, предлагая минималистичные капсулы с встроенными интерфейсами и общими зонами.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Забронировать капсулу, следуя правилам заведения и оплатив 500 едди.\n2. Пройти японский ритуал заселения: снять обувь, воспользоваться шкафчиками и принять душ.\n3. Устроиться в капсуле, активировать интерфейсы и настроить шумоподавление.\n4. Провести время с соседями, обменявшись историями и бонусами к репутации.\n5. Проснуться под звуки железной дороги и завершить отдых в общественной бане.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Отмечены узкие коридоры, тихие капсулы с индивидуальными панелями, запах кофе из общих автоматов и приглушённый гул поезда за окнами.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок тратит 500 едди, получает 300 XP и полностью восстанавливает здоровье и выносливость. Социальные реплики повышают доверие салариманов.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '957b53a567e2d9e67ccad837b4b35ec80e3a703aaad4f52f4762b5f17da8d58f',
    'bc56291a53ad1606812b89ce44981e3599649e50c42ae76fef64e310391cfdd8',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-006-capsule-hotel.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-capsule-hotel';

