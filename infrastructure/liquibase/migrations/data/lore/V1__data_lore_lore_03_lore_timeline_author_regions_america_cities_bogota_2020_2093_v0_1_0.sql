-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\bogota-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.370877

BEGIN;

-- Lore: canon-lore-america-bogota-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-bogota-2020-2093',
    'Богота — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-bogota-2020-2093",
    "title": "Богота — авторские события 2020–2093",
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
      "bogota"
    ],
    "topics": [
      "timeline-author",
      "highlands"
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
        "id": "github-issue-1292",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1292",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:20:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/bogota-2020-2093.md",
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
    "problem": "Таймлайн Боготы в Markdown не связывал высокогорные системы, картели 2.0 и биоисследования в общей структуре знаний.",
    "goal": "Описать преображение Боготы как андского узла, сочетающего транспорт, биотех и политический альянс.",
    "essence": "Богота использует высоту и биоразнообразие, чтобы превратиться в «пакет высоты» для всего региона.",
    "key_points": [
      "Этапы от Трансмилениум 3.0 до экспорта высокогорных протоколов.",
      {
        "Зафиксированы хуки": "картели 2.0, андские серверы, партизанские сети, экваториальные лаборатории."
      },
      "Подготовлена база для сюжетов об андском альянсе и биоэкономике."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Андская высота",
        "body": "- «Трансмилениум 3.0»: автономная транспортная сеть мегаполиса.\n- «Картели 2.0»: киберпреступность нового уровня.\n- «Андские серверы»: дата-центры в высокогорье.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Кофейный код",
        "body": "- «Био-кофе+»: генетически модифицированные плантации.\n- «Медельин-Богота коридор»: технологическая ось.\n- «Джунгли-протоколы»: освоение Амазонии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Горная крепость",
        "body": "- «Высотные преимущества»: труднодоступность как защита.\n- «Партизанские сети»: децентрализованная инфраструктура из истории.\n- «Экваториальные лаборатории»: биоразнообразие для исследований.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Андский альянс",
        "body": "- «Боливар-сеть»: объединение андских стран.\n- «Экспорт био-данных»: биоразнообразие как ресурс.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет высоты",
        "body": "- Экспорт протоколов высокогорных технологий.\n- Богота закрепляется как центр андского хаба.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Трансмилениум 3.0, картели 2.0, андские серверы, партизанские сети, экваториальные лаборатории.\n- Сюжеты о биоразнообразии, андском альянсе и защищённом транспорте.\n",
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
    "github_issue": 1292,
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
      "changes": "Конвертация авторских событий Боготы в структурированный YAML."
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