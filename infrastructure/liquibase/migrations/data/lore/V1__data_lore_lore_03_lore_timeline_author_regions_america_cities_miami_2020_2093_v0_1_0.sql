-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\miami-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.263016

BEGIN;

-- Lore: canon-lore-america-miami-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-america-miami-2020-2093',
        'Майами — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-america-miami-2020-2093",
        "title": "Майами — авторские события 2020–2093",
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
          "miami"
        ],
        "topics": [
          "timeline-author",
          "coastline"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/miami-2020-2093.md",
        "visibility": "internal",
        "audience": [
          "narrative",
          "worldbuilding",
          "live_ops"
        ],
        "risk_level": "high"
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
        "problem": "Таймлайн Майами оставался в Markdown и не связывал климатическую угрозу, карибскую дипломатию и офшорные финансы.",
        "goal": "Описать путь Майами от неонового курорта до карибской столицы плавучих протоколов.",
        "essence": "Майами строит дамбы, подводные кварталы и крипто-гавань, экспортируя «пакет воды».",
        "key_points": [
          "Этапы от тропического парадокса до жизни под водой и федерации островов.",
          {
            "Хуки": "морские дамбы, плавучий Майами, коралловые архитектуры, ураганные протоколы, криптовалютная гавань."
          },
          "Готово основание для квестов о климат-беженцах, карибском союзе и офшорных сетях."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Тропический парадокс",
            "body": "- «Майами-Бич неон»: премиальные импланты на набережной.\n- «Эверглейдс данные»: серверы в болотах.\n- «Карибская сеть»: офшорные дата-хабы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Затопление начинается",
            "body": "- «Морские дамбы»: защита от подъёма океана.\n- «Плавучий Майами»: первые кварталы на воде.\n- «Кубинско-американский мост»: политическое сближение.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Водный город",
            "body": "- «Подводные кварталы»: полноценная жизнь под водой.\n- «Коралловые архитектуры»: биологические конструкции.\n- «Ураганные протоколы»: выживание в экстремальных штормах.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Карибская столица",
            "body": "- «Карибский союз»: федерация островов и побережий.\n- «Криптовалютная гавань»: офшорный финансовый центр.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет воды",
            "body": "- Экспорт протоколов жизни на и под водой.\n- Майами становится глобальным центром водной инфраструктуры.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Морские дамбы, плавучий Майами, подводные кварталы, ураганные протоколы, криптовалютная гавань.\n- Сюжеты о климат-беженцах, карибской дипломатии и подводной экономике.\n",
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
        "github_issue": 1286,
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
          "changes": "Конвертация авторских событий Майами в структурированный YAML."
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