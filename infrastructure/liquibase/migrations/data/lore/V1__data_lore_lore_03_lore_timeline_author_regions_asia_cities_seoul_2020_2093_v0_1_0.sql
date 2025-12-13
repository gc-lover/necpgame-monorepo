-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\seoul-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.511268

BEGIN;

-- Lore: canon-region-asia-seoul-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-asia-seoul-2020-2093',
    'Сеул 2020-2093 — K-Cyber волна',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-asia-seoul-2020-2093",
    "title": "Сеул 2020-2093 — K-Cyber волна",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T01:15:00+00:00",
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
      "seoul",
      "k-culture"
    ],
    "topics": [
      "regional-history",
      "ai-governance"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/seoul-2020-2093.md",
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
    "problem": "Материал о Сеуле был в Markdown и не отражал киберспортивные и AI-механики в структурированной базе знаний.",
    "goal": "Описать эволюцию Сеула от K-cyber волны и чеболей до экспорта протоколов AI-управления.",
    "essence": "Сеул придерживается курса на киберкультуру, AI-правление и виртуальное воссоединение, превращаясь в глобальный пакет инноваций.",
    "key_points": [
      "Выделены пять эпох от K-cyber волны и PC-бангов 2.0 до экспорта AI-протоколов.",
      "Зафиксированы киберспорт-столица, обязательная киберслужба и AI-советы чеболей.",
      "Подготовлены хуки для сюжетов K-культуры, кибербезопасности и дипломатии через виртуальное объединение."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — K-Cyber волна",
        "body": "«Хангук-Тауэр» превращает город в самый подключённый мегаполис.\n«K-Pop Имплантация» внедряет культурные импланты, «Демилитаризованная зона» служит испытательной площадкой дронов, «PC-банги 2.0» обеспечивают полное погружение.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Корпоративные чеболи",
        "body": "«Самсунг-Сити» формирует корпоративный город-государство, «Хан-ривер Неон» поддерживает плавучие платформы стриминга.\n«Северный Коридор» развивает тайные переговоры о воссоединении через киберпространство.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ киберспорт-столица",
        "body": "«Глобальная Арена» становится крупнейшим киберспортивным комплексом, «Сеул-Серверы» — национальной гордостью.\n«Военный Протокол» вводит обязательную киберслужбу.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Нео-чеболи и AI",
        "body": "«AI-Правления» переводят корпорации на управление советами ИИ.\n«Воссоединение 2.0» создаёт виртуальную общность, а «Экспорт Культуры» распространяет K-контент глобально.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет инноваций",
        "body": "Сеул экспортирует протоколы киберспорта и AI-управления как глобальный стандарт.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "K-cyber импланты, глобальная арена, AI-правления, виртуальное воссоединение и PC-банги 2.0 раскрывают сценарии культуры, военной службы и технологической дипломатии.\n",
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
      "changes": "Конвертирована авторская хронология Сеула в YAML и структурированы ключевые инновации и механики."
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