--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-shrine-offering runOnChange:true

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
    'da256d84-9be2-40ef-80f5-188b7719d6fc',
    'canon-quest-tokyo-shrine-offering',
    'Токио 2020-2029 — Приношение в Храме',
    'Токио 2020-2029 — Приношение в Храме',
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
      "text": "Выполнить основную цель квеста: Формализовать храмовый ритуал в YAML, показав механики очищения, приношений и случайных предсказаний..."
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
    "title": "Обзор: Токио 2020-2029 — Приношение в Храме",
    "body": "Этот side квест \"Токио 2020-2029 — Приношение в Храме\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Формализовать храмовый ритуал в YAML, показав механики очищения, приношений и случайных предсказаний...\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Мэйдзи Дзингу остаётся оазисом спокойствия в центре неонового Токио, где игроки ищут благословение ками перед важными миссиями.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Пройти через тории, минуя толпы туристов и добираясь до внутреннего двора.\n2. Выполнить омовение рук и рта у темидзуя, следуя подсказкам NPC-монаха.\n3. Бросить монету в ящик приношений, сделать два хлопка и глубокий поклон.\n4. Вытянуть омикудзи и получить случайное предсказание, закрепив его на древе удачи или забрав с собой.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Лесной коридор, аромат благовоний, деревянные эма с желаниями игроков и монахи в белых хакама создают аутентичную атмосферу.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок тратит 5 йен, получает 400 XP и шанс на бафф +5% к удаче на 24 часа. Неблагоприятное омикудзи можно нейтрализовать повторным посещением.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'a09b37d6a2fe818ff6112436a76117d57bcc0ae008093ebc228bbca089568db2',
    '58e772dfcac18d17b7431b1ac632116a08082781bb354a6dc3079e22d8188347',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-008-shrine-offering.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-shrine-offering';

