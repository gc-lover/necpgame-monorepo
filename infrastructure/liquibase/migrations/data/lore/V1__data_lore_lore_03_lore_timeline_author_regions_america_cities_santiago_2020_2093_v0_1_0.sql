-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\santiago-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.434934

BEGIN;

-- Lore: canon-lore-america-santiago-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-santiago-2020-2093',
    'Сантьяго — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-santiago-2020-2093",
    "title": "Сантьяго — авторские события 2020–2093",
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
      "america",
      "santiago"
    ],
    "topics": [
      "timeline-author",
      "andes"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/santiago-2020-2093.md",
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
    "problem": "Markdown-описание Сантьяго не связывало горные серверы, медные рудники и антарктические амбиции.",
    "goal": "Описать траекторию Сантьяго как андского тех-хаба и тихоокеанского форпоста.",
    "essence": "Сантьяго соединяет горные дата-центры, медные AI и антарктические экспедиции, экспортируя «пакет Анд».",
    "key_points": [
      "Этапы от андской столицы до южноамериканского хаба и экспорта горных протоколов.",
      {
        "Хуки": "Вальпараисо-коридор, медные рудники AI, сейсмо-щиты, патагонский альянс, антарктические экспедиции."
      },
      "Готовые точки для сюжетов о горной логистике, техногородах и Антарктиде."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Андская столица",
        "body": "- «Горные серверы»: дата-центры в Андах.\n- «Вальпараисо-коридор»: связь с портом.\n- «Чилийский силикон»: стартап-экосистема.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Медь и данные",
        "body": "- «Медные рудники AI»: автоматизированная добыча.\n- «Сейсмо-щиты»: защита от землетрясений.\n- «Вино-BD»: культура долин в цифре.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Тихоокеанский форпост",
        "body": "- «Антарктические экспедиции»: контроль южных территорий.\n- «Патагонский альянс»: связь с Аргентиной.\n- «Био-разнообразие»: уникальная экология Чили.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Южноамериканский хаб",
        "body": "- «Транс-Андский коридор»: связь через горы.\n- «Культурный экспорт»: чилийская эстетика и техно-бренды.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Анд",
        "body": "- Экспорт протоколов горных мегаполисов и антарктических инициатив.\n- Сантьяго позиционируется как стратегический центр юга.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Горные серверы, медные рудники AI, сейсмо-щиты, антарктические экспедиции, патагонский альянс.\n- Сюжеты о горных сетях, транспортных коридорах и климатических экспедициях.\n",
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
    "github_issue": 1280,
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
      "changes": "Конвертация авторских событий Сантьяго в структурированный YAML."
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