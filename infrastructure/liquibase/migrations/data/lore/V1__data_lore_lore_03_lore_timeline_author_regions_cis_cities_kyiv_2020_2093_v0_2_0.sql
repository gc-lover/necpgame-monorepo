-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\kyiv-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.553541

BEGIN;

-- Lore: canon-region-cis-kyiv-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-cis-kyiv-2020-2093',
    'Киев 2020-2093 — Досье региона',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-cis-kyiv-2020-2093",
    "title": "Киев 2020-2093 — Досье региона",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.2.0",
    "last_updated": "2025-11-23T04:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "cis",
      "kyiv",
      "chronology"
    ],
    "topics": [
      "regional-history",
      "diplomacy"
    ],
    "related_systems": [
      "narrative-service",
      "social-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-cis-index",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "complements"
      },
      {
        "id": "github-issue-1262",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1262",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:00:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/kyiv-2020-2093.md",
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
        "role": "narrative_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Хронология киевских событий была в Markdown и не позволяла системно подцепить нейтральные и дипломатические сценарии.",
    "goal": "Структурировать эпохи Киева как IT-хаба и нейтральной площадки для переговоров, подчеркнув буферные и мостовые механики.",
    "essence": "«Город посредников и стартапов, где цифровой активизм соседствует с международной дипломатией».",
    "key_points": [
      {
        "Выделены ключевые эпохи": "IT-бум, европейский вектор, буфер Red+, нейтральные арбитражи."
      },
      "Добавлены хуки для сюжетов Майдана 3.0, мостов совместимости и международных судов.",
      "Подготовлена база для дипломатических и социально-инженерных квестов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Восстановление и IT-бум",
        "body": "- «Днепр-Сити»: формирование нового IT-квартала на берегу.\n- «Майдан 3.0»: цифровые площадки протестов и активизма.\n- «Лавра-архивы»: монастырские комплексы как дата-хранилища культуры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Европейский вектор",
        "body": "- «Ворота в ЕС»: транзитный хаб между европейскими и СНГ-блоками.\n- «Стартап-столица»: акселераторы киберстартапов.\n- «Подольские тоннели»: подземная логистика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ нейтральная зона",
        "body": "- «Буферный город»: нейтральный статус между блоками.\n- «Мосты совместимости»: протоколы для разных сетей.\n- «Софийские серверы»: сохранение культурного наследия.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Дипломатический центр",
        "body": "- «Город-посредник»: площадка для переговоров корпораций и государств.\n- «Нейтральные суды»: международные арбитражи и посредничества.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет посредничества",
        "body": "- Экспорт протоколов нейтрального управления и цифровой дипломатии.\n- Позиционирование Киева как примера сбалансированного цифрового города.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Майдан 3.0, мосты совместимости, нейтральные суды и дипломатические арки.\n- Возможность сценариев Live Ops по посредничеству между фракциями.\n",
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
    "github_issue": 1262,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.2.0",
      "date": "2025-11-11",
      "author": "narrative_team",
      "changes": "Конвертация событий Киева в структурированный YAML."
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