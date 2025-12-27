--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-baiterek-tower runOnChange:true

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
    '13344c9a-7589-497b-bc2e-f57df95486ee',
    'canon-quest-astana-baiterek-tower',
    'Астана 2020-2029 — Башня Байтерек',
    'Астана 2020-2029 — Башня Байтерек',
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
      "id": "approach_tower",
      "text": "Подойти к башне Байтерек и изучить архитектуру и историю",
      "type": "interact",
      "target": "baiterek_tower_approach",
      "count": 1,
      "optional": false
    },
    {
      "id": "ride_elevator",
      "text": "Подняться на лифте на смотровую площадку",
      "type": "interact",
      "target": "baiterek_elevator_ride",
      "count": 1,
      "optional": false
    },
    {
      "id": "touch_golden_handprint",
      "text": "Приложить ладонь к золотому отпечатку",
      "type": "interact",
      "target": "golden_handprint_touch",
      "count": 1,
      "optional": false
    },
    {
      "id": "take_panorama_photos",
      "text": "Сделать фотографии панорамы новой столицы",
      "type": "interact",
      "target": "astana_panorama_photos",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 800,
    "money": -10,
    "reputation": {
      "aesthetics": 10
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "На Вершине Байтерека"
      ],
      "flags": [
        "baiterek_tower_visited"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — Башня Байтерек",
    "body": "Этот side квест \"Астана 2020-2029 — Башня Байтерек\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Подойти к башне Байтерек и изучить архитектуру и историю\n2. Подняться на лифте на смотровую площадку\n3. Приложить ладонь к золотому отпечатку\n4. Сделать фотографии панорамы новой столицы\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Футуристическая башня Байтерек поднимается над степью, символизируя перенос столицы и мечту Самрук.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Подойти к башне, изучить архитектуру и историю.\n2. Подняться на лифте на смотровую площадку.\n3. Приложить ладонь к золотому отпечатку и сделать фотографии панорамы.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "800 XP, -10 едди, +10 к эстетике и достижение «На Вершине Байтерека».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '4fe906e10beeb63590782a7cde97b8aa7f3529b5a0cc437f2afeb58277b5d95b',
    'c2d07d9eba189ed192328198a7b1dd8995553a567258ff8e1bd90132bd09cc14',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-001-baiterek-tower.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-baiterek-tower';

