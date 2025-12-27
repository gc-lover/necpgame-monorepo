--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-pachinko-addiction runOnChange:true

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
    '6206149b-68d1-467b-b603-95e8f98b89a6',
    'canon-quest-tokyo-pachinko-addiction',
    'Токио 2020-2029 — Зависимость от Патинко',
    'Токио 2020-2029 — Зависимость от Патинко',
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
      "text": "Выполнить основную цель квеста: Перевести историю спасения NPC в YAML, выделив пути силового вмешательства, выкупа долга и терапии...."
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
    "title": "Обзор: Токио 2020-2029 — Зависимость от Патинко",
    "body": "Этот side квест \"Токио 2020-2029 — Зависимость от Патинко\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Выполнить основную цель квеста: Перевести историю спасения NPC в YAML, выделив пути силового вмешательства, выкупа долга и терапии....\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Патинко-парлор окутан шумом автоматов, табачным дымом и тенью криминала, а игроку предстоит вывести друга из замкнутого круга зависимости.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Встретиться с зависимым NPC, узнать о его долгах и следах якудза.\n2. Проникнуть в патинко-парлор, преодолевая отвлекающий шум и давление охраны.\n3. Противостоять представителям якудза и выбрать подход к урегулированию конфликта.\n4. Сопроводить NPC наружу и закрепить выбранный исход.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "branching_outcomes",
    "title": "Варианты выбора",
    "body": "- Силой: вступить в бой, рискуя травмами и усиливая вражду с якудза.\n- Выкуп: заплатить 20 000 едди и очистить долг, сохранив нейтралитет с мафией.\n- Лечение: отправить NPC в клинику, получив долгосрочную благодарность, но отложенную награду.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 1 200 XP и рост репутации с благодарной семьёй. Расходы зависят от выбранного пути, а отношение якудза меняется соответствующим образом.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '0ff076aeb230605ed713491adb6d2fec2a42668035673319459d877674427a23',
    'dc5541c6aa1df44c14a860d0b40c97daeef227fcf834478a713a93286f4a6013',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-010-pachinko-addiction.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-pachinko-addiction';

