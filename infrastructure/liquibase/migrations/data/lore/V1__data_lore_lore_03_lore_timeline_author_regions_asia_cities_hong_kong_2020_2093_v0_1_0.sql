-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\hong-kong-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.629909

BEGIN;

-- Lore: canon-lore-asia-hong-kong-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-hong-kong-2020-2093',
    'Гонконг — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-hong-kong-2020-2093",
    "title": "Гонконг — авторские события 2020–2093",
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
      "asia",
      "hong-kong"
    ],
    "topics": [
      "timeline-author",
      "finance"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/hong-kong-2020-2093.md",
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
    "problem": "Таймлайн Гонконга находился в Markdown и не покрывал финансовые и анклавные механики в базе знаний.",
    "goal": "Структурировать ключевые эпохи вертикального мегаполиса и его роль свободной гавани.",
    "essence": "Гонконг превращает Коулун 2.0, протестные сети и крипто-столицу в экспортируемый «пакет портов».",
    "key_points": [
      "Выделены пять эпох от вертикального лабиринта до глобального финансового пакета.",
      {
        "Подчёркнуты хуки": "Виктория-харбор хабы, протестные сети, крипто-столица, триады 2.0."
      },
      "Подготовлена база для сюжетов о финансовых протоколах и безопасных анклавных системах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Вертикальный лабиринт",
        "body": "- «Коулун 2.0»: цифровые трущобы с плотностью имплантов.\n- «Виктория-харбор хабы»: морские дата-центры.\n- «Протестные сети»: зашифрованная инфраструктура активистов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Две системы, одно киберпространство",
        "body": "- «Материковая граница»: файервол между системами.\n- «Финансовые алгоритмы»: AI-трейдинг и автоматизация биржи.\n- «Ночные рынки»: крупнейшие BD-базары Азии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Свободная гавань 2.0",
        "body": "- «Цифровое убежище»: убежище для корпоративных беженцев.\n- «Вертикальные фермы»: продовольственная независимость.\n- «Подземные тоннели»: скрытая инфраструктура под городом.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Финансовое сердце Азии",
        "body": "- «Крипто-столица»: центр цифровой валюты и деривативов.\n- «Триады 2.0»: киберпреступные синдикаты с глобальным охватом.\n- «Судебный анклав»: нейтральная зона для международных сделок.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет портов",
        "body": "- Экспорт финансовых протоколов и анклавных систем.\n- Гонконг становится эталоном свободных портов будущего.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Коулун 2.0, цифровое убежище, триады 2.0, крипто-столица, вертикальные фермы.\n- Сюжеты о протестных сетях и финансовых анкладах.\n",
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
    "github_issue": 1272,
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
      "changes": "Конвертация авторских событий Гонконга в структурированный YAML."
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