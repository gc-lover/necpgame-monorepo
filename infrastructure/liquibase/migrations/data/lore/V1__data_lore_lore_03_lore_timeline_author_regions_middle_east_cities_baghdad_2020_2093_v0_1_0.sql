-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east\cities\baghdad-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.852984

BEGIN;

-- Lore: canon-lore-middle-east-baghdad-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-middle-east-baghdad-2020-2093',
    'Багдад — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-middle-east-baghdad-2020-2093",
    "title": "Багдад — авторские события 2020–2093",
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
      "middle-east",
      "baghdad"
    ],
    "topics": [
      "timeline-author",
      "heritage"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-middle-east-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east/cities/baghdad-2020-2093.md",
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
    "problem": "Markdown-версия Багдада не объединяла реконструкцию двуречья, нефте-транзит и хранение древности.",
    "goal": "Структурировать эволюцию Багдада от восстановления до месопотамского ренессанса.",
    "essence": "Багдад совмещает двуречье-серверы, древние протоколы и песчаные купола, экспортируя «пакет древности».",
    "key_points": [
      "Этапы от восстановления и памяти до культурного возрождения и экспорта наследия.",
      {
        "Хуки": "Тигр-Евфрат AR, двуречье-серверы, древние протоколы, купола пыли, Зелёная зона 2.0."
      },
      "Сценарные опоры для конфликта нефти и данных, сохранения Месопотамии и регионального альянса."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Восстановление и память",
        "body": "- «Тигр-Евфрат AR»: цифровое двуречье.\n- «Реконструкция»: восстановление после войн.\n- «Культурное наследие»: древняя Месопотамия.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Нефтяной транзит",
        "body": "- «Транзитный хаб»: нефть и данные.\n- «Зелёная зона 2.0»: корпоративные анклавы.\n- «Подземные бункеры»: защита от конфликтов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Речная крепость",
        "body": "- «Двуречье-серверы»: охлаждение реками.\n- «Древние протоколы»: наследие Вавилона.\n- «Купола пыли»: защита от песчаных бурь.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Месопотамский ренессанс",
        "body": "- «Культурное возрождение»: синтез древности и будущего.\n- «Региональный альянс»: объединение Месопотамии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет древности",
        "body": "- Экспорт протоколов сохранения наследия и двуречья.\n- Багдад становится хранителем культурной памяти региона.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Тигр-Евфрат AR, двуречье-серверы, древние протоколы, купола пыли, Зелёная зона 2.0.\n- Сюжеты о нефти, данных и культурном наследии Месопотамии.\n",
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
    "github_issue": 1240,
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
      "changes": "Конвертация авторских событий Багдада в структурированный YAML."
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