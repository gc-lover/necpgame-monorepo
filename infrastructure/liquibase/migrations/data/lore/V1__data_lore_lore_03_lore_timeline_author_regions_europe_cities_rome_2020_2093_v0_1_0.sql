-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\rome-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.569312

BEGIN;

-- Lore: canon-region-europe-rome-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-europe-rome-2020-2093',
        'Рим 2020-2093 — Пакет вечности',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-europe-rome-2020-2093",
        "title": "Рим 2020-2093 — Пакет вечности",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T01:45:00+00:00",
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
          "rome",
          "cultural-preservation"
        ],
        "topics": [
          "regional-history",
          "heritage"
        ],
        "related_systems": [
          "narrative-service",
          "world-state"
        ],
        "related_documents": [
          {
            "id": "canon-region-europe-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/rome-2020-2093.md",
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
        "problem": "Хронология Рима находилась в Markdown и не подключалась к системам управления культурным наследием.",
        "goal": "Структурировать эпохи Рима в YAML, подчеркнув цифровые архивы, реставрацию и экспорт протоколов «вечного города».",
        "essence": "Рим превращается в глобальный музей человечества, совмещая древние памятники и высокие технологии защиты.",
        "key_points": [
          "Выделены пять эпох от «Вечного города 2.0» до глобального пакета сохранения наследия.",
          "Отмечены механики Колизей-Арены, катакомб-серверов и трибуналов наследия.",
          "Подготовлены хуки для сценариев культуры, права и живых событий."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Вечный город 2.0",
            "body": "«Колизей-Арена» становится киберспортивной площадкой мирового уровня.\n«Ватикан Архивы» переводят религиозные фонды в цифровой формат, «Тибр-Мосты» формируют оффлайн-хабы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Наследие и технологии",
            "body": "«Консервация AR» разворачивает слои дополненной реальности на памятниках.\n«Италийский Союз» объединяет средиземноморские города, «Подземные Катакомбы» превращаются в серверные фермы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: музей человечества",
            "body": "«Архивы Цивилизации» создают глобальное хранилище наследия.\n«Био-Реставрация» применяет биотехнологии для восстановления памятников, «Вечный Купол» защищает исторический центр.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Культурный капитал",
            "body": "«Экспорт культуры» распространяет римскую эстетику в мировых протоколах.\n«Трибуналы наследия» решают споры о культурной собственности.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет вечности",
            "body": "Город экспортирует протоколы сохранения культурного наследия и образовательные пакеты.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Колизей-Арена, архивы цивилизации, катакомбы-серверы, био-реставрация и трибуналы наследия создают сценарии защиты наследия и культурной дипломатии.\n",
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
          "changes": "Конвертирована авторская хронология Рима в YAML и выделены ключевые механики сохранения наследия."
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