-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\oceania-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.679568

BEGIN;

-- Lore: canon-region-oceania-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-oceania-2020-2093',
        'Океания 2020-2093 — Авторская хронология',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-oceania-2020-2093",
        "title": "Океания 2020-2093 — Авторская хронология",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-11T23:20:00+00:00",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [
          {
            "role": "lore_analyst",
            "contact": "lore@necp.game"
          }
        ],
        "tags": [
          "oceania",
          "oceanic-federation",
          "climate"
        ],
        "topics": [
          "regional-history",
          "climate-adaptation"
        ],
        "related_systems": [
          "narrative-service",
          "world-state"
        ],
        "related_documents": [
          {
            "id": "timeline-author-2045-2060-author-events",
            "relation": "references"
          },
          {
            "id": "timeline-author-regions-asia-2020-2093",
            "relation": "complements"
          },
          {
            "id": "timeline-author-factions-nomads-2020-2093",
            "relation": "intersects"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/oceania-2020-2093.md",
        "visibility": "internal",
        "audience": [
          "lore",
          "narrative",
          "systems-design"
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
        "problem": "Хронология Океании находилась в Markdown и не связывалась с системами био-океанического сеттинга.",
        "goal": "Структурировать опорные эпохи региона, подчеркнув морские федерации и климатические механики.",
        "essence": "Океанические государства проходят путь от эко-протоколов до федерации и экспорта био-процессоров глубин.",
        "key_points": [
          "Задокументированы пять эпох, связывающих экологию, политику и технологии океана.",
          "Выделены хуки для плавучих городов, подводных хабов и плавучей дипломатии.",
          "Обеспечена база для квестов защиты инфраструктуры и миссий климат-переговоров."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Островные государства и эко-протоколы",
            "body": "Сидней формирует био-купол над гаванью, а Окленд запускает геотермальные серверные фермы.\nМикронезия строит распределённые островные хабы, параллельно с внедрением коралловых протоколов защиты океана.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Морские города и климат-беженцы",
            "body": "Появляются автономные плавучие города и сеть подводных кабелей с оффлайн-дронами.\nКвинсленд развивает био-зоны, а Тихоокеанский пакт объединяет островные государства для координации.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+ подводные хабы",
            "body": "На океанском дне строятся защищённые дата-центры и органические фермы коралловых процессоров.\nМаори-сети закрепляют культурные протоколы, а риф-патрули обеспечивают безопасность инфраструктуры.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Океанический федерализм",
            "body": "Формируется Федерация Тихого океана и голубая экономика, делающая морские ресурсы основой благосостояния.\nБио-слияние через специализированные импланты и штормовые убежища поддерживают жизнь на воде.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Океанические архивы и био-наследие",
            "body": "На дне океана создаются архивы глубин и био-память из коралловых структур.\nЭкспортируются протоколы устойчивости, включая планктон-процессоры нового поколения.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Плавучие города, подводные хабы и коралловые фермы служат площадками для квестов защиты и переговоров.\nГолубая экономика и био-слияние подпитывают сюжетные линии спасения, дипломатии и экспорта технологий.\n",
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
        "github_issue": 73,
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
          "changes": "Конвертирована авторская хроника Океании в структурированный YAML."
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