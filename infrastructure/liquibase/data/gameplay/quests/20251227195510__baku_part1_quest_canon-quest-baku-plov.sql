--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-plov runOnChange:true

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
    'b19f1e17-993b-47b2-982c-4776f972f17b',
    'canon-quest-baku-plov',
    'Баку 2020-2029 — «Плов по-бакински»',
    'Баку 2020-2029 — «Плов по-бакински»',
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
      "id": "book_restaurant",
      "text": "Забронировать стол в традиционном бакинском ресторане",
      "type": "interact",
      "target": "baku_restaurant_booking",
      "count": 1,
      "optional": false
    },
    {
      "id": "learn_plov_history",
      "text": "Выслушать рассказ шефа об истории плова и отличиях от узбекской версии",
      "type": "interact",
      "target": "plov_history_lesson",
      "count": 1,
      "optional": false
    },
    {
      "id": "taste_variations",
      "text": "Попробовать плов с бараниной, каштанами и сухофруктами",
      "type": "interact",
      "target": "plov_tasting",
      "count": 1,
      "optional": false
    },
    {
      "id": "try_kazmag",
      "text": "Попробовать казмаг — хрустящий слой со дна казана",
      "type": "interact",
      "target": "kazmag_tasting",
      "count": 1,
      "optional": false
    },
    {
      "id": "saffron_tea",
      "text": "Выпить шафрановый чай и выслушать рассказ о семейных рецептах",
      "type": "interact",
      "target": "saffron_tea_ceremony",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 800,
    "money": -40,
    "reputation": {
      "culinary": 10
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Мастер плова"
      ],
      "flags": [
        "baku_plov_tasted"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — «Плов по-бакински»",
    "body": "Этот side квест \"Баку 2020-2029 — «Плов по-бакински»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Забронировать стол в традиционном бакинском ресторане\n2. Выслушать рассказ шефа об истории плова и отличиях от узбекской версии\n3. Попробовать плов с бараниной, каштанами и сухофруктами\n4. Попробовать казмаг — хрустящий слой со дна казана\n5. Выпить шафрановый чай и выслушать рассказ о семейных рецептах\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "restaurant",
    "title": "Ресторан и подготовка",
    "body": "Игрок бронирует стол в традиционном бакинском заведении. Шеф знакомит с историей плова, объясняет, чем версия отличается от узбекской,\nи показывает шафран, выращенный на Каспии.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "tasting",
    "title": "Дегустация и вариации",
    "body": "- Плов с бараниной и сушёными фруктами для баланса сладкого и солёного.\n- Плов с каштанами, создающий текстурный контраст.\n- Казмаг — хрустящий слой со дна казана, подаваемый как деликатес.\nПосле дегустации подаётся шафрановый чай и рассказ о семейных рецептах.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и эффекты",
    "body": "- 800 XP, -40 едди и бафф «Сатiety» (+20 % HP на 8 часов).\n- Ачивка «Мастер плова» и новые рецепты для лагерной кухни.\n- Рост репутации среди кулинарных фракций Баку.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'a79d572c1ecf255f6aa21840c40705ee5c0c1d12b3060304910dd5e6b3fddb7b',
    '48a7ea0883e3a9bcc1c6dfb4947214b82ab82da9056e20905e0a8269b0b6b2c1',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-008-plov-baku-style.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-plov';

