-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\panama-city-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.278565

BEGIN;

-- Lore: canon-lore-america-panama-city-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-panama-city-2020-2093',
    'Панама-Сити — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-panama-city-2020-2093",
    "title": "Панама-Сити — авторские события 2020–2093",
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
      "panama-city"
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
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/panama-city-2020-2093.md",
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
    "problem": "Markdown-описание Панама-Сити не фиксировало канальные мегапроекты, данные джунглей и роль центральноамериканского хаба.",
    "goal": "Структурировать эволюцию транзитной столицы до экспортёра стандарта глобальных коридоров.",
    "essence": "Панама-Сити расширяет канал, автоматизирует транзит и защищает джунгли, предлагая миру «пакет канала».",
    "key_points": [
      "Этапы от канального узла до центральноамериканского протокола.",
      {
        "Хуки": "панамский клин, новый канал 2.0, дроны-контейнеры, дата-штормы тропиков, панама-протокол."
      },
      "Подготовлены связи для сюжетов о глобальной логистике, биобанках джунглей и корпоративных анклавов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Канальный узел",
        "body": "- «Панамский клин»: контроль трансокеанского канала.\n- «Канальные хабы»: оффлайн-пакеты между океанами.\n- «Офшорные серверы»: финансовая секретность.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Мегапроект канала",
        "body": "- «Новый канал 2.0»: расширение и автоматизация.\n- «Дроны-контейнеры»: автономная логистика.\n- «Корпоративные анклавы»: свободные зоны вокруг шлюзов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Глобальная артерия",
        "body": "- «Транзитный контроль»: управление мировой торговлей.\n- «Биоразнообразие джунглей»: генетические банки.\n- «Дата-штормы тропиков»: природная киберзащита.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Центральноамериканский хаб",
        "body": "- «Панама-протокол»: стандарты транзитной торговли.\n- «Карибско-тихоокеанская сеть»: синхронные коридоры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет канала",
        "body": "- Экспорт протоколов транзитного управления и климатической защиты.\n- Панама-Сити становится эталоном глобальных логистических коридоров.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Панамский клин, новый канал 2.0, дроны-контейнеры, дата-штормы тропиков, панама-протокол.\n- Сюжеты о контроле канала, корпоративных анклагах и биобанках джунглей.\n",
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
    "github_issue": 1283,
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
      "changes": "Конвертация авторских событий Панама-Сити в структурированный YAML."
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