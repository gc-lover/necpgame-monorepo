-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\delhi-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.477216

BEGIN;

-- Lore: canon-lore-asia-delhi-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-delhi-2020-2093',
    'Дели — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-delhi-2020-2093",
    "title": "Дели — авторские события 2020–2093",
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
      "delhi"
    ],
    "topics": [
      "timeline-author",
      "megacity"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/delhi-2020-2093.md",
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
    "problem": "Хронология Дели осталась в Markdown и не связывала мегаполисные контрасты, климат-куполы и цифровую демократию в едином формате.",
    "goal": "Сформировать эпохи Дели как мегаполиса контрастов, отражающих климатическую адаптацию и социальное давление.",
    "essence": "Дели балансирует между куполами элиты и трущобами имплантов, внедряя блокчейн-референдумы и экспортируя «пакет разнообразия».",
    "key_points": [
      "Этапы от Красного Форта 2.0 до экспорта протоколов многомиллионных городов.",
      "Подчёркнуты купола над смогом, кастовые коды, метро-лабиринты и блокчейн-демократия.",
      "Созданы хуки для сюжетов о цифровом равенстве и водных конфликтах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Мегаполис контрастов",
        "body": "- «Красный форт 2.0»: защищённый правительственный анклав.\n- «Трущобы имплантов»: нелегальные клиники на каждом углу.\n- «Ямуна стена»: граница между элитой и массами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Индийский кремний",
        "body": "- «Бангалор-Дели коридор»: технологическая ось Индии.\n- «Болливуд BD»: индустрия BD как глобальный игрок.\n- «Кастовые коды»: цифровое наследование статуса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Купола над смогом",
        "body": "- «Климат-купола»: защита для элиты.\n- «Аутсайдеры»: миллионы живут вне куполов.\n- «Метро-лабиринты»: подземные города в метро.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Цифровая демократия",
        "body": "- «Референдум-блокчейн»: прямое голосование через импланты.\n- «Кастовая революция»: движение за цифровое равенство.\n- «Ганг-архивы»: священные реки как метафора данных.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет разнообразия",
        "body": "- Экспорт протоколов управления многомиллионными городами и климат-адаптации.\n- Дели становится эталоном балансировки кастовых и цифровых систем.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Купола над смогом, кастовые коды, метро-лабиринты, референдум-блокчейн, кастовая революция.\n- Сюжеты о цифровом равенстве и климатической миграции.\n",
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
    "github_issue": 1273,
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
      "changes": "Конвертация авторских событий Дели в структурированный YAML."
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