--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-katana-smith runOnChange:true

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
    'e0c38078-9beb-4c27-9867-c0eef873031f',
    'canon-quest-tokyo-katana-smith',
    'Токио 2020-2029 — Мастер Катаны',
    'Токио 2020-2029 — Мастер Катаны',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    15,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 15,
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
      "text": "Выполнить основную цель квеста: Структурировать квест в YAML, описав дуэльное доказательство достоинства, цикл производства и церемо..."
    }
  ],
  "rewards": {
    "experience": 800,
    "money": {
      "min": 500,
      "max": 2000
    },
    "items": []
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Мастер Катаны",
    "body": "Этот side квест \"Токио 2020-2029 — Мастер Катаны\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Структурировать квест в YAML, описав дуэльное доказательство достоинства, цикл производства и церемо...\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Последний мастер катан использует тамахаганэ и кибернетические печи, сохраняя душу ремесла и обеспечивая заказчиков персонализированными клинками.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Найти кузницу в горах за пределами Токио, расшифровав подсказки коллекционеров оружия.\n2. Пройти испытание на честь: дуэль на деревянных мечах и беседа о кодексе бусидо.\n3. Выбрать параметры клинка, оплатить 50 000 едди и решить, ускорять ли производство редкими материалами.\n4. Дождаться завершения ковки (30 игровых дней или ускорение) и вернуться на церемонию вручения.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 2 000 XP, легендарную катану с +20% к урону и кастомным внешним видом, а также рост репутации среди самурайских кланов.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'f6895d9a08f594e9c23ba7da396a286f3cd9a3da46b2e1449ca7d2bacfb2a3b9',
    'f426520c3d4794b7eb82f1715e3dcb444b2194be59f9f1cca9ce64286f07f2dc',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-009-katana-smith.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-katana-smith';

