-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\kuala-lumpur-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.367733

BEGIN;

-- Lore: canon-lore-asia-kuala-lumpur-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-kuala-lumpur-2020-2093',
    'Куала-Лумпур — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-kuala-lumpur-2020-2093",
    "title": "Куала-Лумпур — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T03:55:00+00:00",
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
      "asia",
      "kuala-lumpur"
    ],
    "topics": [
      "timeline-author",
      "megastructure"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1268",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1268",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T03:55:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/kuala-lumpur-2020-2093.md",
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
    "problem": "Хронология Куала-Лумпура лежала в Markdown и не учитывала мегаструктурные и религиозные арки в единой структуре.",
    "goal": "Зафиксировать эпохи Куала-Лумпура как вертикального мегаполиса с интеграцией мильз и ислама.",
    "essence": "Куала-Лумпур растёт вверх, объединяя вертикальные коридоры, халяль-протоколы и исламский киберпанк в «пакет плотности».",
    "key_points": [
      "Этапы от двойных башен 2.0 до экспорта вертикальных стандартов.",
      {
        "Хуки": "Megatowers, halal-tech, исламский киберпанк, вертикальные фермы."
      },
      "Подготовлены сценарии для вертикального транспорта и религиозных протоколов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Башни будущего",
        "body": "- «Petronas 2.0»: обновление башен с нейро-интерфейсами.\n- «Halal-tech»: технологические стандарты для религиозных сервисов.\n- «KL-Singapore Hyperloop»: скоростной коридор.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Вертикальная экспансия",
        "body": "- «Megatowers»: многоуровневые комплексы.\n- «Вертикальные фермы»: сельское хозяйство в небоскрёбах.\n- «Sky Pods»: дроны-такси.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Мегаструктуры и вера",
        "body": "- «Исламский киберпанк»: интеграция религиозных практик и технологий.\n- «Межконфессиональные протоколы»: долина веротерпимости.\n- «Климатические купола»: контроль дождей и жары.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Региональный узел",
        "body": "- «Малайзия-АСЕАН»: ось торговли и финтеха.\n- «VR-уммы»: виртуальные религиозные сообщества.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет плотности",
        "body": "- Экспорт протоколов вертикальных городов и религиозной совместимости.\n- Куала-Лумпур — эталон сверхплотной урбанизации.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Megatowers, halal-tech, исламский киберпанк, VR-уммы, вертикальные фермы.\n- Сюжеты о веротерпимости и сверхплотных городах.\n",
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
    "github_issue": 1268,
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
      "changes": "Конвертация авторских событий Куала-Лумпура в структурированный YAML."
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