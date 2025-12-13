-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\cairo-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.305767

BEGIN;

-- Lore: canon-lore-africa-cairo-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-cairo-2020-2093',
    'Каир — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-cairo-2020-2093",
    "title": "Каир — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:10:00+00:00",
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
      "africa",
      "cairo"
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
        "id": "canon-lore-regions-africa-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-middle-east-2020-2093",
        "relation": "complements"
      },
      {
        "id": "github-issue-1304",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1304",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:10:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/cairo-2020-2093.md",
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
    "problem": "Таймлайн Каира в Markdown не позволял подключить логистические и культурные арки Египта к общей базе знаний.",
    "goal": "Оформить ключевые эпохи Каира с акцентом на Нил, Суэцкий канал и религиозные сервисы.",
    "essence": "Каир удерживает роль «вечного города», соединяя пирамиды AR, пустынную экспансию и протоколы хранения для будущих поколений.",
    "key_points": [
      "Эпохи от вечного города до экспорта «пакета вечности».",
      {
        "Выделены хуки": "пирамиды AR, фараоновы архивы, суэцкий коридор, нильская федерация."
      },
      "Сформированы сценарии для религиозных, логистических и энергетических систем."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Вечный город",
        "body": "- «Пирамиды AR»: древние чудеса с цифровыми слоями.\n- «Нил-хабы»: речная логистика и дата-центры.\n- «Новая столица»: административный город в пустыне.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Арабский хаб",
        "body": "- «Суэцкий коридор»: ключевой транзитный маршрут.\n- «Исламские серверы»: религиозно-совместимые технологии.\n- «Пустынные расширения»: серия новых городов в Сахаре.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Фараоновы архивы",
        "body": "- «Подземные хранилища»: дата-центры под пирамидами.\n- «Нильская федерация»: союз с Суданом и соседями.\n- «Солнечные фермы»: использование энергии Сахары.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Ворота Африки",
        "body": "- «Транс-африканский хаб»: соединение Севера и Юга.\n- «Культурный экспорт»: египетская эстетика и образовательные центры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет вечности",
        "body": "- Экспорт протоколов долгосрочного хранения и религиозных сервисов.\n- Каир закрепляется как страж истории и логистический центр.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Пирамиды AR, фараоновы архивы, суэцкий коридор, нильская федерация, пустынные расширения.\n- Квесты о религиозной совместимости технологий и пустынных мегаполисах.\n",
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
    "github_issue": 1304,
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
      "changes": "Конвертация авторских событий Каира в структурированный YAML."
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