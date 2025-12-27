--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-ramen-master runOnChange:true

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
    '2e6123de-6886-406d-b973-c3a18cd19d5b',
    'canon-quest-tokyo-ramen-master',
    'Токио 2020-2029 — Мастер Рамена',
    'Токио 2020-2029 — Мастер Рамена',
    'side',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
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
      "id": "main_objective",
      "text": "Выполнить основную цель квеста: Конвертировать сюжет про легендарного мастера в YAML и описать кулинарные эффекты и локационные заце..."
    }
  ],
  "rewards": {
    "experience": 500,
    "money": {
      "min": 200,
      "max": 800
    },
    "items": []
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Мастер Рамена",
    "body": "Этот side квест \"Токио 2020-2029 — Мастер Рамена\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Конвертировать сюжет про легендарного мастера в YAML и описать кулинарные эффекты и локационные заце...\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Легендарный мастер рамена ведёт ночной ларёк в переулках Синдзюку, где ароматные бульоны и свежей лапшей выстраиваются в целый ритуал посвящения гурманов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Собрать слухи о мастере через стриминговые каналы едока и уличных курьеров.\n2. Найти скрытый ятай, ориентируясь на ароматы, пар и замаскированные неоновые вывески.\n3. Выбрать вариацию рамена (сёю, мисо, тонкоцу) и наблюдать приготовление в катсцене.\n4. Пройти вкусовой QTE, подтверждая гастрономический экстаз и разблокируя секрет мастера.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Узкие переулки Синдзюку освещают красные фонари, пар от бульона смешивается с дождём, а ларёк рассчитан всего на восемь гостей с прямым видом на рабочую станцию шефа.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок тратит 500 едди, получает 800 XP, +30% HP и +15% выносливости на 12 часов, а также достижение «Гурман Токио» и доступ к дальнейшим гастрономическим квестам.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '0e812073ab0f8a85b2c3bd9bf73a7df5ff2ae38ca19b18d2dc3ac4418a4c3a58',
    '1673436aaf632f913e86e665847dd2e9c9c14e96071710ae1c9a6cca1bee9a91',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-004-ramen-master.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-ramen-master';

