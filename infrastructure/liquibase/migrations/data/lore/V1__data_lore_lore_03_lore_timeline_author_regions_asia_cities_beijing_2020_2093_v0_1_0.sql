-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\beijing-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.335241

BEGIN;

-- Lore: canon-lore-asia-beijing-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-beijing-2020-2093',
    'Пекин — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-beijing-2020-2093",
    "title": "Пекин — авторские события 2020–2093",
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
      "beijing"
    ],
    "topics": [
      "timeline-author",
      "governance"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/beijing-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "worldbuilding",
      "live_ops"
    ],
    "risk_level": "high"
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
    "problem": "Хронология Пекина находилась в Markdown и не фиксировала контролирующие и дипломатические хуки в структурированном виде.",
    "goal": "Оцифровать эпохи Пекина как технологической сверхдержавы для дальнейшего использования в сюжетах контроля и экспансии.",
    "essence": "Пекин распределяет социальный кредит, строит Великие стены данных и экспортирует глобальный «пакет контроля».",
    "key_points": [
      "Этапы от цифрового дракона до глобального центра и экспорта стандартов.",
      {
        "Зафиксированы хуки": "Социальный кредит 2.0, Великая стена данных, AI-правительство, «Blackwall китайский»."
      },
      "Подготовлена база для сценариев о цифровом юане и пекинском консенсусе."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Цифровой дракон",
        "body": "- «Запретный город AR»: имперское наследие в цифровых слоях.\n- «Социальный кредит 2.0»: тотальный контроль через импланты.\n- «Великая стена данных»: национальный файервол.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Технологическая сверхдержава",
        "body": "- «Шёлковый путь 3.0»: глобальная сеть влияния.\n- «AI-правительство»: автоматизированное управление городом.\n- «Подземный Пекин»: мега-метро и бункеры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Контроль и инновации",
        "body": "- «Blackwall китайский»: собственная версия защиты.\n- «Экспорт стандартов»: протоколы контроля для других стран.\n- «Лунные программы»: космическая экспансия.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Глобальный центр",
        "body": "- «Пекинский консенсус»: альтернатива западным моделям.\n- «Цифровой юань»: глобальная валюта.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет контроля",
        "body": "- Экспорт протоколов тотального управления и цифровой валюты.\n- Пекин закрепляется как мировая столица надзора.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Социальный кредит 2.0, Великая стена данных, AI-правительство, Blackwall китайский, пекинский консенсус.\n- Сюжеты о глобальной валюте и цифровой дипломатии.\n",
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
    "github_issue": 1275,
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
      "changes": "Конвертация авторских событий Пекина в структурированный YAML."
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