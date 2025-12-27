--liquibase formatted sql

--changeset astana-part1-quests:canon-quest-astana-modern-nomads runOnChange:true

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
    'ad1522fa-22e0-4a76-be4c-55614d18b754',
    'canon-quest-astana-modern-nomads',
    'Астана 2020-2029 — «Современные кочевники»',
    'Астана 2020-2029 — «Современные кочевники»',
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
      "id": "find_nomad_camp",
      "text": "Выехать из Астаны в степь и найти лагерь современных кочевников",
      "type": "interact",
      "target": "nomad_camp_finding",
      "count": 1,
      "optional": false
    },
    {
      "id": "meet_elder",
      "text": "Встретиться со старейшиной и пройти обряд знакомства",
      "type": "interact",
      "target": "nomad_elder_meeting",
      "count": 1,
      "optional": false
    },
    {
      "id": "help_infrastructure",
      "text": "Помочь починить метеостанцию, обновить сетевой узел и настроить канал связи",
      "type": "interact",
      "target": "nomad_infrastructure_help",
      "count": 1,
      "optional": false
    },
    {
      "id": "participate_traditions",
      "text": "Участвовать в традициях: кумыс, охота с беркутом и вечерний песенный круг",
      "type": "interact",
      "target": "nomad_traditions_participation",
      "count": 1,
      "optional": false
    }
  ],
  "rewards": {
    "experience": 2000,
    "money": 0,
    "reputation": {
      "culture": 15
    },
    "items": [],
    "unlocks": {
      "achievements": [
        "Друг кочевников"
      ],
      "flags": [
        "modern_nomads_faction_unlocked"
      ]
    }
  },
  "branches": []
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Астана 2020-2029 — «Современные кочевники»",
    "body": "Этот side квест \"Астана 2020-2029 — «Современные кочевники»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выехать из Астаны в степь и найти лагерь современных кочевников\n2. Встретиться со старейшиной и пройти обряд знакомства\n3. Помочь починить метеостанцию, обновить сетевой узел и настроить канал связи\n4. Участвовать в традициях: кумыс, охота с беркутом и вечерний песенный круг\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "arrival",
    "title": "Прибытие в лагерь",
    "body": "Игрок выезжает из Астаны в степь и находит лагерь, где юрты питаются от солнечных панелей. Старейшина объясняет\nкодекс кочевников и предлагает пройти обряд знакомства, включающий обмен подарками и короткий рассказ о роде.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "tradition_and_network",
    "title": "Традиции и инфраструктура",
    "body": "- Игрок помогает угощать кумысом, наблюдает охоту с беркутом и участвует в вечернем песенном круге.\n- Параллельно требуется починить метеостанцию, обновить сетевой узел и настроить безопасный канал связи с городом.\n- Каждая задача открывает новые диалоги о сохранении культуры и развитии автономных поселений.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "outcomes",
    "title": "Награды и влияние",
    "body": "- 2 000 XP, +15 к показателю «Культура» и достижение «Друг кочевников».\n- Доступ к фракционным заданиям по логистике степи и уникальным крафтовым схемам (мобильная юрта, модификаторы для транспорта).\n- Возможность вызвать кочевой караван для временного лагеря в других регионах.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '63b1717fec80448942b474636cf11924dc1eb98093044d7d3ac541cab9f7cfa9',
    '68be79db8337b82f70b5fdcf2545ab3bbf2b41ae4979d22210aab0efcd6e8051',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\astana\2020-2029\quest-006-modern-nomads.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-astana-modern-nomads';

