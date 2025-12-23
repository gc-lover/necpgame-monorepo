-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\singapore-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.394695

BEGIN;

-- Lore: canon-region-asia-singapore-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-asia-singapore-2020-2093',
        'Сингапур 2020-2093 — Буферный консорциум',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-asia-singapore-2020-2093",
        "title": "Сингапур 2020-2093 — Буферный консорциум",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T01:13:00+00:00",
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
          "singapore",
          "maritime"
        ],
        "topics": [
          "regional-history",
          "data-governance"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/singapore-2020-2093.md",
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
        "problem": "Описание Сингапура находилось в Markdown и не фиксировало буферные протоколы и BD-банкинг в структуре знаний.",
        "goal": "Структурировать хронику город-государства, подчёркивая эволюцию буферных шлюзов, морской логистики и анти-DataKrash систем.",
        "essence": "Сингапур от платных шлюзов совместимости переходит к глобальному «Пакету Сингапура», экспортируя модель буферного контроля.",
        "key_points": [
          "Пять эпох показывают усиление буферной монополии и переход к глобальному стандарту безопасности данных.",
          "Патрули Blackwall, BD-банкинг и суды конфиденциальности становятся ключевыми механиками.",
          "Подготовлены хуки для сюжетов морской логистики, анти-DataKrash и экспортируемых лицензий."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Консорциум буферов",
            "body": "Платные шлюзы совместимости закрепляют монополии и запускают судебные войны с пиратскими буферами.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Морская логистика",
            "body": "Дроны-паромы доставляют оффлайн-пакеты, а «Зелёные Порты» внедряют биорежимы обслуживания.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+ безопасный хаб",
            "body": "Протоколы анти-DataKrash и патрули Blackwall закрепляют роль Сингапура как защищённого узла.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Банкинг памяти",
            "body": "BD-банки и лицензии вводят новый уровень контроля за данными, суды конфиденциальности поддерживают доверие.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет Сингапура",
            "body": "Город экспортирует «буферную» модель как сервис безопасности и регуляции данных для других мегаполисов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Буферные шлюзы, анти-DataKrash протоколы, патрули Blackwall и BD-банкинг обеспечивают сценарии логистики, хакерских атак и дипломатии лицензий.\n",
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
        "github_issue": 72,
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
          "changes": "Конвертирована авторская хронология Сингапура в YAML и выделены ключевые буферные механики."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        0) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;