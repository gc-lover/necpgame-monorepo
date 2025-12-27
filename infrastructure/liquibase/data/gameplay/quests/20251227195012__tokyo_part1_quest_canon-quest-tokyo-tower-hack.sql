--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-tower-hack runOnChange:true

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
    '9bebefa8-312b-4d6f-becd-83cc4b179771',
    'canon-quest-tokyo-tower-hack',
    'Токио 2020-2029 — Взлом Токийской Башни',
    'Токио 2020-2029 — Взлом Токийской Башни',
    'main',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    15,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "main",
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
      "id": "main_objective",
      "text": "Выполнить основную цель квеста: Структурировать квест в YAML, увязать с системами хакинга, управления городом и ветвления последстви..."
    }
  ],
  "rewards": {
    "experience": 800,
    "money": {
      "min": 500,
      "max": 2000
    },
    "items": []
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Взлом Токийской Башни",
    "body": "Этот main квест \"Токио 2020-2029 — Взлом Токийской Башни\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Структурировать квест в YAML, увязать с системами хакинга, управления городом и ветвления последстви...\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Токийская башня переоборудована в медиахаб корпораций: антенны транслируют AR-рекламу, а защитные протоколы контролируют вкусы миллионов горожан.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Найти и нанять опытного нетраннера, подготовить оборудование и планы проникновения.\n2. Проникнуть в башню, выбрать стелс-маршрут через обслуживание или воспользоваться социальной инженерией.\n3. Подняться на вершину, обходя дроны, лазерные ловушки и корпоративные патрули.\n4. Взломать ядро управления AR, пройти проверку DC 20 и принять одно из стратегических решений.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "branching_outcomes",
    "title": "Варианты выбора",
    "body": "- Отключить рекламу: город получает чистое небо, жители благодарны, корпорации в ярости.\n- Захватить контроль: игрок монетизирует каналы, создаёт пассивный доход и усиливает свою власть.\n- Саботировать: хаос в медиасреде, массовые глюки, рост анархии и ответных мер корпораций.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Базовая награда: 3 000 XP. Дополнительно игрок получает 0–10 000 едди в неделю при контроле рекламы, массовую репутацию среди горожан при отключении и волну хаоса, влияющую на события Токио, при саботаже.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '0916a8b0e323dfed1e56924888e7b23d7d64d997b3cdf9d7a9318d6ce6ca6253',
    '07c8f9f32bec41fc5c57ec4bff19d0495c695da866b1522e77c6bd4ba8af71c2',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-005-tokyo-tower-hack.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-tower-hack';

