-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\tokyo-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.526885

BEGIN;

-- Lore: canon-region-asia-tokyo-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-asia-tokyo-2020-2093',
    'Токио 2020-2093 — Авторская хронология',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-asia-tokyo-2020-2093",
    "title": "Токио 2020-2093 — Авторская хронология",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.2.0",
    "last_updated": "2025-12-12T00:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "asia",
      "tokyo",
      "blackwall"
    ],
    "topics": [
      "regional-history",
      "cultural-governance"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/tokyo-2020-2093.md",
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
    "problem": "Хронология Токио была в Markdown и не учитывалась в структурированной базе знаний.",
    "goal": "Перевести авторские эпохи города в YAML с акцентом на культурно-правовые параметры и Blackwall-коридоры.",
    "essence": "Токио проходит путь от рекурсивных кварталов и мегастеков до экспорта «Пакета Токио» с судами параметров и нулевыми каналами.",
    "key_points": [
      {
        "Пять эпох": "от рекурс-кварталов и мегастеков до «Пакета Токио» с судами параметров."
      },
      "Коридоры Blackwall, суд параметров, лицензии BD-призраков и нулевые каналы — ядро механик.",
      "Основание для квестов культурного арбитража, оффлайн-маршрутов и клубов «нулевого шума»."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Рекурс-кварталы",
        "body": "«Рекурс Arasaka» формирует циклы полной безопасности и нулевой терпимости.\n«Синто-Лабс» связывает биоэтику имплантов с ритуалами, а «Кабуки-Линк» становится сценой BD-театров.\n",
        "mechanics_links": [
          "mechanics/world/urban-expansion.yaml",
          "mechanics/world/reputation-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Мегастэки и водные линии",
        "body": "«Йокогама-Стэк» раскрывает вертикальные жилые ярусы и теневые рынки.\n«Токийские Каналы» поддерживают водные оффлайн-маршруты, а «Храм Памяти» хранит общественные архивы BD.\n",
        "mechanics_links": [
          "mechanics/world/urban-expansion.yaml",
          "mechanics/world/offline-networks.yaml"
        ],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ коридоры Blackwall",
        "body": "«Коридор Осаки» становится основным туннелем обмена с роевыми ИИ.\n«Код Вежливости» задаёт стандарты поведения сервисных ИИ, а «Клуб Нулевого Шума» обслуживает элиту.\n",
        "mechanics_links": [
          "mechanics/technology/blackwall-tunnels.yaml",
          "mechanics/quests/branching-outcomes.yaml"
        ],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Суд Параметров",
        "body": "«Кагура-казус» решает дела о параметрах культуры и сетевых прав.\n«Metro-Grid JP» расширяет совместимость с азиатскими хабами, а «Лицензии Призраков» регулируют BD-персон.\n",
        "mechanics_links": [
          "mechanics/world/governance/culture-courts.yaml",
          "mechanics/world/offline-networks.yaml"
        ],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Токио",
        "body": "Город экспортирует культурно-правовые параметры и устраивает «Ночь Храмов» как публичный метасценарий.\n",
        "mechanics_links": [
          "mechanics/world/expansion-kits.yaml",
          "mechanics/quests/quest-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Коридоры Blackwall, суды параметров, лицензии BD-призраков и нулевые каналы открывают сценарии дипломатии, расследований и киберритуалов.\n",
        "mechanics_links": [
          "mechanics/quests/quest-system.yaml",
          "mechanics/world/offline-networks.yaml",
          "mechanics/world/governance/culture-courts.yaml"
        ],
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
    "github_issue": 72,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.2.0",
      "date": "2025-12-12",
      "author": "content_team",
      "changes": "Добавлены ссылки на механики, уточнены эпохи и обновлена дата."
    },
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "concept_director",
      "changes": "Конвертирована авторская хронология Токио в YAML и структурированы эпохи и механики."
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