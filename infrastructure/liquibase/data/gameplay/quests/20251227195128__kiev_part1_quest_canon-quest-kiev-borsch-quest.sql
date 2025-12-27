--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-borsch-quest runOnChange:true

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
    '02fa61df-0187-483b-bcbe-7bb148139676',
    'canon-quest-kiev-borsch-quest',
    'Киев 2020-2029 — «Квест борща»',
    'Киев 2020-2029 — «Квест борща»',
    'social',
    'Kiev',
    '2020-2029',
    'easy',
    '30-60 минут',
    1,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "social",
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
      "id": "taste_red_borsch",
      "text": "Попробовать классический красный борщ"
    },
    {
      "id": "taste_green_borsch",
      "text": "Попробовать зелёный борщ из щавеля"
    },
    {
      "id": "taste_cold_borsch",
      "text": "Попробовать холодный борщ окрошка"
    },
    {
      "id": "vote_for_best",
      "text": "Проголосовать за лучший борщ и объяснить выбор"
    }
  ],
  "rewards": {
    "experience": 300,
    "money": {
      "min": 50,
      "max": 150
    },
    "items": [
      "health_buff",
      "culinary_access",
      "borsch_connoisseur_title"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Квест борща»",
    "body": "Этот social квест \"Киев 2020-2029 — «Квест борща»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Попробовать классический красный борщ\n2. Попробовать зелёный борщ из щавеля\n3. Попробовать холодный борщ окрошка\n4. Проголосовать за лучший борщ и объяснить выбор\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "survey",
    "title": "Сбор рекомендаций",
    "body": "Игрок опрашивает жителей Подола, Печерска и Оболони о лучшем борще. Формируется список ресторанов и визуализируется карта маршрута.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "tasting",
    "title": "Дегустация",
    "body": "Посещение трёх точек с разными стилями: классический красный борщ со свеклой, зелёный со щавелем и холодный летний вариант.\nОбязательные дополнения: сметана, сало и пампушки.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "debate",
    "title": "Финал и спор",
    "body": "Игрок объявляет победителя, запускает спор с приезжими из Москвы и фиксирует культурный эффект.\nВозможность выбрать дипломатичный, провокационный или шутливый итог.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "600 XP, -30 едди и бафф «Сытость» (+25 % HP на 8 часов). Ачивка «Знаток борща» и рост репутации в кулинарной линии Киева.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'd52cb7189728d05c32ff59d65b977f36c085f4fec5758cd9c6b05b4b0c1e5c31',
    '7fbf52ca7f93fd7eae192348f3e732d865e9ae00431289b6278a14284e070678',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-004-borsch-quest.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-borsch-quest';

