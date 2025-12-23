-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\kampala-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.190903

BEGIN;

-- Lore: canon-lore-africa-kampala-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-africa-kampala-2020-2093',
        'Кампала — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-africa-kampala-2020-2093",
        "title": "Кампала — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:15:00+00:00",
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
          "africa",
          "kampala"
        ],
        "topics": [
          "timeline-author",
          "diplomacy"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-africa-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1297",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1297",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:15:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/kampala-2020-2093.md",
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
        "problem": "Кампала была описана в Markdown и не позволяла использовать озёрные и мобильные механики в общей базе знаний.",
        "goal": "Перенести ключевые эпохи озёрного мегаполиса в структурированный YAML, подчеркнув мобильные платежи, водные переговоры и партизанское наследие.",
        "essence": "Кампала превращает столицу холмов в узел мобильных финансов, озёрной логистики и экспорта «пакета озёр».",
        "key_points": [
          "Этапы от озёрной логистики до региональной столицы Восточной Африки.",
          "Акценты на М-песа 3.0, кибер-матату и био-кофе.",
          "Подготовлены хуки для сценариев о Великих Озёрах и партизанских протоколах."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Город холмов 2.0",
            "body": "- «Виктория-хабы»: озёрная логистика и дроны.\n- «Баджа-баджа AR»: мото-такси как распределённая сеть.\n- «Горные серверы»: охлаждение за счёт высоты.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Восточноафриканский технохаб",
            "body": "- «М-Песа 3.0»: эволюция мобильных денег и смарт-контрактов.\n- «Нильские переговоры»: дипломатия водных ресурсов.\n- «Кибер-матату»: автономный общественный транспорт.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Озёрная республика",
            "body": "- «Великие озёра альянс»: политическая интеграция региона.\n- «Био-кофе»: генетические эксперименты с растениями.\n- «Партизанские протоколы»: децентрализация и исторические уроки.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Региональный центр",
            "body": "- «Восточноафриканская столица»: укрепление политического веса.\n- «Культурный экспорт»: угандийская модель для Великих Озёр.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет озёр",
            "body": "- Экспорт протоколов озёрных городов для управления водными ресурсами.\n- Кампала как стандарт мобильных финансов и экологической дипломатии.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Виктория-хабы, баджа-баджа AR, М-Песа 3.0, био-кофе, партизанские протоколы.\n- Сюжеты о Нильских переговорах и мобильном транспорте.\n",
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
        "github_issue": 1297,
        "needs_task": false,
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
          "changes": "Конвертация авторских событий Кампалы в структурированный YAML."
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