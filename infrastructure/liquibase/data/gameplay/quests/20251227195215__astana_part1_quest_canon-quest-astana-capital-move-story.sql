--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-capital-move-story runOnChange:true

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
    '82ffe150-1606-4e6d-885d-34fe263755b8',
    'canon-quest-astana-capital-move-story',
    'Астана 2020-2029 — История переноса столицы',
    'Астана 2020-2029 — История переноса столицы',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    2,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 2,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "visit_museum",
      "text": "Посетить музей истории переноса столицы",
      "type": "interact",
      "target": "capital_move_museum_visit",
      "count": 1,
      "optional": false
    },
    {
      "id": "meet_builders",
      "text": "Встретиться со строителями и выслушать их истории",
      "type": "interact",
      "target": "builders_meeting",
      "count": 1,
      "optional": false
    },
    {
      "id": "study_archives",
      "text": "Изучить архивы и документы о переносе столицы",
      "type": "interact",
      "target": "capital_move_archives",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 1200,
    "money": 0,
    "reputation": {
      "history": 10
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Свидетель Трансформации"
      ],
      "flags": [
        "capital_move_story_learned"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — История переноса столицы",
    "body": "Этот side квест \"Астана 2020-2029 — История переноса столицы\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Посетить музей истории переноса столицы\n2. Встретиться со строителями и выслушать их истории\n3. Изучить архивы и документы о переносе столицы\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Исторический квест о переносе столицы Казахстана из Алматы в Астану в 1997 году.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Посетить музей истории переноса столицы.\n2. Встретиться со строителями и выслушать их истории.\n3. Изучить архивы и документы о переносе столицы.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "1 200 XP, +10 к параметру «История» и достижение «Свидетель Трансформации».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '89bf0bb196ad899eccbc2fcb5d2a793ec68640f1d067574acd7219e5bd4534d4',
    '0a03725eefb58341c99230140d743e3cbf573e47ae19fdb6c637c8765d516173',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-004-capital-move-story.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-capital-move-story';

