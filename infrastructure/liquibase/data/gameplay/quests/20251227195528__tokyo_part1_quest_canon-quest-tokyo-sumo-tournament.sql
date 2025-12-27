--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-sumo-tournament runOnChange:true

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
    'f34692f8-8baa-445f-b647-fe361506ce75',
    'canon-quest-tokyo-sumo-tournament',
    'Токио 2020-2029 — Турнир Сумо',
    'Токио 2020-2029 — Турнир Сумо',
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
      "text": "Выполнить основную цель квеста: Оформить кибер-сумо в YAML, подчеркнув стадионную атмосферу, прогрессию боёв и титул ёкодзуна...."
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
    "title": "Обзор: Токио 2020-2029 — Турнир Сумо",
    "body": "Этот side квест \"Токио 2020-2029 — Турнир Сумо\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Оформить кибер-сумо в YAML, подчеркнув стадионную атмосферу, прогрессию боёв и титул ёкодзуна....\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Турнир сумо в Токио соединяет вековые ритуалы с нейроусиленными экзокостюмами, привлекая зрителей и трансляции по всему мегаполису.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Пройти регистрацию и оплатить взнос в 2 000 едди, получив доступ к экзокостюму.\n2. Выполнить ритуал очищения солью и войти на дохё под руководством гёдзи.\n3. Провести серию матчей, используя толчки, броски и силовые импульсы костюма.\n4. В финале победить ёкодзуну, адаптируясь к его усиленному оборудованию и контратакам.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Описаны традиционные барабаны тайко, голографические флаги кланов, семпаи в кимоно и толпы фанатов, ставящих ставки в AR-интерфейсах.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Чемпион получает 30 000 едди, титул «Ёкодзуна» и доступ к элитным турнирам. Поражение даёт опыт и репутацию в кибер-спортсообществе.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '0fb594dd638164df153edb4c95f979e11fbc912c0e2d90abc53aeb27fc65e36e',
    '1995586ca5e8e9e2b6e51221b7525d36a9d1a6a41745ed995eb37ec64368b3d3',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-007-sumo-tournament.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-sumo-tournament';

