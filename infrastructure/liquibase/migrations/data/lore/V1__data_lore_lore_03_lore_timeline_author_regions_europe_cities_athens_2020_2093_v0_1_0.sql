-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\athens-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.487126

BEGIN;

-- Lore: canon-lore-europe-athens-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-athens-2020-2093',
    'Афины — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-athens-2020-2093",
    "title": "Афины — авторские события 2020–2093",
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
      "europe",
      "athens"
    ],
    "topics": [
      "timeline-author",
      "philosophy"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/timeline-author/regions/europe/cities/athens-2020-2093.md",
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
    "problem": "Афины в Markdown не связывали цифровой Акрополь, философские DAO и олимпийские киберигры.",
    "goal": "Отразить в YAML путь Афин от колыбели демократии 2.0 до экспортёра «пакета мудрости».",
    "essence": "Афины объединяют Акрополь AR, философские DAO и олимпийские киберигры, формируя средиземноморский пакет управления.",
    "key_points": [
      "Этапы от колыбели демократии 2.0 до философского центра и экспорта протоколов управления.",
      {
        "Хуки": "Акрополь AR, философские DAO, эгейские маршруты, античные серверы, академия Платона 2.0."
      },
      "Сюжеты о энергетической автономии, островной федерации и олимпийских кибериграх."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Колыбель демократии 2.0",
        "body": "- «Акрополь AR»: цифровые слои древних руин.\n- «Пирей порт»: морские оффлайн-хабы.\n- «Кризис и восстановление»: экономический ренессанс.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Средиземноморский хаб",
        "body": "- «Греческие острова сеть»: распределённая инфраструктура.\n- «Философские DAO»: цифровые агоры.\n- «Солнечная революция»: энергетическая независимость.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Островная федерация",
        "body": "- «Эгейские маршруты»: оффлайн-пакеты между островами.\n- «Античные серверы»: дата-центры в древних храмах.\n- «Олимпийские киберигры»: возрождение традиций.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Философский центр",
        "body": "- «Академия Платона 2.0»: центр киберфилософии.\n- «Средиземноморский альянс»: объединение южных городов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет мудрости",
        "body": "- Экспорт философских протоколов управления и образовательных программ.\n- Афины закрепляются как духовный и технологический центр Средиземноморья.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Акрополь AR, философские DAO, эгейские маршруты, античные серверы, академия Платона 2.0.\n- Сюжеты о демократии 2.0, энергетической автономии и олимпийских кибериграх.\n",
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
    "github_issue": 1253,
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
      "changes": "Конвертация авторских событий Афин в структурированный YAML."
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