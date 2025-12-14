-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\istanbul-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.780563

BEGIN;

-- Lore: canon-lore-europe-istanbul-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-istanbul-2020-2093',
    'Стамбул — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-istanbul-2020-2093",
    "title": "Стамбул — авторские события 2020–2093",
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
      "europe",
      "middle-east",
      "istanbul"
    ],
    "topics": [
      "timeline-author",
      "trade"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-middle-east-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/istanbul-2020-2093.md",
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
    "problem": "Стамбул в Markdown не раскрывал системно транзит трёх континентов, мистику суфийских серверов и босфорский нейтралитет.",
    "goal": "Сформировать путь Стамбула к статусу трёхконтинентального узла и экспортёра мультикультурных протоколов.",
    "essence": "Стамбул соединяет босфорские хабы, дата-султанат и суфийские серверы, предлагая миру «пакет моста».",
    "key_points": [
      "Этапы от моста цивилизаций до трёхконтинентального узла и экспорта мультикультурного управления.",
      {
        "Хуки": "базары нейро, султанат данных, суфийские серверы, нейтралитет Босфора, консорциум мостов."
      },
      "Сюжеты о транзите, духовности и гибридном управлении между Европой и Азией."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Мост цивилизаций",
        "body": "- «Босфор-хабы»: транзит Европа—Азия—Ближний Восток.\n- «Базары нейро»: крупнейшие рынки имплантов региона.\n- «Галатская башня-ретранслятор»: киберпанк-мечеть.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Тройной альянс",
        "body": "- «Три моря»: маршруты между Чёрным, Средиземным и Мраморным морями.\n- «Султанат данных»: гибридное управление.\n- «Подземные цистерны»: дата-хранилища в исторических подземельях.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Нейтральная зона",
        "body": "- «Босфорский нейтралитет»: демилитаризованная зона обмена.\n- «Мосты совместимости»: буферы между сетями регионов.\n- «Суфийские серверы»: мистические дата-общины.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Трёхконтинентальный узел",
        "body": "- «Консорциум мостов»: управление транзитом трёх континентов.\n- «Ночные рынки»: 24/7 BD-базары.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет моста",
        "body": "- Экспорт протоколов мультикультурного управления и транзита.\n- Стамбул закрепляется как нейтральный узел трёх континентов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Босфор-хабы, султанат данных, суфийские серверы, нейтралитет Босфора, консорциум мостов.\n- Сюжеты о транзите, духовности и мультикультурном управлении.\n",
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
    "github_issue": 1247,
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
      "changes": "Конвертация авторских событий Стамбула в структурированный YAML."
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