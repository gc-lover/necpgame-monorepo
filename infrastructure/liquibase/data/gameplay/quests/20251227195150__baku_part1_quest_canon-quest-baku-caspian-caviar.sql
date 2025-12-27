--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-caspian-caviar runOnChange:true

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
    'aa583bd2-a697-459c-9576-04e70372aecf',
    'canon-quest-baku-caspian-caviar',
    'Баку — Каспийская икра',
    'Баку — Каспийская икра',
    'side',
    'Baku',
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
      "id": "visit_caviar_farm",
      "text": "Посетить икорную ферму на побережье Каспия"
    },
    {
      "id": "learn_production_process",
      "text": "Изучить процесс производства черной икры"
    },
    {
      "id": "taste_different_types",
      "text": "Попробовать разные виды каспийской икры"
    },
    {
      "id": "understand_ecological_impact",
      "text": "Понять экологические проблемы и меры защиты"
    }
  ],
  "rewards": {
    "experience": 300,
    "money": {
      "min": 100,
      "max": 300
    },
    "items": [
      "caviar_connoisseur",
      "luxury_food_access",
      "environmental_awareness"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку — Каспийская икра",
    "body": "Этот side квест \"Баку — Каспийская икра\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_profile",
    "title": "Параметры квеста",
    "body": "Тип: social. Сложность: medium. Формат: solo. Длительность: 1–2 часа.\nНаграды: 2,000 XP, -500 едди, бафф «Гурман» (+50% XP на 24 часа). Ачивка «Икорный Гурман».\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы",
    "body": "1. Найти сертифицированную икорную ферму и получить доступ.\n2. Изучить процесс разведения осетра и извлечения икры.\n3. Провести дегустацию белужьей, осетровой и севрюжьей икры.\n4. Приобрести банку в качестве инвестиции или подарка.\n5. Обсудить темы браконьерства и охраны вида.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Каспийское море — уникальный ареал осетра. Чёрная икра — символ азербайджанской роскоши и наследия СССР.\nИкорные фермы заботятся о долгосрочной защите вида.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "systems_hooks",
    "title": "Системные крючки",
    "body": "economy-service (роскошный товар), food-system (деликатес), mood-system (бафф «Эстетический восторг»). Возможны контракты на поставку для VIP-клиентов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "2,000 XP, -500 едди, бафф «Гурман» (+50% XP на 24 ч), ачивка «Икорный Гурман».\nОткрывает квесты о гастрономии Баку и инвестициях в фермы.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '79486fccf31f6e27f33bec023152cf6625800fa6dec4ac89f5ad90e094b2be73',
    '1a5f7825de7aa3dcc1eafbcf1128483b05f3f358bdc8bbc8bf112a966ebfe5e6',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-004-caspian-caviar.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-caspian-caviar';

