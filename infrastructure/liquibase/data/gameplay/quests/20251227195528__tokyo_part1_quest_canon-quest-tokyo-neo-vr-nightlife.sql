--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-neo-vr-nightlife runOnChange:true

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
    'de640df8-00fb-4a54-89c6-a3a9a0e9f430',
    'canon-quest-tokyo-neo-vr-nightlife',
    'Токио 2020-2029 — VR Ночная Жизнь Нео-Токио',
    'Токио 2020-2029 — VR Ночная Жизнь Нео-Токио',
    'social',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    40,
    'active',
    '1.0.0',
    '{
  "quest_type": "social",
  "level_min": 5,
  "level_max": 40,
  "requirements": {
    "completed_quests": [],
    "flags": []
  },
  "objectives": [
    {
      "id": "enter_vr_club",
      "type": "location",
      "description": "Войти в VR клуб в районе Сибуя",
      "required": true
    },
    {
      "id": "social_interaction",
      "type": "interaction",
      "description": "Вступить в разговор с 3 NPC в виртуальном клубе",
      "required": true
    },
    {
      "id": "dance_performance",
      "type": "skill",
      "description": "Выполнить танцевальное выступление в VR",
      "required": false
    },
    {
      "id": "network_expansion",
      "type": "achievement",
      "description": "Добавить 5 новых контактов в социальную сеть",
      "required": true
    }
  ],
  "rewards": {
    "experience": 1200,
    "money": {
      "type": "eddies",
      "value": -15000
    },
    "reputation": {
      "social": 20,
      "style": 10
    },
    "unlocks": {
      "achievements": [
        {
          "id": "vr_nightlife_explorer",
          "name": "Исследователь VR Ночи"
        }
      ],
      "flags": []
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — VR Ночная Жизнь Нео-Токио",
    "body": "Этот social квест \"Токио 2020-2029 — VR Ночная Жизнь Нео-Токио\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Objective 1\n2. Objective 2\n3. Objective 3\n4. Objective 4\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Квест демонстрирует виртуальную ночную жизнь Нео-Токио как социальный хаб, объединяя VR технологии с традиционными развлечениями.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы погружения",
    "body": "1. Войти в VR клуб в районе Сибуя и адаптироваться к виртуальной реальности.\n2. Вступить в социальные взаимодействия с NPC и другими игроками.\n3. Участвовать в виртуальных развлечениях (танцы, игры, концерты).\n4. Расширить свою социальную сеть новыми контактами.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Киберпанк атмосфера",
    "body": "Подсвечены неоновые VR клубы, цифровые вечеринки, голографические шоу и социальные взаимодействия в виртуальном пространстве.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и механики",
    "body": "Игрок получает 1200 XP, может потратить до 15 000 едди на виртуальные развлечения, увеличивает параметры «Социальность» на 20 и «Стиль» на 10, получает достижение «Исследователь VR Ночи». Механики включают социальные взаимодействия, виртуальные развлечения и атмосферу киберпанк Токио.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '93c7c812861c07d50770215e38a1edd207155889071716086d6b8edb49e9381c',
    'caf07bf53d650f78941c74ef1e4989a35954c8ca17af108ed2c421ddc73f05fd',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-007-neo-tokyo-vr-nightlife.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-neo-vr-nightlife';

