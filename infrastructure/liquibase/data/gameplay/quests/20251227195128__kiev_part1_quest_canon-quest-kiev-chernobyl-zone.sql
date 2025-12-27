--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-chernobyl-zone runOnChange:true

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
    '0889e91c-22f3-456c-a709-45310d4416b9',
    'canon-quest-kiev-chernobyl-zone',
    'Киев 2020-2029 — «Зона Чернобыля»',
    'Киев 2020-2029 — «Зона Чернобыля»',
    'faction',
    'Kiev',
    '2020-2029',
    'easy',
    '30-60 минут',
    15,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "faction",
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
      "id": "accept_mission",
      "text": "Принять контракт на экспедицию в Чернобыльскую зону"
    },
    {
      "id": "navigate_zone",
      "text": "Преодолеть радиацию и аномалии зоны отчуждения"
    },
    {
      "id": "explore_pripyat",
      "text": "Исследовать ключевые локации Припяти"
    },
    {
      "id": "extract_artifact",
      "text": "Найти и извлечь целевой артефакт"
    }
  ],
  "rewards": {
    "experience": 2000,
    "money": {
      "min": 2000,
      "max": 5000
    },
    "items": [
      "radiation_resistance",
      "chernobyl_artifact",
      "zone_survivor_title"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Зона Чернобыля»",
    "body": "Этот faction квест \"Киев 2020-2029 — «Зона Чернобыля»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Принять контракт на экспедицию в Чернобыльскую зону\n2. Преодолеть радиацию и аномалии зоны отчуждения\n3. Исследовать ключевые локации Припяти\n4. Найти и извлечь целевой артефакт\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "briefing",
    "title": "Контракт и подготовка",
    "body": "Игроки получают контракт на артефакт, подготавливают антирадиационные наборы, транспорт и сопровождающих сталкеров.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "pripyat",
    "title": "Поездка в Припять",
    "body": "Колонна пересекает 30-километровую зону отчуждения, дозиметр сигнализирует о всплесках. Команда изучает пустые улицы,\nржавое колесо обозрения и школы с оставленными игрушками.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "anomalies",
    "title": "Аномалии и мутанты",
    "body": "Радиационные поля, электрические аномалии и мутировавшие животные. Игроки используют датчики, ловушки и скрытность.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "extraction",
    "title": "Артефакт и эвакуация",
    "body": "Артефакт найден в подземных помещениях больницы. Команда активирует экстракцию, контролирует перегрев дозиметра и вывозит\nнаходку к безопасной зоне.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "8 000 XP, 50 000 едди и уникальные артефакты. В случае провала — радиационная болезнь и потеря репутации в экспедиционных фракциях.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '10439926cdcc862230e82e0c25e475e75c8579d9d492094fe9d57a083829b136',
    'bfbf5295442e55e874076939e2dedc47a6e59252f34421de9a77b8035dee37c4',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-008-chernobyl-zone.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-chernobyl-zone';

