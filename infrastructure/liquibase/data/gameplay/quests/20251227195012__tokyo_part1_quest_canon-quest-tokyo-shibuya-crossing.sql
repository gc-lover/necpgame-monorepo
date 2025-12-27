--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-shibuya-crossing runOnChange:true

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
    'e9019ce7-1265-44d5-8803-7f37170f24b2',
    'canon-quest-tokyo-shibuya-crossing',
    'Токио 2020-2029 — Перекрёсток Сибуя',
    'Токио 2020-2029 — Перекрёсток Сибуя',
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
      "id": "receive_package",
      "text": "Получить пакет для доставки на Shibuya Crossing"
    },
    {
      "id": "navigate_crowd",
      "text": "Проложить путь через плотную толпу пешеходов"
    },
    {
      "id": "avoid_surveillance",
      "text": "Избежать обнаружения системами наблюдения"
    },
    {
      "id": "deliver_package",
      "text": "Доставить пакет получателю в указанное время"
    }
  ],
  "rewards": {
    "experience": 800,
    "money": {
      "min": 500,
      "max": 1500
    },
    "items": [
      "courier_reputation",
      "tokyo_delivery_bonus"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Перекрёсток Сибуя",
    "body": "Этот side квест \"Токио 2020-2029 — Перекрёсток Сибуя\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Получить пакет для доставки на Shibuya Crossing\n2. Проложить путь через плотную толпу пешеходов\n3. Избежать обнаружения системами наблюдения\n4. Доставить пакет получателю в указанное время\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Перекрёсток Сибуя остаётся главной транспортной развязкой неонового Токио: тысячи горожан пересекают площадь под голографическими билбордами, а камеры корпораций анализируют каждое перемещение.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Встретиться со связным у статуи Хачико и получить защищённый пакет.\n2. Пройти перекрёсток, используя толпу как укрытие и подавляя трекеры слежения.\n3. Стабилизировать AR-карту, чтобы обнаружить скрытый маршрут среди рекламных шумов.\n4. Доставить отправление в пункт выдачи и выполнить биометрическое подтверждение клиента.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Толпы офисных кибер-работников, неоновые фасады, звуковой хаос поездов и уличных музыкантов, а также культурный ориентир в виде статуи Хачико формируют узнаваемый профиль Сибуи.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 300 XP, 800 едди и +1 репутации в курьерской сети. Дополнительно открываются заказы с доставкой в деловом поясе Токио и повышается доверие посредников.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'fe065b78692b6ec3dca43b7e2be5638a90ee904780fac1e34d4660e4a914947c',
    '1933151d4b92221ec7907e6422dcfce1cd01cfd107d229a7664fb6c93c847b6d',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-001-shibuya-crossing.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-shibuya-crossing';

