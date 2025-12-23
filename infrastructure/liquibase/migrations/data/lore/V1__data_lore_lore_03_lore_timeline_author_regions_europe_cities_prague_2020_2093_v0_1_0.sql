-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\prague-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.560348

BEGIN;

-- Lore: canon-lore-europe-prague-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-prague-2020-2093',
        'Прага — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-prague-2020-2093",
        "title": "Прага — авторские события 2020–2093",
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
          "prague"
        ],
        "topics": [
          "timeline-author",
          "heritage"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/prague-2020-2093.md",
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
        "problem": "Прага в Markdown не объединяла V4-протоколы, купол истории и роль города как культурного архива.",
        "goal": "Оцифровать путь Праги к статусу центральноевропейского узла по сохранению наследия.",
        "essence": "Прага сочетает Карлов мост-ретранслятор, подземные лаборатории и музей памяти, экспортируя «пакет культуры».",
        "key_points": [
          "Этапы от золотого города 2.0 до культурного капитала и экспорта протоколов сохранения.",
          {
            "Хуки": "Карлов мост, Вышеград протоколы, купол истории, Карловы серверы, музей памяти."
          },
          "Сюжеты о V4-альянсе, катакомбах и AR-пластах исторического центра."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Золотой город 2.0",
            "body": "- «Карлов мост ретранслятор»: исторический узел связи.\n- «Пражский град архивы»: госхранилища данных.\n- «Влтава-хабы»: речные оффлайн-маршруты.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Центральноевропейский узел",
            "body": "- «Вышеград протоколы»: стандарты для V4.\n- «Старый город AR»: исторический центр с AR-слоями.\n- «Подземные лаборатории»: катакомбы для исследований.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Купол истории",
            "body": "- «Защита наследия»: купол над центром.\n- «Чешские стартапы»: технологический бум.\n- «Карловы серверы»: дата-центры университетов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Культурный капитал",
            "body": "- «Музей памяти»: BD-архивы европейской истории.\n- «V4 альянс»: технологическое объединение.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет культуры",
            "body": "- Экспорт протоколов сохранения наследия и культурного обмена.\n- Прага закрепляется как европейский центр памяти.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Карлов мост, Вышеград протоколы, купол истории, Карловы серверы, музей памяти.\n- Сюжеты о катакомбах, сохранении памяти и V4-альянсе.\n",
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
        "github_issue": 1248,
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
          "changes": "Конвертация авторских событий Праги в структурированный YAML."
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