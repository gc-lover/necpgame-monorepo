-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\2020-2030-main-story.yaml
-- Generated: 2025-12-14T16:03:08.945544

BEGIN;

-- Lore: main-story-2020-2030
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2020-2030',
    'Основной сюжет — 2020–2030',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2020-2030",
    "title": "Основной сюжет — 2020–2030",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-09T11:23:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "github_issue_number": 133,
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "main-story",
      "blackwall",
      "corporate-war"
    ],
    "topics": [
      "timeline_author",
      "story-arc"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "event-service"
    ],
    "related_documents": [
      {
        "id": "main-story-framework",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/2020-2030-main-story.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "liveops"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "concept_director",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Каркас основного сюжета 2020–2030 существовал в Markdown и не был связан с механикой навыков и таймлайном Blackwall.",
    "goal": "Структурировать первую фазу сюжетной кампании, чтобы задать акты, ветвления по происхождениям/классам и ориентиры по тестам навыков.",
    "essence": "Арка описывает переход от DataKrash к становлению Blackwall, вводит ключевые фракции и определяет пороги навыков для хакерских, социальных и боевых испытаний.",
    "key_points": [
      "Четыре акта, раскрывающие угрозу Blackwall и раннюю экономику имплантов.",
      "Ветвления по происхождению (Street Kid, Corpo, Nomad) и классам (Netrunner, Techie, Solo).",
      "Механика DAO и экстракт-лиг как социальный и боевой драйверы.",
      "Настройка порогов навыков 0.60–0.70 для ключевых испытаний."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "context",
        "title": "Контекст и фон",
        "body": "Четвёртая корпоративная война и DataKrash формируют хаос начала 2020-х. Blackwall зарождается в 2023–2025, корпорации\nвыстраивают прокси-силы, а мегаполисы, включая Найт-Сити и Атланту, превращаются в автономные узлы. Этот период задаёт\nоснову для новой борьбы за сетевой контроль и восстановление инфраструктуры.\n",
        "mechanics_links": []
      },
      {
        "id": "act_structure",
        "title": "Структура актов",
        "body": "- **Акт I — Шумы за чёрным заслоном:** узлы «Триак» и «Слепые зоны МАКСТАК» раскрывают первые сбои Blackwall.\n  Происхождения Street Kid, Corpo и Nomad дают уникальные маршруты обхода сенсоров.\n- **Акт II — Экономика имплантов 2.0:** квесты «Гильдии рипдоков» и «Сертификат человечности» балансируют прибыль и риск\n  киберпсихоза. Классы Netrunner, Techie и Solo получают отдельные цепочки.\n- **Акт III — Социальные шифт-протоколы:** DAO и неоновые коридоры экстракт-лиги противопоставляют NetWatch и чёрные сети.\n- **Акт IV — Развязка эпохи:** решение «Чистого канала» определяет отношение героев к NetWatch и задаёт последствия для 2030–2045.\n",
        "mechanics_links": []
      },
      {
        "id": "skill_thresholds",
        "title": "Пороговые значения навыков",
        "body": "- Хакерские испытания: 0.62 → 0.70 в зависимости от прогресса мировых событий 2000–2040.\n- Социальные проверки DAO: 0.60–0.68.\n- Боевые и стелс проверки экстракт-лиги: 0.62–0.70.\n",
        "mechanics_links": []
      },
      {
        "id": "dependencies",
        "title": "Связанные материалы",
        "body": "- Таймлайн восстановления: `../../_03-lore/timeline/2020-2040-destruction-recovery.yaml`\n- Авторские события: `../../../lore/_03-lore/timeline-author/2020-2030-author-events.yaml`\n- World events 2020–2040: `../../../02-gameplay/world/events/world-events-2020-2040.yaml`\n- Каркас кампании: `framework.yaml`\n",
        "mechanics_links": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 133,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Конвертация сюжета 2020–2030 в структурированный YAML."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    0
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;