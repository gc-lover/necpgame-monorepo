-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\oslo-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.795015

BEGIN;

-- Lore: canon-region-europe-oslo-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-oslo-2020-2093',
    'Осло 2020-2093 — Пакет фьордов',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-oslo-2020-2093",
    "title": "Осло 2020-2093 — Пакет фьордов",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T02:05:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "europe",
      "oslo",
      "sustainability"
    ],
    "topics": [
      "regional-history",
      "arctic-governance"
    ],
    "related_systems": [
      "narrative-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/oslo-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "lore_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Хронология Осло в Markdown не учитывала фьорд-серверы и арктические стандарты в структуре знаний.",
    "goal": "Конвертировать эпохи города в YAML, подчеркнув переход к зелёной энергии и экспорт устойчивости.",
    "essence": "Осло превращается в арктический хаб, где фьорд-серверы, ледовые щиты и нулевые выбросы формируют «пакет фьордов» для региона.",
    "key_points": [
      "Пять эпох описывают путь от нордического стандарта до экспорта арктической устойчивости.",
      "Выделены фьорд-серверы, лёд-щиты, подземные галереи и гидро-сети.",
      "Подготовлены хуки для сюжетов энергоперехода, арктической логистики и культурного экспорта."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Нордический стандарт",
        "body": "«Фьорд-Серверы» используют охлаждение водой.\n«Нефтегаз 2.0» запускает переход к зелёной энергии, «Социальные Импланты» поддерживают госпрограммы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Арктический хаб",
        "body": "«Северный Путь» управляет логистикой и кабельными трассами.\n«Полярные Станции» поддерживают научные базы, «Лёд-Щиты» защищают берег.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: устойчивый город",
        "body": "«Нулевые Выбросы» фиксируют полную декарбонизацию.\n«Подземные Галереи» создают климатические убежища, «Гидро-Сети» развивают водную энергию.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Нордический союз",
        "body": "«Нормативный Экспорт» распространяет социальные стандарты.\n«Культурный Экспорт» популяризирует северный неон и устойчивый образ жизни.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет фьордов",
        "body": "Город экспортирует протоколы арктической устойчивости, энергосетей и береговой защиты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Фьорд-серверы, ледовые щиты, нулевые выбросы, подземные галереи и гидро-сети обеспечивают сценарии энергоперехода и арктических операций.\n",
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
    "needs_task": false,
    "github_issue": 71,
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
      "changes": "Конвертирована хронология Осло в YAML и выделены механики арктической устойчивости."
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