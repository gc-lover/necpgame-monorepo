-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\buenos-aires-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.376558

BEGIN;

-- Lore: canon-lore-america-buenos-aires-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-buenos-aires-2020-2093',
    'Буэнос-Айрес — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-buenos-aires-2020-2093",
    "title": "Буэнос-Айрес — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:20:00+00:00",
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
      "buenos-aires"
    ],
    "topics": [
      "timeline-author",
      "antarctic"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1293",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1293",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:20:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/buenos-aires-2020-2093.md",
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
    "problem": "Таймлайн Буэнос-Айреса в Markdown не связывал культурные и антарктические инициативы в цельной структуре.",
    "goal": "Зафиксировать превращение Буэнос-Айреса в южный форпост, равновесие между культурным экспортом и экстремальными территориями.",
    "essence": "Буэнос-Айрес опирается на танго-протоколы, пампас-серверы и антарктический контроль, формируя «пакет юга».",
    "key_points": [
      "Этапы от южной жемчужины до экспорта протоколов экстремальных регионов.",
      {
        "Хуки": "Ла-Бока неон, пампас-серверы, антарктические экспедиции, патагонские бункеры, ледяные архивы."
      },
      "Подготовлены связки для сюжетов о Меркосур-сети и южном полюсе."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Южная жемчужина",
        "body": "- «Ла-Бока неон»: танго-клубы с BD-технологиями.\n- «Пампас-серверы»: дата-центры в степях.\n- «Рио-де-Ла-Плата порт»: морская логистика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Меркосур-сеть",
        "body": "- «Южноамериканский союз»: интеграция с соседями.\n- «Аргентинские стартапы»: технологический бум.\n- «Фолклендские споры»: киберконфликты за острова.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Антарктический форпост",
        "body": "- «Антарктические экспедиции»: контроль южного континента.\n- «Патагонские бункеры»: убежища на краю света.\n- «Танго-протоколы»: культурный экспорт и мягкая сила.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Южный полюс",
        "body": "- «Антарктическая база»: постоянное присутствие Аргентины.\n- «Ледяные архивы»: хранилища данных на льду.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет юга",
        "body": "- Экспорт протоколов освоения экстремальных регионов.\n- Буэнос-Айрес становится центром южной стратегии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Танго-клубы BD, пампас-серверы, антарктические экспедиции, патагонские бункеры, ледяные архивы.\n- Сюжеты о Меркосур-сети и южной геополитике.\n",
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
    "github_issue": 1293,
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
      "changes": "Конвертация авторских событий Буэнос-Айреса в структурированный YAML."
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