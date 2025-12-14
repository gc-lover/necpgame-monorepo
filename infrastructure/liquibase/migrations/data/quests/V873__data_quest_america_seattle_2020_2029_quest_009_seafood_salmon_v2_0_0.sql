-- Issue: #1066
-- Import quest from: america\seattle\2020-2029\quest-009-seafood-salmon.yaml
-- Version: 2.0.0
-- Generated: 2025-12-08T23:05:00.000000

BEGIN;

-- Quest: canon-quest-seattle-seafood
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
    'canon-quest-seattle-seafood',
    'Сиэтл — Морепродукты и лосось',
    'Игрок пробует cedar plank salmon, краба Dungeness и clam chowder, чтобы прочувствовать дух северо-западного побережья.',
    'side',
    1,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"go_to_seafood_restaurant","text":"Отправиться в Pike Place Market или проверенный ресторан морепродуктов","type":"interact","target":"seafood_restaurant_visit","count":1,"optional":false},
      {"id":"order_pacific_dishes","text":"Заказать cedar plank salmon, краба Dungeness и региональный clam chowder","type":"interact","target":"pacific_seafood_order","count":1,"optional":false},
      {"id":"evaluate_freshness","text":"Оценить свежесть блюд и обсудить с шефом происхождение продуктов","type":"interact","target":"seafood_freshness_evaluation","count":1,"optional":false},
      {"id":"create_gastronomic_notes","text":"Сделать заметки для гастрономического гида и поделиться рекомендациями","type":"interact","target":"gastronomic_guide_notes","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1000,"money":-50,"reputation":{"culinary":15},"items":[],"unlocks":{"achievements":["Лососевый гурман"],"flags":["seattle_seafood_tasted"],"buffs":[{"id":"hp_boost","value":"+30% HP","duration_hours":10}]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-seattle-seafood",
        "title": "Сиэтл — Морепродукты и лосось",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "2.0.0",
        "last_updated": "2025-12-06T11:40:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "",
        "owners": [{"role": "narrative_team", "contact": "narrative@necp.game"}],
        "tags": ["seattle", "cuisine", "seafood"],
        "topics": ["regional-content", "gastronomy"],
        "related_systems": ["food-system", "buff-system"],
        "related_documents": [
          {"id": "github-issue-369", "title": "GitHub Issue #369", "link": "https://github.com/gc-lover/necpgame-monorepo/issues/369", "relation": "migrated_to", "migrated_at": "2025-11-22T21:30:00Z"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/america/seattle/2020-2029/quest-009-seafood-salmon.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "quest-design"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "narrative_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Гастрономическая визитная карточка Сиэтла не была формализована и не учитывала баффы от локальных блюд.",
        "goal": "Перенести дегустационный квест в YAML, акцентируя связь города с океаном и кухней.",
        "essence": "Игрок пробует лосося на кедровой доске, краба Dungeness и clam chowder, чтобы прочувствовать дух северо-западного побережья.",
        "key_points": [
          "Квест-id SEATTLE-2029-009, лёгкая сложность, соло, длительность 1–2 часа.",
          "Включает посещение Pike Place Market или топового ресторана морепродуктов, знакомит с cedar plank salmon и региональными блюдами.",
          "Награда увеличивает здоровье и предоставляет достижение «Лососевый гурман»."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "quest_profile",
            "title": "Параметры квеста",
            "body": "Тип: social. Сложность: easy. Формат: solo. Длительность: 1–2 часа.\\nНаграды: 1 000 XP, +30% HP на 10 часов, траты 50 едди на дегустацию.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "stages",
            "title": "Этапы дегустации",
            "body": "1. Отправиться в Pike Place Market или проверенный ресторан морепродуктов.\\n2. Заказать cedar plank salmon, краба Dungeness и региональный clam chowder.\\n3. Оценить свежесть блюд и обсудить с шефом происхождение продуктов.\\n4. Сделать заметки для гастрономического гида и поделиться рекомендациями.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional_details",
            "title": "Региональный контекст",
            "body": "Тихоокеанский лосось, поставки из Аляски и Пьюджет-Саунд, краб Dungeness и аромат кедровых досок формируют вкусовой профиль.\\nРыбный рынок и рестораны определяют бытовую культуру Сиэтла.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "systems_hooks",
            "title": "Системные связи",
            "body": "Квест интегрирует food-system, временные баффы и социальные бонусы для локальных фракций.\\nРасширяет гастрономическую ветку города и открывает дополнительные рецепты.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 1066,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "2.0.0", "date": "2025-12-06", "author": "content_writer", "changes": "Обновлен формат rewards, исправлен github_issue на 1066, статус изменен на draft."},
        {"version": "2.0.0", "date": "2025-11-11", "author": "concept_director", "changes": "Конвертирован квест «Морепродукты и лосось» в YAML с баффами и локальной кухней."}
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















