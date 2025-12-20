-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\abuja-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.142747

BEGIN;

-- Lore: canon-lore-africa-abuja-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-abuja-2020-2093',
    'Абуджа — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-abuja-2020-2093",
    "title": "Абуджа — авторские события 2020–2093",
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
      "abuja"
    ],
    "topics": [
      "timeline-author",
      "governance"
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
        "id": "github-issue-1305",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1305",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:10:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/abuja-2020-2093.md",
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
    "problem": "Описание эпох Абуджи оставалось в Markdown и не было связано с системами планирования сюжетов и политических арок.",
    "goal": "Структурировать трансформацию Абуджи как столичного и административного хаба Западной Африки с упором на энергетику и безопасность.",
    "essence": "Запланированная столица превращается в укреплённый политический центр, который экспортирует протоколы управления и энергетики.",
    "key_points": [
      "Расслоены периоды от плановой столицы до экспортируемого пакета запланированных городов.",
      "Выделены хуки про геометрические купола, солнечные фермы саванны и афро-политические драмы.",
      "Задано связующее звено с западноафриканской интеграцией и энергетическими сценариями."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Запланированная столица",
        "body": "- «Геометрические купола»: развитие плановой архитектуры.\n- «Нефтяные данные»: переход экономики из нефти в цифровые активы.\n- «Региональные серверы»: сети для Западной Африки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Нигерийская мощь",
        "body": "- «Абуджа-Лагос коридор»: экономическая магистраль между мегаполисами.\n- «Климат-адаптация»: инженерные решения против жары и ливней.\n- «Афро-политика BD»: политические саги как игровые события.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Административная крепость",
        "body": "- «Правительственные протоколы»: усиленная безопасность и фильтрация данных.\n- «Подземные бункеры»: стратегические архивы и защитные центры.\n- «Солнечные фермы саванны»: региональная энергетика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Западноафриканский центр",
        "body": "- «ЭКОВАС штаб-квартира»: платформа объединённых государств.\n- «Культурный экспорт»: нигерийские медиа и хабы творческих индустрий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет столиц",
        "body": "- Экспорт протоколов плановых столиц и административной безопасности.\n- Использование Абуджи как эталона для выстраивания новых мегаполисов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Геометрические купола, солнечные фермы саванны, афро-политика BD.\n- Сборки сюжетов про ЭКОВАС и административные протоколы.\n",
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
    "github_issue": 1305,
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
      "changes": "Конвертация авторских событий Абуджи в структурированный YAML."
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