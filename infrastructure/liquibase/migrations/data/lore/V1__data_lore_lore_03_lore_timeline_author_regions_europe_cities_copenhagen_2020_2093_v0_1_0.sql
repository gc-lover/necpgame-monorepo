-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\copenhagen-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.521321

BEGIN;

-- Lore: canon-region-europe-copenhagen-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-europe-copenhagen-2020-2093',
        'Копенгаген 2020-2093 — Пакет благополучия',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-europe-copenhagen-2020-2093",
        "title": "Копенгаген 2020-2093 — Пакет благополучия",
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
          "copenhagen",
          "wellbeing"
        ],
        "topics": [
          "regional-history",
          "sustainable-city"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/copenhagen-2020-2093.md",
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
        "problem": "Хронология Копенгагена была в Markdown и не описывала кибер-велодороги, хюгге-протоколы и островные туннели.",
        "goal": "Перевести историю города в YAML, выделив нордические программы благополучия и экспорт устойчивого счастья.",
        "essence": "Копенгаген превращается в нордический центр благополучия, где кибер-велодороги, хюгге-протоколы и подводные туннели формируют «пакет благополучия».",
        "key_points": [
          "Пять эпох связывают велосети, DAO демократии и балтийские коридоры с экспортом стандарта благополучия.",
          "Акцентированы оффшорные ветровые фермы, подводные туннели и хюгге-протоколы.",
          "Подготовлены хуки для сцен устойчивого транспорта, дипломатии и экологических проектов."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Велосипедный город 3.0",
            "body": "«Кибер-Велодороги» обеспечивают автономные велосети с импланто-навигацией.\n«Эресунн-Мост Данные» связывает Данию и Швецию, «Зелёная Столица» удерживает нулевые выбросы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Нордическая гармония",
            "body": "«Хюгге-Протоколы» оцифровывают благополучие.\n«Ветровые Фермы Моря» дают оффшорную энергию, «Копенгаген-DAO» проводит прямую демократию.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: островной узел",
            "body": "«Балтийские Коридоры» связывают города Скандинавии.\n«Подводные Туннели» соединяют Данию, Швецию и Германию, «Био-Гавань» очищает море технологиями.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Нордический центр",
            "body": "«Нордический Стандарт» экспортирует социальную модель.\n«Культура Хюгге» становится глобальным продуктом благополучия.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет благополучия",
            "body": "Город экспортирует протоколы устойчивого счастья и экологического управления.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Кибер-велодороги, хюгге-протоколы, подводные туннели, оффшорные фермы и DAO демократия создают сценарии благополучия, транспорта и дипломатии.\n",
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
          "changes": "Конвертирована хронология Копенгагена в YAML и выделены механики устойчивого благополучия."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        0) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;