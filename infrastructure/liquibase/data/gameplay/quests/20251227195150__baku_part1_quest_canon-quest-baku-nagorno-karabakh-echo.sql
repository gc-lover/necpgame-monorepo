--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-nagorno-karabakh-echo runOnChange:true

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
    'fe4ae015-2c7d-4406-8138-c79ed8982631',
    'canon-quest-baku-nagorno-karabakh-echo',
    'Баку 2020-2029 — «Эхо Нагорного Карабаха»',
    'Баку 2020-2029 — «Эхо Нагорного Карабаха»',
    'main',
    'Baku',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "main",
  "level_min": 5,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "meet_refugees",
      "text": "Встретиться с семьями, перемещёнными из Карабаха",
      "type": "interact",
      "target": "karabakh_refugees_meeting",
      "count": 1,
      "optional": false
    },
    {
      "id": "visit_memorials",
      "text": "Посетить Аллею шехидов и другие памятники",
      "type": "interact",
      "target": "martyrs_alley_visit",
      "count": 1,
      "optional": false
    },
    {
      "id": "study_war_history",
      "text": "Изучить хронику войны 2020 года с историками",
      "type": "interact",
      "target": "war_2020_study",
      "count": 1,
      "optional": false
    },
    {
      "id": "make_choice",
      "text": "Выбрать позицию: помощь беженцам, исследование истории или посредничество",
      "type": "choice",
      "target": "karabakh_position_choice",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 5000,
    "money": 0,
    "reputation": {
      "diplomacy": 20
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Свидетель истории"
      ],
      "flags": [
        "karabakh_echo_witnessed"
      ]
    }
  },
  "branches": [
    {
      "condition": "Помочь беженцам",
      "outcome": "Бонус к репутации среди гражданских фракций Азербайджана",
      "next_quests": []
    },
    {
      "condition": "Исследовать историю",
      "outcome": "Открытие дипломатических веток",
      "next_quests": []
    },
    {
      "condition": "Понять обе стороны",
      "outcome": "Сложный дипломатический бонус",
      "next_quests": []
    }
  ]
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — «Эхо Нагорного Карабаха»",
    "body": "Этот main квест \"Баку 2020-2029 — «Эхо Нагорного Карабаха»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Встретиться с семьями, перемещёнными из Карабаха\n2. Посетить Аллею шехидов и другие памятники\n3. Изучить хронику войны 2020 года с историками\n4. Выбрать позицию: помощь беженцам, исследование истории или посредничество\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "testimonies",
    "title": "Свидетельства беженцев",
    "body": "Игрок встречается с семьями, перемещёнными из Карабаха. Диалоги раскрывают утрату дома, жизнь в временных поселениях\nи надежды на возвращение. Записи фиксируются в журнале для дальнейшего анализа.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "memorial",
    "title": "Мемориалы и память",
    "body": "Посещение Аллеи шехидов и других памятников. Игрок взаимодействует с историками, изучает хронику войны 2020 года\nи собирает материалы о погибших, усиливая эмпатию и знание timeline.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "choices",
    "title": "Выбор позиции",
    "body": "- **Помочь беженцам:** организовать гуманитарную поддержку и получить бонус к репутации среди гражданских фракций Азербайджана.\n- **Исследовать историю:** собрать архивы, подготовить доклад и открыть дипломатические ветки.\n- **Понять обе стороны:** связаться с посредниками, снизить напряжение и получить сложный, но полезный дипломатический бонус.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "- 5 000 XP, усиленная эмпатия и ачивка «Свидетель истории».\n- Выбранная позиция влияет на дипломатические проверки, доступ к миссиям в регионе и отношение армянских фракций.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '0f2b3f59b3ee542118a27d20ddb203129c30c81dae533774d6e39036fd483cba',
    'bea9254ca7c7a44e9c80dc9b0aa153659fda328d3006a956e273854596bc566d',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-010-nagorno-karabakh-echo.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-nagorno-karabakh-echo';

