-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\kazan-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.693228

BEGIN;

-- Lore: canon-lore-cis-kazan-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-cis-kazan-2020-2093',
    'Казань — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-cis-kazan-2020-2093",
    "title": "Казань — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:00:00+00:00",
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
      "cis",
      "kazan"
    ],
    "topics": [
      "timeline-author",
      "multiculturalism"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-cis-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1263",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1263",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:00:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/kazan-2020-2093.md",
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
    "problem": "Описание эпох Казани хранилось в Markdown и не учитывало мультикультурные хуки для нарратива и механик.",
    "goal": "Формализовать трансформацию Казани в мультикультурный узел Поволжья и зафиксировать ключевые механики.",
    "essence": "Казань соединяет IT-эксперименты Иннополиса, религиозно совместимые импланты и поволжскую федерацию.",
    "key_points": [
      "Выделены этапы от татарского хаба до экспорта мультикультурного пакета.",
      "Зафиксированы хуки для двуязычных сетей, халяль-имплантов и купола-мечети.",
      "Создана база для сценариев религиозной нейтральности и речной логистики."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Татарский хаб",
        "body": "- «Волга-порт»: ключевой речной транзитный узел.\n- «Кремль 3.0»: исторический центр с нейро-музеями.\n- «Иннополис расширение»: автономный IT-город-эксперимент.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Мультикультурный протокол",
        "body": "- «Двуязычные сети»: интеграция русско-татарской инфраструктуры.\n- «Халяль-импланты»: технологии, совместимые с религиозными нормами.\n- «Волжские дроны»: речная логистика на автономных платформах.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Восточный щит",
        "body": "- «Урал-Волга коридор»: защищённый маршрут обмена.\n- «Мусульманские серверы»: религиозно-нейтральные дата-центры.\n- «Купол-мечеть»: синтез архитектуры и технологической защиты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Региональная столица",
        "body": "- «Поволжская федерация»: объединение городов региона.\n- «Экспорт мультикультуризма»: тиражирование модели Казани.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет синтеза",
        "body": "- Экспорт протоколов мультикультурного управления и веротерпимости.\n- Презентация Поволжья как эталона интеграции культуры и технологий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Иннополис, мультикультурный протокол, купол-мечеть и поволжская федерация.\n- Сюжеты о религиозно совместимых имплантах и двуязычных сетях.\n",
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
    "github_issue": 1263,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Конвертация авторских событий Казани в структурированный YAML."
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