-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\cape-town-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.166784

BEGIN;

-- Lore: canon-lore-africa-cape-town-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-cape-town-2020-2093',
    'Кейптаун — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-cape-town-2020-2093",
    "title": "Кейптаун — авторские события 2020–2093",
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
      "cape-town"
    ],
    "topics": [
      "timeline-author",
      "innovation"
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
        "id": "github-issue-1301",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1301",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:15:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/cape-town-2020-2093.md",
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
    "problem": "История Кейптауна лежала в Markdown и не отражала инновационные и правовые хуки города в структуре знаний.",
    "goal": "Сформировать эпохи Кейптауна как южноафриканского инноваторского центра и экспортёра равноправных протоколов.",
    "essence": "Кейптаун развивает технологические стандарты, суды и энергетические фермы, закрепляя пан-африканский «пакет надежды».",
    "key_points": [
      "Выделены этапы от «южной жемчужины» до экспорта равноправных протоколов.",
      "Подчёркнуты сюжетные узлы про радужный протокол, пустынные фермы и пан-африканские суды.",
      "Подготовлены хуки для инновационных квестов и юридических сценариев."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Южная жемчужина",
        "body": "- «Столовая гора ретранслятор»: ключевой узел связи.\n- «Рондебош Силикон»: африканская Кремниевая долина.\n- «Атлантические порты»: оффлайн-логистика и торговля.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Радужный протокол",
        "body": "- «Пост-апартеид 2.0»: цифровое равенство и сертификации.\n- «Вотерфронт хабы»: туристические и деловые центры.\n- «Винные данные»: экспорт биотехнологий и агроплатформ.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Мыс Доброй Надежды",
        "body": "- «Южноафриканская федерация»: политическое объединение регионов.\n- «Пустынные фермы»: солнечная энергия Калахари.\n- «Робен-архивы»: цифровая память и права человека.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Африканский центр инноваций",
        "body": "- «Пан-африканский стандарт»: технологическое лидерство континента.\n- «Суды континента»: международные арбитражи и правовые протоколы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет надежды",
        "body": "- Экспорт протоколов равенства, инноваций и правосудия.\n- Кейптаун как образец пан-африканского прогресса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Рондебош Силикон, радужный протокол, пустынные фермы, пан-африканский стандарт, суды континента.\n- Квесты о технологическом лидерстве и равноправных системах.\n",
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
    "github_issue": 1301,
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
      "changes": "Конвертация авторских событий Кейптауна в структурированный YAML."
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