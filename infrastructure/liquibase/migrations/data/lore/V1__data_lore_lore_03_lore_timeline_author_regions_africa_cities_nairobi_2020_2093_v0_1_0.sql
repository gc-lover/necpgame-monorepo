-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\nairobi-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.358504

BEGIN;

-- Lore: canon-region-nairobi-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-nairobi-2020-2093',
    'Найроби — авторские события 2020-2093',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-nairobi-2020-2093",
    "title": "Найроби — авторские события 2020-2093",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:20:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "nairobi",
      "city",
      "africa"
    ],
    "topics": [
      "regional-events",
      "tech-hubs"
    ],
    "related_systems": [
      "narrative-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-regions-africa-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1294",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1294",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:20:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/nairobi-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "economy"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "concept_director",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "События Найроби были описаны в Markdown и не подключались к общей матрице регионов Африки.",
    "goal": "Оцифровать эволюцию города от «Силиконовой саванны» до экспортёра мобильных протоколов в едином YAML.",
    "essence": "Найроби растёт как восточноафриканский тех-хаб, объединяет регион и балансирует инновации с природными заповедниками.",
    "key_points": [
      "2020-е — становление стартап-хаба и переход к M-Pesa 3.0.",
      "2030-2060 — цифровой рывок и дата-центры в саванне.",
      "2070-е+ — политическое лидерство и экспорт био-технологий."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "decade_2020",
        "title": "2020-2029 — Восточноафриканский хаб",
        "body": "- «Силикон Саванна» формирует экосистему стартапов и инкубаторов.\n- «M-Pesa 3.0» закрепляет лидерство города в мобильных платежах.\n- «Кибера импланты» появляются в трущобных клиниках, создавая новый рынок.\n",
        "mechanics_links": [
          "mechanics/economy/economy-events.yaml"
        ],
        "assets": []
      },
      {
        "id": "decade_2030",
        "title": "2030-2039 — Цифровой прыжок",
        "body": "- Китайские инвестиции строят дата-центры и инфраструктуру.\n- «Масаи-киберпанк» объединяет традиционную эстетику с неоновой модой.\n- Восточноафриканское сообщество усиливает региональную интеграцию.\n",
        "mechanics_links": [
          "mechanics/world/world-state/player-impact-systems.yaml"
        ],
        "assets": []
      },
      {
        "id": "decade_2040",
        "title": "2040-2060 — Red+: сервера саванны",
        "body": "- Дата-центры используют природное охлаждение саванны.\n- «Кенийский стандарт» регулирует мобильные технологии по всему континенту.\n- Заповедники 2.0 внедряют биотехнологии для охраны природы.\n",
        "mechanics_links": [
          "mechanics/economy/economy-logistics.yaml"
        ],
        "assets": []
      },
      {
        "id": "decade_2060",
        "title": "2061-2077 — Восточная столица",
        "body": "- «Рог Африки Альянс» оформляет политическое и экономическое объединение.\n- «Эфиопско-кенийский коридор» создаёт технологическую ось.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "decade_2078",
        "title": "2078-2093 — Пакет саванны",
        "body": "- Экспорт мобильных протоколов, био-технологий и гибридных решений безопасности.\n- Найроби становится посредником между корпорациями и региональными государствами.\n",
        "mechanics_links": [
          "mechanics/economy/economy-contracts.yaml"
        ],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Системы M-Pesa и «Силикон Саванна» влияют на экономические кампании игроков.\n- Масаи-киберпанк добавляет визуальные и социальные слои для квестов.\n- Заповедники 2.0 связываются с экологическими сюжетами и биодоменами.\n",
        "mechanics_links": [
          "mechanics/world/world-state/player-impact-systems.yaml"
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
    "github_issue": 1294,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "concept_team",
      "changes": "Конвертация авторских событий Найроби в YAML."
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