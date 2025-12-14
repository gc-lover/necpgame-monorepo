-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\montevideo-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.546111

BEGIN;

-- Lore: canon-lore-america-montevideo-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-montevideo-2020-2093',
    'Монтевидео — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-montevideo-2020-2093",
    "title": "Монтевидео — авторские события 2020–2093",
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
      "america",
      "montevideo"
    ],
    "topics": [
      "timeline-author",
      "governance"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/montevideo-2020-2093.md",
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
    "problem": "Описание Монтевидео в Markdown не связывало социальные инновации, антарктические права и цифровую демократию.",
    "goal": "Сформировать карту развития маленькой столицы как лаборатории прогрессивного управления.",
    "essence": "Монтевидео превращает соцгарантии 2.0, нейро-референдумы и антарктические инициативы в глобальный «пакет свободы».",
    "key_points": [
      "Этапы от тихой столицы до экспортёра прогрессивных протоколов.",
      {
        "Хуки": "универсальный базовый имплант, прямые референдумы, био-побережье, уругвайская модель."
      },
      "Созданы опорные точки для сюжетов о социальном эксперименте, Антарктике и кооперации с Буэнос-Айресом."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Тихая столица",
        "body": "- «Рамбла неон»: культурная набережная.\n- «Свободные порты»: либеральная экономика.\n- «Маленькая столица, большие идеи»: прогрессивные протоколы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Южный эксперимент",
        "body": "- «Социальные гарантии 2.0»: универсальный базовый имплант.\n- «Плайя-купола»: защищённые пляжи.\n- «Ла-Плата коридор»: связь с Буэнос-Айресом.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Лаборатория демократии",
        "body": "- «Прямые референдумы»: нейро-голосования.\n- «Антарктические права»: борьба за южный континент.\n- «Био-побережье»: очистка и восстановление залива.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Прогрессивный центр",
        "body": "- «Уругвайская модель»: экспорт соцполитики.\n- «Культура мате BD»: традиции в цифровом формате.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет свободы",
        "body": "- Экспорт протоколов прогрессивного управления и антарктических инициатив.\n- Монтевидео становится глобальным центром демократических инноваций.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Социальные гарантии 2.0, прямые референдумы, антарктические права, уругвайская модель, био-побережье.\n- Сюжеты о цифровой демократии, антарктических переговорах и коридоре Ла-Плата.\n",
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
    "github_issue": 1285,
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
      "changes": "Конвертация авторских событий Монтевидео в структурированный YAML."
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