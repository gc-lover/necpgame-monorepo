--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-golden-gate runOnChange:true

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
    '2816201d-7446-491d-a9d0-f6aa5c8ede2d',
    'canon-quest-kiev-golden-gate',
    'Киев 2020-2029 — «Золотые ворота»',
    'Киев 2020-2029 — «Золотые ворота»',
    'side',
    'Kiev',
    '2020-2029',
    'easy',
    '30-60 минут',
    3,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 3,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "visit_gates",
      "text": "Посетить реконструированные Золотые ворота"
    },
    {
      "id": "experience_vr_tour",
      "text": "Пройти VR-тур по Киеву XI века"
    },
    {
      "id": "find_secret_passage",
      "text": "Найти тайный подземный ход под воротами"
    },
    {
      "id": "explore_underground",
      "text": "Исследовать подземелья и собрать артефакты"
    }
  ],
  "rewards": {
    "experience": 350,
    "money": {
      "min": 50,
      "max": 200
    },
    "items": [
      "history_knowledge",
      "ancient_artifact",
      "cultural_insight"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Золотые ворота»",
    "body": "Этот side квест \"Киев 2020-2029 — «Золотые ворота»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Посетить реконструированные Золотые ворота\n2. Пройти VR-тур по Киеву XI века\n3. Найти тайный подземный ход под воротами\n4. Исследовать подземелья и собрать артефакты\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "arrival",
    "title": "Посещение ворот",
    "body": "Игрок прибывает к реконструированным Золотым воротам, узнаёт об истории строительства 1037 года и осматривает церковь\nБлаговещения, расположенную над входом. Экскурсовод даёт доступ к VR-модулям.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "vr",
    "title": "VR-экскурсия",
    "body": "Активируется VR-сцена «Киев XI века»: деревянные дома, княжеские кортежи и укрепления. Игрок может наблюдать город глазами\nЯрослава Мудрого и принимать исторические решения, влияющие на культурную репутацию.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "underground",
    "title": "Тайный ход",
    "body": "После VR-сеанса игрок обнаруживает вход в подземелья. Исследование древних коридоров приносит артефакты Руси и открывает\nдополнительную линию эксплоринга.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "1 500 XP, +10 к параметру «Культура» и шанс получить древние артефакты. История вписывается в историческую линию Киева.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'a012dd479e3cdbf65e2b7c15ae897caa5f692b33422c9ebd030cea4b9dc82643',
    'f8bb82938a467a513e73b9f616f1c9a81414e1299424400a8584fe429905b950',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-006-golden-gate.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-golden-gate';

