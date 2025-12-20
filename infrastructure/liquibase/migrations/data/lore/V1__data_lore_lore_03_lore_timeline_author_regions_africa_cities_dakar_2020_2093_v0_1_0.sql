-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\dakar-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.174678

BEGIN;

-- Lore: canon-lore-africa-dakar-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-dakar-2020-2093',
    'Дакар — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-dakar-2020-2093",
    "title": "Дакар — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:15:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "narrative_team",
        "contact": "narrative@necp.game"
      }
    ],
    "tags": [
      "regions",
      "africa",
      "dakar"
    ],
    "topics": [
      "timeline-author",
      "logistics"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-africa-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1300",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1300",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:15:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/dakar-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "worldbuilding",
      "live_ops"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "narrative_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Описание Дакара оставалось в Markdown и не связывало трансатлантическую логистику с культурными арками Сенегала.",
    "goal": "Формализовать эпохи Дакара как пустынно-атлантического узла с защитой от штормов и песчаных бурь.",
    "essence": "Дакар укрепляет пустынные и морские протоколы, соединяя франкофонный мир и Западную Африку, готовя «пакет пустынь».",
    "key_points": [
      "Расписаны этапы от атлантического форпоста до экспорта пустынных стандартов.",
      "Подчёркнуты песчаные щиты, подводные кабели и музыкальный брендинг.",
      "Добавлена связка с транс-сахарскими маршрутами и энергетикой харматтана."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Атлантический форпост",
        "body": "- «Даже раллоти-хабы»: морская логистика и автономные причалы.\n- «Франко-африканский мост»: договоры с Европой.\n- «Пустынные ветра»: энергетика харматтана.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Западноафриканские ворота",
        "body": "- «Транс-сахарский путь»: укрепление связи с Северной Африкой.\n- «Океанские порты»: автономный транзит грузов.\n- «Мбалакс BD»: цифровой экспорт музыкальной сцены.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Сахельская крепость",
        "body": "- «Песчаные щиты»: инфраструктура защиты данных во время бурь.\n- «Подводные кабели Атлантики»: трансатлантическая связь.\n- «Солнечные пустыни»: энергетические фермы Сахеля.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Франкофонный центр",
        "body": "- «Западноафриканский альянс»: франкофонная интеграция.\n- «Культурный экспорт»: сенегальская модель сотрудничества.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет пустынь",
        "body": "- Экспорт протоколов пустынного побережья для мегаполисов.\n- Тиражирование песчаных щитов и музыкальных медиа как мягкой силы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Песчаные щиты, подводные кабели, мбалакс BD, франкофонный альянс.\n- Сценарии о транс-сахарских караванах и энергетике харматтана.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 1300,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Конвертация авторских событий Дакара в структурированный YAML."
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