--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-gobustan runOnChange:true

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
    '8616ad02-1759-4c86-8f9d-c175bb9991a6',
    'canon-quest-baku-gobustan',
    'Баку 2020-2029 — «Петроглифы Гобустана»',
    'Баку 2020-2029 — «Петроглифы Гобустана»',
    'side',
    'Baku',
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
      "id": "travel_to_gobustan",
      "text": "Добраться из Баку в заповедник Гобустан",
      "type": "interact",
      "target": "gobustan_travel",
      "count": 1,
      "optional": false
    },
    {
      "id": "visit_museum",
      "text": "Посетить интерактивный музей с VR-экспозициями жизни охотников",
      "type": "interact",
      "target": "gobustan_museum_vr",
      "count": 1,
      "optional": false
    },
    {
      "id": "explore_petroglyphs",
      "text": "Исследовать скальный массив и документировать петроглифы",
      "type": "interact",
      "target": "petroglyphs_documentation",
      "count": 1,
      "optional": false
    },
    {
      "id": "visit_mud_volcanoes",
      "text": "Посетить грязевые вулканы и собрать образцы",
      "type": "interact",
      "target": "mud_volcanoes_collection",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 2500,
    "money": 0,
    "reputation": {
      "archaeology": 15
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Археолог Гобустана"
      ],
      "flags": [
        "gobustan_explored"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — «Петроглифы Гобустана»",
    "body": "Этот side квест \"Баку 2020-2029 — «Петроглифы Гобустана»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Добраться из Баку в заповедник Гобустан\n2. Посетить интерактивный музей с VR-экспозициями жизни охотников\n3. Исследовать скальный массив и документировать петроглифы\n4. Посетить грязевые вулканы и собрать образцы\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "museum",
    "title": "Музей и подготовка",
    "body": "Игрок прибывает в интерактивный музей Гобустана, где VR-экспозиции демонстрируют жизнь охотников, танцы и обряды.\nКуратор объясняет правила посещения и уязвимость древних рисунков.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "trail",
    "title": "Тропа петроглифов",
    "body": "Посещение скального массива с гидами. Игрок документирует сцены охоты, танцевальные хороводы и изображения животных,\nиспользуя сканер для архива и взаимодействие с timeline.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "mud_volcanoes",
    "title": "Грязевые вулканы",
    "body": "В завершение реализуется дополнительная активность — путешествие к грязевым вулканам. Игрок собирает образцы и получает\nбонус к геологической ветке.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и эффекты",
    "body": "- 2 500 XP, +15 к параметру «Археология» и ачивка «Археолог Гобустана».\n- Репутация среди научных фракций и доступ к дополнительным исследованиям.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '5a2cfa3937078890e5f88246c0f6de2f8a976087a4c658f5b806dc1f54db31a0',
    '2b6df3404afe5c44d11db8a760d501f6a9119cdc82350fcf0fe038c5a0723f28',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-009-gobustan-petroglyphs.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-gobustan';

