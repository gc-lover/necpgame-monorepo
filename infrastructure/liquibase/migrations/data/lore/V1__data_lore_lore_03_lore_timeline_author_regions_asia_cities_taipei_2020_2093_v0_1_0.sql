-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\taipei-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.668581

BEGIN;

-- Lore: canon-region-asia-taipei-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-asia-taipei-2020-2093',
    'Тайбэй 2020-2093 — Кремниевый остров',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-asia-taipei-2020-2093",
    "title": "Тайбэй 2020-2093 — Кремниевый остров",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T01:12:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "asia",
      "taipei",
      "democracy"
    ],
    "topics": [
      "regional-history",
      "geopolitical-tension"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/taipei-2020-2093.md",
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
    "problem": "Авторская хронология Тайбэя находилась в Markdown и не была интегрирована в базу знаний.",
    "goal": "Структурировать эпохи Тайбэя с учётом чип-дипломатии, островной обороны и демократических протоколов.",
    "essence": "Тайбэй проходит путь от кремниевой монополии TSMC до технологического арбитра, экспортирующего «Пакет свободы».",
    "key_points": [
      "Пять эпох фиксируют эволюцию от Кремниевого острова до нейтрального арбитра между блоками.",
      "Чип-дипломатия и оборонные системы выделены как ключевые механики давления на регион.",
      "Созданы хуки для сценариев проливного кризиса, подземных убежищ и экспортируемой демократии."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Кремниевый Остров",
        "body": "«TSMC Империя» монополизирует чипы для имплантов, укрепляя техноэкономику.\n«Тайбэй 101 Ретранслятор» становится ключевым узлом связи, а «Ночные Рынки 3.0» объединяют традиции и технологии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Напряжение и инновации",
        "body": "«Пролив Кризис» задаёт постоянную угрозу, вынуждая развивать «Подводные Кабели».\n«Демократические Протоколы» выступают цифровым противовесом давлению Пекина.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ Островная крепость",
        "body": "«Оборонные Системы» создают автоматизированную защиту, «Чип-Дипломатия» превращает технологии в оружие влияния.\n«Подземный Тайбэй» обеспечивает убежища и скрытое производство.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Технологический арбитр",
        "body": "Тайбэй обеспечивает нейтральный статус и выступает посредником между блоками.\nЭкспортирует демократическую модель управления технологическими платформами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет свободы",
        "body": "Остров экспортирует протоколы демократического управления и формирует блок союзников.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Империя TSMC, проливный кризис, чип-дипломатия, подземный Тайбэй и демократические протоколы создают сюжетные линии обороны и дипломатии.\n",
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
    "github_issue": 72,
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
      "changes": "Конвертирована хронология Тайбэя в YAML и структурированы ключевые эпохи и механики."
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