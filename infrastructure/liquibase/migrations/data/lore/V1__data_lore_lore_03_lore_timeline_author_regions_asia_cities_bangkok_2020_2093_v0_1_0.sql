-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\bangkok-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.330413

BEGIN;

-- Lore: canon-lore-asia-bangkok-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-bangkok-2020-2093',
    'Бангкок — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-bangkok-2020-2093",
    "title": "Бангкок — авторские события 2020–2093",
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
      "asia",
      "bangkok"
    ],
    "topics": [
      "timeline-author",
      "spirituality"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/bangkok-2020-2093.md",
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
    "problem": "Описание Бангкока в Markdown не позволяло использовать духовные и климатические хуки в сценариях.",
    "goal": "Структурировать эпохи Бангкока как города-на-воде со связкой духовности и кибертехнологий.",
    "essence": "Бангкок объединяет плавучие рынки, монастыри-дата-центры и цифровое просветление, экспортируя «пакет баланса».",
    "key_points": [
      "Этапы от логистического города ангелов до духовного центра киберпанка.",
      "Хуки про Чао-Прайя дроны, подводные храмы и нирвана-протокол.",
      "Сформировано основание для сюжетов о климат-адаптации и духовном контенте."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Город ангелов 2.0",
        "body": "- «Чао-Прайя дроны»: речная логистика дронов.\n- «Храмы памяти»: монастыри как архивы BD.\n- «Каосан 3.0»: туристический квартал для киберпаломников.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — ЮВА-хаб",
        "body": "- «АСЕАН-серверы»: региональный дата-центр.\n- «Плавучие рынки 2.0»: нейро-интерфейсы торговли на воде.\n- «Секс-индустрия+»: киберпроституция и BD-сервисы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Затопленный город",
        "body": "- «Плавучий Бангкок»: адаптация к подъёму моря.\n- «Подводные храмы»: туристические маршруты.\n- «Биологическая защита»: генмоды против тропических болезней.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Духовный центр киберпанка",
        "body": "- «Буддизм 2.0»: интеграция нейро-технологий.\n- «Нирвана-протокол»: цифровое просветление.\n- «Золотые серверы»: монастыри-дата-центры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет баланса",
        "body": "- Экспорт духовно-технологических практик.\n- Создание эталона соосуществования религии и технологий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Плавучие рынки 2.0, подводные храмы, буддизм 2.0, нирвана-протокол, золотые серверы.\n- Сюжеты о киберпаломниках и климатических вызовах.\n",
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
    "github_issue": 1276,
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
      "changes": "Конвертация авторских событий Бангкока в структурированный YAML."
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