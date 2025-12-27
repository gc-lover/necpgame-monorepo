--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-maidan-legacy runOnChange:true

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
    'da3abafb-231c-4e11-87da-cef702fdd151',
    'canon-quest-kiev-maidan-legacy',
    'Киев 2020-2029 — «Наследие Майдана»',
    'Киев 2020-2029 — «Наследие Майдана»',
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
    "title": "Обзор: Киев 2020-2029 — «Наследие Майдана»",
    "body": "Этот side квест \"Киев 2020-2029 — «Наследие Майдана»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
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
    "id": "memorial",
    "title": "Мемориал и хроника",
    "body": "Игрок прибывает на площадь, взаимодействует с цифровыми стендами и архивами протеста, слушает истории «Небесной Сотни»\nи получает задания на сбор воспоминаний. Колонна Независимости, флаги и цветы формируют эмоциональный фон.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "veterans",
    "title": "Встреча с ветеранами",
    "body": "Диалоговая сцена с участниками революций. Игрок фиксирует их рассказы, сопоставляет перспективы 2004 и 2014 годов и\nформирует собственное понимание свободы и ответственности.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "choice",
    "title": "Решение о будущем",
    "body": "- Поддержать новый протест, увеличив репутацию «Свобода» и усилив риск конфликта.\n- Сохранить статус-кво, укрепив отношения с городскими властями и инвесторами.\n- Инициировать компромиссный стол, открывая дипломатические цепочки.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "outcomes",
    "title": "Последствия",
    "body": "Итог фиксируется в политической линии Киева, меняет события будущих квестов и добавляет новости в мировую ленту.\nИгрок получает 2 000 XP и +10 к репутации свободы при активной поддержке Майдана.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'e70575f2dd5f327762cf5a035e2f5eb9430793c16176c5e6bb6b5c8ed17c381b',
    '30b6ef117de8ccfc3a6efc93066aba3c6b3a1b9ab4e38ab5ebe825e3296220eb',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-001-maidan-legacy.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-maidan-legacy';

