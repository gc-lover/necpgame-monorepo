--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-dnieper-crossing runOnChange:true

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
    'c6cfa4a9-5866-42e4-aa7e-272c208527b2',
    'canon-quest-kiev-dnieper-crossing',
    'Киев 2020-2029 — «Переправа через Днепр»',
    'Киев 2020-2029 — «Переправа через Днепр»',
    'side',
    'Kiev',
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
      "id": "choose_crossing_method",
      "text": "Выбрать способ переправы через Днепр (мост, паром, лодка)"
    },
    {
      "id": "navigate_river",
      "text": "Преодолеть реку и избежать препятствий"
    },
    {
      "id": "encounter_smugglers",
      "text": "Столкнуться с контрабандистами на острове"
    },
    {
      "id": "make_choice",
      "text": "Решить: вмешаться, проигнорировать или присоединиться"
    }
  ],
  "rewards": {
    "experience": 500,
    "money": {
      "min": 100,
      "max": 300
    },
    "items": [
      "transport_access",
      "river_navigation_skill"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Переправа через Днепр»",
    "body": "Этот side квест \"Киев 2020-2029 — «Переправа через Днепр»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выбрать способ переправы через Днепр (мост, паром, лодка)\n2. Преодолеть реку и избежать препятствий\n3. Столкнуться с контрабандистами на острове\n4. Решить: вмешаться, проигнорировать или присоединиться\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "planning",
    "title": "Планирование маршрута",
    "body": "Игрок анализирует карту Киева, выбирает между мостом Патона, Метромостом или лодкой. В каждой опции собственные проверки\nнавыков и временные затраты.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "crossing",
    "title": "Переправа",
    "body": "- Мост Патона: автомобильное движение и история первого сварного моста.\n- Метромост: поезд Политехнического института с видом на Днепр.\n- Лодка: манёвры между островами при сильном течении.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "encounter",
    "title": "Встреча на реке",
    "body": "Игрок сталкивается с контрабандистами. Возможные решения:\n- Помочь и получить 500 едди, но потерять репутацию с властями.\n- Арестовать и повысить доверие городской безопасности.\n- Игнорировать ради нейтралитета и логистических бонусов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "800 XP, навык «Навигация» по мостам и вклад в транспортную линию Киева. Выбор фиксируется в timeline.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '22878240aa445e7573397a3442e06d90e8599cdec2a211d94cd3f987a5594c1e',
    '807556c287c3fac3c62cd2cdfc96f9b3d244a408f8a4c67157f70c11892095ff',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-003-dnieper-crossing.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-dnieper-crossing';

