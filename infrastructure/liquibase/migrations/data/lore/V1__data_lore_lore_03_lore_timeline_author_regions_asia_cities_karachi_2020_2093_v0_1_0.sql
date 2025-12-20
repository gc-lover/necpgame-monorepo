-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\karachi-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.361474

BEGIN;

-- Lore: canon-lore-asia-karachi-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-karachi-2020-2093',
    'Карачи — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-karachi-2020-2093",
    "title": "Карачи — авторские события 2020–2093",
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
      "karachi"
    ],
    "topics": [
      "timeline-author",
      "logistics"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/karachi-2020-2093.md",
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
    "problem": "Хронология Карачи была в Markdown и не отражала портовые и энергетические хуки в знаниях.",
    "goal": "Структурировать превращение Карачи в узел Индийского океана с корпоративным управлением.",
    "essence": "Карачи автоматизирует мегапорт, решает водный дефицит и экспортирует «пакет портов».",
    "key_points": [
      "Этапы от портового мегаполиса до корпоративной свободной зоны.",
      "Подчёркнуты морские оффлайн-коридоры, портовые серверы, купола пыли.",
      "Подготовлены сценарии о борьбе за воду и миграционных волнах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Порт мегаполиса",
        "body": "- «Карачи порт-хаб»: автономные контейнерные узлы.\n- «Водо-нехватка»: серые рынки воды.\n- «Старые кварталы AR»: исторические районы в цифре.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Энерго-линии",
        "body": "- «Солнечные пустыни»: энергетические фермы Синда.\n- «Десалинация 2.0»: мегазаводы опреснения.\n- «Тех-патрули»: корпоративные охранные сети.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Узел Индийского океана",
        "body": "- «Морские оффлайн-коридоры»: пиратские маршруты данных.\n- «Портовые серверы»: дата-центры в контейнерах.\n- «Купола пыли»: защита от бурь.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Корпоративный порт-город",
        "body": "- «Свободные экономические зоны»: легальные анклавы мегакорпораций.\n- «Миграционные волны»: дешёвая рабочая сила для порта.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет портов",
        "body": "- Экспорт протоколов управления мегапортами и защитой от бурь.\n- Карачи становится образцом портовой автоматизации.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Морские оффлайн-коридоры, портовые серверы, купола пыли, десалинация 2.0, миграционные волны.\n- Сюжеты о защите портовых данных и рынках воды.\n",
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
    "github_issue": 1269,
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
      "changes": "Конвертация авторских событий Карачи в структурированный YAML."
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