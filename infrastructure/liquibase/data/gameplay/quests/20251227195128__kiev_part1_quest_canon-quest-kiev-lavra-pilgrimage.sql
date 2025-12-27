--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-lavra-pilgrimage runOnChange:true

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
    '96c73681-d3a1-4105-a077-5ad890877a9f',
    'canon-quest-kiev-lavra-pilgrimage',
    'Киев 2020-2029 — «Паломничество в Лавру»',
    'Киев 2020-2029 — «Паломничество в Лавру»',
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
  "level_max": null
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Паломничество в Лавру»",
    "body": "Этот side квест \"Киев 2020-2029 — «Паломничество в Лавру»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "entrance",
    "title": "Вход в лавру",
    "body": "Игрок входит через Святые врата, слышит колокольный звон и наблюдает панораму золотых куполов. Настоятель объясняет правила\nповедения и выдаёт карту маршрута.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "cathedral",
    "title": "Успенский собор",
    "body": "Посещение главного собора с реликвиями и стенописями. Игрок зажигает свечу, слушает службу и получает духовный отклик,\nповышающий репутацию с религиозными фракциями.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "caves",
    "title": "Пещеры и мощи",
    "body": "Спуск в пещеры с мощами святых монахов. Игрок следует лабиринтом, отмечает святыни и ведёт запись ауры для timeline.\nТемнота и запах ладана усиливают атмосферу.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "blessing",
    "title": "Благословение и награды",
    "body": "Финальный этап предполагает беседу с монахом и получение благословения. Игрок получает 1 000 XP, -10 едди (пожертвование),\nбафф «Благословение» (+10 % к защите на 48 часов) и ачивку «Паломник Киева».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '8b6cb42301605207341734f171797348dddb1fedfe754846666fd401a1f9040c',
    '30b6ef117de8ccfc3a6efc93066aba3c6b3a1b9ab4e38ab5ebe825e3296220eb',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-002-lavra-pilgrimage.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-lavra-pilgrimage';

