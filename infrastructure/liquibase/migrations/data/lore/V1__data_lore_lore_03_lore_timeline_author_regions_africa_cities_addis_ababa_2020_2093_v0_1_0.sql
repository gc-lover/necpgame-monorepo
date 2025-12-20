-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\addis-ababa-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.153684

BEGIN;

-- Lore: canon-lore-africa-addis-ababa-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-addis-ababa-2020-2093',
    'Аддис-Абеба — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-addis-ababa-2020-2093",
    "title": "Аддис-Абеба — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:10:00+00:00",
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
      "addis-ababa"
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
        "id": "github-issue-1303",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1303",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:10:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/addis-ababa-2020-2093.md",
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
    "problem": "Хронология Аддис-Абебы хранилась в Markdown и не раскрывала дипломатические и панафриканские хуки в общей системе знаний.",
    "goal": "Формализовать эпохи столицы Африканского союза, выделив высотные инфраструктуры, религиозные архивы и переговорные сценарии.",
    "essence": "Высокогорная столица объединяет религиозные серверы, водные переговоры и панафриканские суды, экспортируя протоколы единства.",
    "key_points": [
      "Структурированы этапы от дипломатической столицы до пакета единства.",
      "Зафиксированы хуки про штаб-квартиру АС, высокогорные серверы и нильские переговоры.",
      "Сформирована основа для сценариев по федерации Рога Африки и континентальным арбитражам."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Дипломатическая столица",
        "body": "- «Штаб-квартира АС»: Африканский Союз 2.0 и обновлённые протоколы.\n- «Высокогорные серверы»: дата-центры на плато для охлаждения и защиты.\n- «Кофейные протоколы»: культурный экспорт через цифровые каналы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Рог Африки",
        "body": "- «Эфиопско-кенийский коридор»: технологическая ось развития региона.\n- «Православные серверы»: религиозные архивы и безопасность данных.\n- «Нильские переговоры»: дипломатия водных ресурсов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Горная крепость",
        "body": "- «Высотная защита»: использование рельефа для обороны.\n- «Федерация Рога»: политическое объединение стран региона.\n- «Древние церкви AR»: смешение традиций и технологий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Панафриканский центр",
        "body": "- «Континентальные суды»: арбитраж и международные договорённости.\n- «Культурный синтез»: интеграция традиций и технологических платформ.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет единства",
        "body": "- Экспорт протоколов панафриканского управления и арбитража.\n- Аддис-Абеба как образец континентального центра переговоров.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Штаб-квартира АС, высокогорные серверы, нильские переговоры, федерация Рога.\n- Сюжеты о континентальных судах и культурном синтезе.\n",
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
    "github_issue": 1303,
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
      "changes": "Конвертация авторских событий Аддис-Абебы в структурированный YAML."
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