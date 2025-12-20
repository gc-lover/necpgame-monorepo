-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\lima-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.241439

BEGIN;

-- Lore: canon-lore-america-lima-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-lima-2020-2093',
    'Лима — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-lima-2020-2093",
    "title": "Лима — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:25:00+00:00",
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
      "america",
      "lima"
    ],
    "topics": [
      "timeline-author",
      "seismic"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1289",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1289",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:25:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/lima-2020-2093.md",
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
    "problem": "Таймлайн Лимы в Markdown не объединял сейсмическую адаптацию, гастрономический экспорт и андский альянс в единую структуру.",
    "goal": "Систематизировать переход Лимы от тихоокеанской жемчужины к глобальному пакету сейсмической защиты.",
    "essence": "Лима строит умные здания, защищает Тихий океан и экспортирует протоколы адаптации и культурного синтеза.",
    "key_points": [
      "Этапы от Мирафлорес анклава до транс-тихоокеанской сети.",
      {
        "Хуки": "Инка-AR, гастро-BD, умные здания, подземная Лима, транс-тихоокеанская сеть."
      },
      "Основа для сюжетов о сейсмических технологиях и андском сотрудничестве."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Тихоокеанская жемчужина",
        "body": "- «Мирафлорес анклав»: элитный район на утёсах.\n- «Инка-AR»: цифровые слои над древними руинами.\n- «Тихоокеанские порты»: оффлайн-логистика на побережье.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Андский альянс",
        "body": "- «Лима-Богота коридор»: технологическая ось.\n- «Гастро-BD»: перуанская кухня как нейросетевой экспорт.\n- «Пустынные расширения»: города в прибрежной пустыне.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Сейсмическая адаптация",
        "body": "- «Умные здания»: защита от землетрясений.\n- «Подземная Лима»: убежища и метро.\n- «Амазонские экспедиции»: контроль джунглей.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Тихоокеанский хаб",
        "body": "- «Транс-тихоокеанская сеть»: связь с Азией.\n- «Культурный синтез»: инка-наследие и киберпанк.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет адаптации",
        "body": "- Экспорт протоколов сейсмической защиты и судоходства.\n- Лима становится эталоном устойчивых прибрежных мегаполисов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Инка-AR, гастро-BD, умные здания, подземная Лима, транс-тихоокеанская сеть.\n- Сценарии о сейсмической адаптации и андском сотрудничестве.\n",
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
    "github_issue": 1289,
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
      "changes": "Конвертация авторских событий Лимы в структурированный YAML."
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