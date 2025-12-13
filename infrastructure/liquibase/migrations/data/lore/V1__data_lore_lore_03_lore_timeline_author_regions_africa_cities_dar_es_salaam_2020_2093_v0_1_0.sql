-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\dar-es-salaam-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.330709

BEGIN;

-- Lore: canon-lore-africa-dar-es-salaam-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-dar-es-salaam-2020-2093',
    'Дар-эс-Салам — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-dar-es-salaam-2020-2093",
    "title": "Дар-эс-Салам — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:15:00+00:00",
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
      "dar-es-salaam"
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
        "id": "github-issue-1299",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1299",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:15:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/dar-es-salaam-2020-2093.md",
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
    "problem": "Описание Дар-эс-Салама находилось в Markdown и не позволяла использовать восточноафриканские логистические хуки в знаниях.",
    "goal": "Формализовать эпохи порта Индийского океана с акцентом на суахили-культуру, климатическую адаптацию и федерацию Восточной Африки.",
    "essence": "Дар-эс-Салам превращает побережье в портовую крепость с океанскими серверами, коралловыми фермами и экспортом «пакета побережья».",
    "key_points": [
      "Этапы от портовой логистики до восточноафриканской федерации.",
      "Подчёркнуты океанские серверы, коралловые фермы и суахили-неон.",
      "Созданы хуки для сюжетов об EAC и торговых протоколах Индийского океана."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Индийский океан хаб",
        "body": "- «Порт-логистика»: транзит Восток–Запад и автоматизация грузов.\n- «Суахили BD»: цифровые архивы языка и культуры.\n- «Занзибар-коридор»: островная сеть и логистика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Восточноафриканская столица",
        "body": "- «Дар-Найроби ось»: тех-магистраль региона.\n- «Морские платформы»: расширение города в океан.\n- «Климат-адаптация»: решения против жары и влажности.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Портовая крепость",
        "body": "- «Океанские серверы»: дата-центры с морским охлаждением.\n- «Коралловые фермы»: биопроцессоры и восстановление рифов.\n- «Торговые протоколы»: стандарты Индийского океана.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Восточноафриканская федерация",
        "body": "- «EAC-сеть»: объединение государств региона.\n- «Культурный экспорт»: суахили-неон и творческие индустрии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет побережья",
        "body": "- Экспорт протоколов портовых мегаполисов и климатической адаптации.\n- Дар-эс-Салам как стандарт безопасности и устойчивости побережий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Океанские серверы, коралловые фермы, торговые протоколы, суахили-неон.\n- Сюжеты о Занзибар-коридоре и климат-адаптации.\n",
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
    "github_issue": 1299,
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
      "changes": "Конвертация авторских событий Дар-эс-Салама в структурированный YAML."
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