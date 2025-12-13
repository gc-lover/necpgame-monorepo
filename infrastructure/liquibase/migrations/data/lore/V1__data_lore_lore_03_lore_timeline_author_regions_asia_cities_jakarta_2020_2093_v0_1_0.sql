-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\jakarta-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.492330

BEGIN;

-- Lore: canon-lore-asia-jakarta-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-jakarta-2020-2093',
    'Джакарта — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-jakarta-2020-2093",
    "title": "Джакарта — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T00:00:00+00:00",
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
      "asia",
      "jakarta"
    ],
    "topics": [
      "timeline-author",
      "climate"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-oceania-2020-2093",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/jakarta-2020-2093.md",
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
    "problem": "История Джакарты оставалась в Markdown и не раскрывала климатические и архипелажные механики.",
    "goal": "Сформировать полную картину трансформации Джакарты от тонущего мегаполиса до архипелаг-федерации.",
    "essence": "Джакарта строит искусственные острова, переносит столицу и экспортирует «пакет архипелага».",
    "key_points": [
      "Этапы от затопления 2027 до глобального управления островными системами.",
      {
        "Хуки": "1000 островов 2.0, Нусантара-сити, пиратские своды, коралловые серверы."
      },
      "Готовые точки входа для сюжетов об эвакуации, биоджунглях и подводных фабриках."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Тонущий мегаполис",
        "body": "- «Затопление 2027»: критическая точка затопления.\n- «Эвакуационные планы»: масштабное переселение.\n- «Старая Батавия»: исторический центр под куполом.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Архипелаг-сеть",
        "body": "- «1000 островов 2.0»: сеть искусственных островов.\n- «АСЕАН-хаб»: политический центр ЮВА.\n- «Подводные фабрики»: производство под водой.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Новая столица",
        "body": "- «Нусантара-сити»: киберпанк столица в джунглях.\n- «Двойной город»: старая и новая Джакарта связаны.\n- «Био-джунгли»: интеграция города с тропическим лесом.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Архипелаг-федерация",
        "body": "- «Морские дороги»: подводные туннели между островами.\n- «Пиратские своды»: легализованные морские кланы.\n- «Коралловые серверы»: дата-центры в рифах.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет архипелага",
        "body": "- Экспорт протоколов управления островными системами.\n- Джакарта становится эталоном климатической адаптации.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Затопление 2027, 1000 островов 2.0, Нусантара-сити, пиратские своды, коралловые серверы.\n- Сюжеты об эвакуации, подводных фабриках и биоджунглях.\n",
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
    "github_issue": 1270,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "concept_director",
      "changes": "Конвертация авторских событий Джакарты в структурированный YAML."
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