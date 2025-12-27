--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-yanar-dag runOnChange:true

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
    '1158edfa-38e8-4ac9-bdcb-c7722727fc10',
    'canon-quest-baku-yanar-dag',
    'Баку — Янар Даг (Горящая гора)',
    'Баку — Янар Даг (Горящая гора)',
    'side',
    'Baku',
    '2020-2029',
    'easy',
    '30-60 минут',
    3,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 3,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "reach_yanar_dag",
      "text": "Добраться до горы Янар Даг"
    },
    {
      "id": "observe_eternal_flame",
      "text": "Наблюдать вечный огонь и понять его значение"
    },
    {
      "id": "learn_zoroastrian_history",
      "text": "Изучить историю зороастризма и огнепоклонников"
    },
    {
      "id": "experience_spiritual_atmosphere",
      "text": "Почувствовать духовную атмосферу святилища"
    }
  ],
  "rewards": {
    "experience": 350,
    "money": {
      "min": 50,
      "max": 150
    },
    "items": [
      "spiritual_enlightenment",
      "fire_worshiper_achievement",
      "zoroastrian_knowledge"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку — Янар Даг (Горящая гора)",
    "body": "Этот side квест \"Баку — Янар Даг (Горящая гора)\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_profile",
    "title": "Параметры квеста",
    "body": "Тип: side. Сложность: medium. Формат: solo. Длительность: 1–2 часа.\nНаграды: 1,500 XP, 0 едди, +10 природа. Ачивка «Огнепоклонник».\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы",
    "body": "1. Добраться до Янар Даг (25 км от Баку).\n2. Изучить склоны, где из земли выходит природный газ и горит вечный огонь.\n3. Узнать о поклонении зороастрийцев и истории святилища.\n4. Провести время у огня ночью и зафиксировать атмосферу.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Янар Даг символизирует «Землю огня». Природный газ, выходящий из почвы, горит столетиями. Сайт связан с древними религиями и легендами.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "systems_hooks",
    "title": "Системные крючки",
    "body": "exploration-system (путешествие), mood-system (эффект «Мистический огонь»), религиозная ветка о зороастризме.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "1,500 XP, 0 едди, +10 очков природы, ачивка «Огнепоклонник». Разблокирует дополнительные задания по религиозным местам Азербайджана.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'ee70b94a5d743e82a1ac57c7e329df0d8ab1e7b0a9ab19a042aeb160ed77fe64',
    '6e841bd03202f99bd30608701dd7950f0f672427eaef843cb40c70596eebd8ba',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-003-yanar-dag.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-yanar-dag';

