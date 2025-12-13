-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\accra-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.292838

BEGIN;

-- Lore: canon-lore-africa-accra-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-accra-2020-2093',
    'Аккра — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-accra-2020-2093",
    "title": "Аккра — авторские события 2020–2093",
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
      "accra"
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
        "id": "github-issue-1306",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1306",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:10:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/accra-2020-2093.md",
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
    "problem": "Сценарии Аккры сохранялись в Markdown и не позволяли системно подключать побережные и логистические арки.",
    "goal": "Перенести ключевые эпохи города в структурированный YAML, выделив технологии побережья и культурный экспорт.",
    "essence": "Аккра развивает водные стартапы, шторм-барьеры и автономную логистику, распространяя протоколы западноафриканского побережья.",
    "key_points": [
      "Этапы от водного ренессанса до экспорта «пакета залива».",
      "Акценты на аква-тех, дорогах-дронах и защите побережья.",
      "Подготовлены хуки для сюжетов об энергетике, штормовых барьерах и культурной экспансии."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Западноафриканский ренессанс",
        "body": "- «Аква-тех»: водные стартапы и очистка.\n- «Электро-кооперативы»: DAO коммунальных сетей.\n- «Культурные хабы»: хайлайф и афробит как цифровой контент.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Гвинейский коридор",
        "body": "- «Порты какао 3.0»: агро-логистика экспортных цепочек.\n- «Солнечные побережья»: энергетические фермы на берегу.\n- «Дороги-дроны»: автономные перевозки по воздуху.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Побережная крепость",
        "body": "- «Шторм-барьеры»: защита побережья от наводнений.\n- «Подземные центры»: защищённые серверные на случай тайфунов.\n- «Рыбные био-реакторы»: устойчивые источники энергии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Западноафриканский узел",
        "body": "- «Аккра—Лагос»: тех-ось региона для торговли и культурного обмена.\n- «Культурный экспорт»: афро-киберпанк и фестивали нового формата.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет залива",
        "body": "- Экспорт протоколов побережных городов по защите, логистике и культуре.\n- Аккра становится эталоном берегового мегаполиса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Аква-тех, дороги-дроны, шторм-барьеры, рыбные био-реакторы.\n- Сюжеты о культурном экспорте и кооперативных экономических DAO.\n",
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
    "github_issue": 1306,
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
      "changes": "Конвертация авторских событий Аккры в структурированный YAML."
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