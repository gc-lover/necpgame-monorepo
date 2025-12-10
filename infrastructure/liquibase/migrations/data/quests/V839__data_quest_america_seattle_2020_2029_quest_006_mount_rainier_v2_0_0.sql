-- Issue: #50, #1063
-- Import quest from: america\seattle\2020-2029\quest-006-mount-rainier.yaml
-- Version: 2.0.0
-- Generated: 2025-12-08T19:30:00.000000

BEGIN;

-- Quest: canon-quest-seattle-mount-rainier
INSERT INTO gameplay.quest_definitions (
    quest_id,
    title,
    description,
    quest_type,
    level_min,
    level_max,
    requirements,
    objectives,
    rewards,
    branches,
    content_data,
    version,
    is_active
) VALUES (
    'canon-quest-seattle-mount-rainier',
    'Сиэтл — Гора Рейнир',
    'Группа игроков поднимается из Национального парка Mount Rainier к снежной вершине, балансируя между красотой и опасностью активного вулкана.',
    'main',
    5,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":["hiking_gear"]}'::jsonb,
    '[
      {"id":"reach_national_park","text":"Добраться до Mount Rainier National Park из Сиэтла, подготовив снаряжение и разрешения","type":"interact","target":"mount_rainier_preparation","count":1,"optional":false},
      {"id":"explore_paradise","text":"Пройти область Paradise с лугами и обзорными площадками, получить первое впечатление от ледников","type":"interact","target":"paradise_area_exploration","count":1,"optional":false},
      {"id":"follow_skyline_trail","text":"Следовать по Skyline Trail, лавируя между ледяными участками и трещинами","type":"interact","target":"skyline_trail_ascent","count":1,"optional":false},
      {"id":"climb_snowy_summit","text":"Взобраться на снежную вершину, преодолевая погодные угрозы и отслеживая сейсмику","type":"interact","target":"snowy_summit_climb","count":1,"optional":false},
      {"id":"safe_descent","text":"Совершить безопасный спуск и зафиксировать панораму города на горизонте","type":"interact","target":"safe_descent_panorama","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":4000,"money":-50,"reputation":{"nature":30},"items":[],"unlocks":{"achievements":[],"flags":["mount_rainier_climbed"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-seattle-mount-rainier",
        "title": "Сиэтл — Гора Рейнир",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "2.0.0",
        "last_updated": "2025-12-06T11:40:00+00:00",
        "concept_approved": true,
        "concept_reviewed_at": "",
        "owners": [{"role": "narrative_team", "contact": "narrative@necp.game"}],
        "tags": ["seattle", "nature", "expedition"],
        "topics": ["regional-content", "exploration"],
        "related_systems": ["traversal-system", "survival-system"],
        "related_documents": [
          {
            "id": "github-issue-368",
            "title": "GitHub Issue #368",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/368",
            "relation": "migrated_to",
            "migrated_at": "2025-11-22T21:30:00Z"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/america/seattle/2020-2029/quest-006-mount-rainier.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "quest-design"],
        "risk_level": "high"
      },
      "review": {
        "chain": [{"role": "narrative_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Материал о походе на Mount Rainier существовал только в текстовом описании и не раскрывал системные риски активного вулкана.",
        "goal": "Структурировать экспедиционный квест на гору Рейнир для дальнейшей балансировки и интеграции с механиками выживания.",
        "essence": "Группа игроков поднимается из Национального парка Mount Rainier к снежной вершине, балансируя между красотой и опасностью активного вулкана.",
        "key_points": [
          "Квест-id SEATTLE-2029-006, формат группа 2–4, длительность 8+ часов, высокий риск окружения.",
          "Стадии включают путешествие в парк, треккинг по Paradise, выход на Skyline Trail и штурм вершины с постоянным снегом.",
          "Награды усиливают природную репутацию, но требуют затрат и готовности к угрозе извержения."
        ]
      },
      "quest_definition": {
        "quest_type": "main",
        "level_min": 5,
        "level_max": null,
        "requirements": {
          "required_quests": [],
          "required_flags": [],
          "required_reputation": {},
          "required_items": ["hiking_gear"]
        },
        "objectives": [
          {"id": "reach_national_park", "text": "Добраться до Mount Rainier National Park из Сиэтла, подготовив снаряжение и разрешения", "type": "interact", "target": "mount_rainier_preparation", "count": 1, "optional": false},
          {"id": "explore_paradise", "text": "Пройти область Paradise с лугами и обзорными площадками, получить первое впечатление от ледников", "type": "interact", "target": "paradise_area_exploration", "count": 1, "optional": false},
          {"id": "follow_skyline_trail", "text": "Следовать по Skyline Trail, лавируя между ледяными участками и трещинами", "type": "interact", "target": "skyline_trail_ascent", "count": 1, "optional": false},
          {"id": "climb_snowy_summit", "text": "Взобраться на снежную вершину, преодолевая погодные угрозы и отслеживая сейсмику", "type": "interact", "target": "snowy_summit_climb", "count": 1, "optional": false},
          {"id": "safe_descent", "text": "Совершить безопасный спуск и зафиксировать панораму города на горизонте", "type": "interact", "target": "safe_descent_panorama", "count": 1, "optional": false}
        ],
        "rewards": {
          "experience": 4000,
          "money": -50,
          "reputation": {"nature": 30},
          "items": [],
          "unlocks": {
            "achievements": [],
            "flags": ["mount_rainier_climbed"]
          }
        },
        "branches": []
      },
      "content": {
        "sections": [
          {
            "id": "quest_profile",
            "title": "Параметры квеста",
            "body": "Тип: main. Сложность: hard. Формат: группа 2–4 игроков. Длительность: 8+ часов.\\nНаграды: 4 000 XP, природа +30, финансовые траты 50 едди на снаряжение и доступ.\\nРиск: травмы и возможная катастрофа при пробуждении вулкана.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "stages",
            "title": "Этапы восхождения",
            "body": "1. Добраться до Mount Rainier National Park из Сиэтла, подготовив снаряжение и разрешения.\\n2. Пройти область Paradise с лугами и обзорными площадками, получить первое впечатление от ледников.\\n3. Следовать по Skyline Trail, лавируя между ледяными участками и трещинами.\\n4. Взобраться на снежную вершину, преодолевая погодные угрозы и отслеживая сейсмику.\\n5. Совершить безопасный спуск и зафиксировать панораму города на горизонте.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional_details",
            "title": "Региональный контекст",
            "body": "Mount Rainier — символ горизонта Сиэтла и активный стратовулкан высотой 4 392 м.\\nЛедники и вечный снег соседствуют с цветущими лугами Paradise и напоминают о потенциале извержения, угрожающем мегаполису.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "systems_hooks",
            "title": "Системные связи",
            "body": "Квест подключает traversal-system, систему погодных событий и эвакуирует данные об опасности вулканов.\\nУспех активирует цепочки для службы гражданской обороны и природных фракций Сиэтла.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 1063,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "2.0.0", "date": "2025-12-06", "author": "content_writer", "changes": "Обновлен формат rewards, исправлен github_issue на 1063, статус изменен на draft."},
        {"version": "2.0.0", "date": "2025-11-11", "author": "concept_director", "changes": "Конвертирован квест «Гора Рейнир» в YAML и заданы этапы экспедиции и угрозы вулкана."}
      ],
      "validation": {"checksum": "", "schema_version": "1.0"}
    }'::jsonb,
    2,
    true
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    quest_type = EXCLUDED.quest_type,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    requirements = EXCLUDED.requirements,
    objectives = EXCLUDED.objectives,
    rewards = EXCLUDED.rewards,
    branches = EXCLUDED.branches,
    content_data = EXCLUDED.content_data,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest

