-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\barcelona-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.491371

BEGIN;

-- Lore: canon-lore-europe-barcelona-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-barcelona-2020-2093',
        'Барселона — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-barcelona-2020-2093",
        "title": "Барселона — авторские события 2020–2093",
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
          "barcelona"
        ],
        "topics": [
          "timeline-author",
          "creativity"
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
        "source": "shared/docs/knowledge/canon/lore/timeline-author/regions/europe/cities/barcelona-2020-2093.md",
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
        "problem": "Барселона в Markdown не связывала гауди-спирали, каталонский протокол и биолюминесцентный порт с системными хуками.",
        "goal": "Перевести хронологию Барселоны в YAML, подчеркнув автономный дух, креативные кварталы и экспорт «пакета творчества».",
        "essence": "Барселона объединяет гауди-спирали, Рамбла-нейрон и каталонский протокол, формируя средиземноморский центр креативной автономии.",
        "key_points": [
          "Этапы от медитеранской жемчужины до экспортёра креативных протоколов.",
          {
            "Хуки": "гауди-спирали, каталонский протокол, медитеран-линк, креативные кварталы, биолюминесцентный порт."
          },
          "Сюжеты о культурных фестивалях, энергетической автономии и спортивных модификациях."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Медитеранская жемчужина",
            "body": "- «Гауди-спирали»: био-архитектура и городские системы.\n- «Рамбла-нейрон»: уличная культура BD-перформансов.\n- «Порт-Каталония»: морской хаб оффлайн-пакетов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Автономный дух",
            "body": "- «Каталонский протокол»: независимая сетевая инфраструктура.\n- «Солнечные террасы»: энергетическая автономия.\n- «Футбол+»: киберспортивные модификации классического футбола.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Средиземноморская сеть",
            "body": "- «Медитеран-линк»: оффлайн-маршруты побережья.\n- «Креативные кварталы»: экспериментальные режимы управления.\n- «Биолюминесцентный порт»: био-освещение набережной.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Культурный экспорт",
            "body": "- «Барселонский стиль»: архитектурная эстетика в мировых параметрах.\n- «Средиземноморский альянс»: союз прибрежных городов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет творчества",
            "body": "- Экспорт креативных протоколов, био-архитектуры и культурных программ.\n- Барселона закрепляется как столица средиземноморской автономии.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Гауди-спирали, каталонский протокол, медитеран-линк, креативные кварталы, биолюминесцентный порт.\n- Сюжеты об энергетической автономии, культурном экспорте и спортивных инновациях.\n",
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
        "github_issue": 1252,
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
          "changes": "Конвертация авторских событий Барселоны в структурированный YAML."
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