--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-kazakh-language runOnChange:true

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
    '81b64e16-eead-4632-850f-e0e9b44f648f',
    'canon-quest-astana-kazakh-language',
    'Астана 2020-2029 — «Казахский язык»',
    'Астана 2020-2029 — «Казахский язык»',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    3,
    15,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 3,
  "level_max": 15,
  "requirements": [
    {
      "type": "location",
      "value": "astana"
    }
  ],
  "objectives": [
    {
      "id": "complete_course",
      "type": "complete",
      "description": "Пройти интенсивный курс казахского языка",
      "required": true
    },
    {
      "id": "practice_dialogue",
      "type": "interact",
      "description": "Практиковать диалоги с носителями языка",
      "required": true
    },
    {
      "id": "pass_exam",
      "type": "complete",
      "description": "Сдать экзамен по казахскому языку",
      "required": true
    }
  ],
  "rewards": {
    "experience": 2500,
    "money": {
      "type": "eddies",
      "value": 0
    },
    "reputation": {
      "education": 20
    },
    "unlocks": {
      "achievements": [
        {
          "id": "kazakh_speaker",
          "name": "Қазақ"
        }
      ],
      "flags": [
        "kazakh_language_unlocked",
        "educational_factions_friendly"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — «Казахский язык»",
    "body": "Этот side квест \"Астана 2020-2029 — «Казахский язык»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
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
    "id": "lesson_plan",
    "title": "Учебный план",
    "body": "Курс делится на модули: фонетика, базовые фразы, кириллица и переход на латиницу. Преподаватель объясняет реформу\n2025 года и роль языка в восстановлении национальной идентичности.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "practice",
    "title": "Практика и культура",
    "body": "Игрок выполняет упражнения с носителями: ведёт диалоги на рынках, читает поэзию Абая, участвует в радиопередаче.\nВ каждом задании отслеживается правильность произношения и словарный запас.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "certification",
    "title": "Экзамен и награды",
    "body": "Финальное испытание проходит в культурном центре: устный экзамен, перевод, написание на латинице. Успех приносит\n2 500 XP, +20 к навыку «Казахский язык», титул «Қазақ» и дружбу с образовательными фракциями.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'b07864b87dc504bf45c28cadd9e53214e8319316f12f761979753d61a73bdae8',
    'be45e597d6c02c4b3623bf6444129dcb19228f0709770c368d65fab5fcfbcbf5',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-008-kazakh-language.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-kazakh-language';

