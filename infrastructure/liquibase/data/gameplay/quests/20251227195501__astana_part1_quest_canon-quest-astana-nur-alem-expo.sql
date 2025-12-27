--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-nur-alem-expo runOnChange:true

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
    'cf08b38c-3056-4791-9ea8-01153b3fdf91',
    'canon-quest-astana-nur-alem-expo',
    'Астана 2020-2029 — Нур Алем (EXPO)',
    'Астана 2020-2029 — Нур Алем (EXPO)',
    'side',
    'Astana',
    '2020-2029',
    'easy',
    '30-60 минут',
    2,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 2,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "enter_pavilion",
      "text": "Войти в павильон Нур Алем и получить интерактивный гид",
      "type": "interact",
      "target": "nur_alem_entrance",
      "count": 1,
      "optional": false
    },
    {
      "id": "explore_energy_floors",
      "text": "Исследовать этажи возобновляемой энергии и выполнить мини-игры",
      "type": "interact",
      "target": "energy_floors_exploration",
      "count": 1,
      "optional": false
    },
    {
      "id": "reach_observation_deck",
      "text": "Подняться на верхний уровень, посмотреть панораму EXPO и получить сертификат",
      "type": "interact",
      "target": "expo_observation_deck",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 1500,
    "money": -20,
    "reputation": {
      "science": 15
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Исследователь Будущего"
      ],
      "flags": [
        "nur_alem_explored"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — Нур Алем (EXPO)",
    "body": "Этот side квест \"Астана 2020-2029 — Нур Алем (EXPO)\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Войти в павильон Нур Алем и получить интерактивный гид\n2. Исследовать этажи возобновляемой энергии и выполнить мини-игры\n3. Подняться на верхний уровень, посмотреть панораму EXPO и получить сертификат\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Сферический павильон Нур Алем рассказывает об «Энергии будущего», предлагая исследования солнечной, ветровой и космической энергии.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Войти в павильон и получить интерактивный гид.\n2. Исследовать этажи возобновляемой энергии и выполнить мини-игры.\n3. Подняться на верхний уровень, посмотреть панораму EXPO и получить сертификат исследователя.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "1 500 XP, -20 едди, +15 к параметру «Наука» и достижение «Исследователь Будущего».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '2a59993616ab2d5a3eaebd444e9f610059323195de668e28f939b1fa13e89713',
    '21fad7e8f78e1540fac727f56165d4e4c6c07b9063b66e2794c8d7191a08406f',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-003-nur-alem-expo.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-nur-alem-expo';

