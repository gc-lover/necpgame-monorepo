-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\origins\data-orphan.yaml
-- Generated: 2025-12-13T21:13:37.850257

BEGIN;

-- Lore: origin-data-orphan
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'origin-data-orphan',
    'Origin: Data Orphan',
    'canon',
    'origin-story',
    '{
  "metadata": {
    "id": "origin-data-orphan",
    "title": "Origin: Data Orphan",
    "document_type": "canon",
    "category": "origin-story",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-05T18:38:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "origin",
      "netrunner",
      "blackwall"
    ],
    "topics": [
      "origins",
      "narrative"
    ],
    "related_systems": [
      "netrun-service",
      "analytics-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/origins/data-orphan.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative"
    ],
    "risk_level": "high"
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
    "problem": "История Data Orphan описана кратко и не учитывает игровые проверки и последствия.",
    "goal": "Сформировать ветви происхождения с акцентом на шумы Blackwall и метаподсказки.",
    "essence": "Игрок пережил DataKrash, обладает аномальными сетевыми эхо и использует их для поиска своего происхождения.",
    "key_points": [
      "Завязка связана с шумом Blackwall и тестами hacking и deception.",
      "Эпохальные события раскрывают зараженные микрокоды, часовых Blackwall и перезапуск реальности.",
      "Бонусы снижают пороги hacking, но увеличивают heat при провалах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Кратко",
        "body": "- Игрок известен как \"сирота данных\", происхождение стерто во время DataKrash.\n- Стартовые навыки: hacking и deception на уровне проверки 0.66 и выше.\n- Над игроком постоянно звучит сетевой шум, намекающий на скрытые узлы.\n",
        "mechanics_links": [
          "mechanics/netrun/netrunner-core.yaml"
        ]
      },
      {
        "id": "inciting_incident",
        "title": "Завязка",
        "body": "Первые миссии проходят рядом с разломами Blackwall. Успешные проверки дают эхо подсказок о потерянной личности, провалы вызывают всплеск heat и охоту часовых.\n",
        "mechanics_links": [
          "mechanics/social/social-mechanics-overview.yaml"
        ]
      },
      {
        "id": "epoch_path",
        "title": "Траектория эпох",
        "body": "- 2030-2045: баги недели и зараженные микрокоды формируют иммунитет или новые уязвимости.\n- 2045-2060: появление часовых Blackwall и теплых коридоров, где эхо становится громче.\n- 2060-2077: грань намеков и участие в мета судах по цифровой личности.\n- 2078-2093: поиск артефактов реальности и возможный перезапуск идентичности.\n",
        "mechanics_links": []
      },
      {
        "id": "rewards",
        "title": "Бонусы и риски",
        "body": "- Снижение порогов hacking рядом с разломами.\n- Получение временных подсказок маршрутов и скрытых дверей.\n- Heat растет при провале проверок, что привлекает Blackwall sentry и корпоративные охотничьи группы.\n",
        "mechanics_links": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 133,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-05",
      "author": "concept_team",
      "changes": "Конверсия origin Data Orphan в формат знаний."
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