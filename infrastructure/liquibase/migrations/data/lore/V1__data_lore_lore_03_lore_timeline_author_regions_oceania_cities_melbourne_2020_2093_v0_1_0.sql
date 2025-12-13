-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\oceania\cities\melbourne-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.728290

BEGIN;

-- Lore: canon-region-oceania-melbourne-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-oceania-melbourne-2020-2093',
    'Мельбурн 2020-2093 — Южный пакет',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-oceania-melbourne-2020-2093",
    "title": "Мельбурн 2020-2093 — Южный пакет",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T23:22:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "oceania",
      "melbourne",
      "climate-adaptation"
    ],
    "topics": [
      "regional-history",
      "sports-culture"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-oceania-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/oceania/cities/melbourne-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "lore_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Хронология Мельбурна находилась вне структурированной базы знаний и не связывала спорт, климат и логистику.",
    "goal": "Перевести эпохи города в YAML, выделив климатическую адаптацию и экспорт спортивных стандартов.",
    "essence": "Мельбурн развивается от культурной столицы до лидера климат-решений, поддерживая союзы юга Океании.",
    "key_points": [
      "Уточнены пять эпох, соединяющих киберарены, антарктические экспедиции и защиту от пожаров.",
      "Отмечены коралловые фермы, подземный мегаполис и водные технологии.",
      "Сформированы хуки для сюжетов спорта, климата и южных союзов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Культурная столица",
        "body": "Ярра-хабы размещают речные дата-центры, а спортивные киберарены выводят город в мировые рейтинги.\nКофейная культура 3.0 объединяет традиции и нейро-интерфейсы в городских кварталах.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Южный полюс инноваций",
        "body": "Мельбурн поддерживает антарктические экспедиции и строит био-лаборатории для уникальной флоры и фауны.\nПодземный мегаполис расширяет транспортную сеть и безопасность города.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ Климат-адаптация",
        "body": "Автоматизированные системы защищают от пожаров, а водные технологии обеспечивают опреснение и экономию.\nКоралловые фермы производят био-процессоры для региональных сетей.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Южная федерация",
        "body": "Мельбурн формирует союз с Сиднеем, конкурируя за лидерство в голубой экономике.\nЭкспорт спортивных стандартов укрепляет влияние в киберспортивных лигах.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет юга",
        "body": "Город экспортирует протоколы климатической адаптации и интегрирует их в союзные мегаполисы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Спортивные киберарены, антарктические экспедиции, коралловые фермы и водные технологии создают сюжетные линии соревнований и выживания.\n",
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
    "needs_task": false,
    "github_issue": 73,
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
      "changes": "Конвертированы авторские события Мельбурна в YAML и акцентированы климатические механики."
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