-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\novosibirsk-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.713552

BEGIN;

-- Lore: canon-region-cis-novosibirsk-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-cis-novosibirsk-2020-2093',
    'Новосибирск 2020-2093 — Сибирский технополис',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-cis-novosibirsk-2020-2093",
    "title": "Новосибирск 2020-2093 — Сибирский технополис",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "approved",
    "version": "1.1.0",
    "last_updated": "2025-11-23T04:00:00+00:00",
    "concept_approved": true,
    "concept_reviewed_at": "2025-11-12T00:45:00+00:00",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "cis",
      "novosibirsk",
      "cold-chain"
    ],
    "topics": [
      "regional-history",
      "climate-engineering",
      "logistics"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-cis-index",
        "relation": "references"
      },
      {
        "id": "github-issue-1259",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1259",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:00:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/novosibirsk-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative",
      "systems"
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
    "problem": "Городской профиль Новосибирска был разбит на заметки о разных эпохах и не связывал научные хабы, зимнюю логистику и автономные общины в единую систему.",
    "goal": "Сформулировать последовательную эволюцию города 2020–2093, показать как криохранилища, зимние окна и Академгородок 2.0 поддерживают сюжеты науки и холодовой логистики.",
    "essence": "«Академгородок 2.0 управляет северным коридором знаний, используя вечную мерзлоту как самый надёжный сейф данных».",
    "key_points": [
      {
        "Структурированы пять эпох с указанием драйверов": "научный центр, восточный коридор, ледяной щит, автономная республика и экспорт “пакета вечности”."
      },
      "Описаны механики криохранилищ, зимних окон, подземных каналов и автономных коммун для систем narrative/world/economy.",
      "Добавлены хуки для исследовательских, конвойных и survival-сценариев в экстремальном климате."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "city_profile",
        "title": "Параметры региона",
        "body": "Новосибирск позиционируется как сибирский научно-логистический хаб с населением ~6 млн, двумя уровнями городского ядра и сетью холодовых дата-центров вдоль Оби.\nКлимат — арктический континентальный: круглогодичный пермамфрост, снежные бури, короткие «зимние окна» спокойствия.\nКлючевые сферы: фундаментальные исследования (Академгородок 2.0), холодовая логистика, криохранилища биоматериалов и цифровых следов, маршруты между Европой и Азией.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_eras",
        "title": "Хронология 2020–2093",
        "body": "2020–2029 — «Академгородок 2.0»: обновлённые кампусы, речные дата-хабы и транссибирский узел распределения.\n2030–2039 — «Сибирский коридор»: строительство подземных логистических тоннелей и формирование зимних технологий выживания.\n2040–2060 — «Ледяной щит Red+»: появление криохранилищ, сезонных обменов через Blackwall и сетей автономных коммун.\n2061–2077 — «Сибирская республика»: статус де-факто автономии, баланс корпораций, государства и научных синдикатов.\n2078–2093 — «Пакет вечности»: экспорт протоколов долговременного хранения данных и образцов, тиражирование технологий вечной мерзлоты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "systems_hooks",
        "title": "Системные крючки",
        "body": "world-service: климатические модификаторы, требующие подготовки к бурям и обледенению; влияние вечной мерзлоты на передвижение.\neconomy-service: холодовая логистика, premium-тарифы за хранение и транспортировку данных/биоматериалов, трейды между Азия+Европа.\nnarrative-service: экспедиции в автономные научные коммуны, расследования утечек криохранилищ, escort-миссии по зимним окнам.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "faction_dynamics",
        "title": "Фракции и напряжение",
        "body": "«Сибирский консорциум» — союз университетов и исследовательских корпораций; держит контроль над протоколами хранения.\n«Ледяные конвои» — независимые логисты, обеспечивающие зимние перевозки; балансируют между серым и белым рынком.\n«Городская автономия» — муниципалитет и охранные отряды, которые распределяют доступ к подземным каналам и следят за “чистотой” данных.\nКонфликтные зоны: утечка криоархивов, саботаж зимних окон, давление мегакорпораций на научные лицензии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "opportunities",
        "title": "Ключевые события и контентные возможности",
        "body": "Боевые и стелс-миссии в подземных тоннелях «Стык-города» с динамическими климатическими эффектами.\nСоциальные сюжеты в Академгородке 2.0 — рекрутинг учёных, протечки ноосферных данных, хакерские дуэли за доступ к архивам.\nКонвойные и survival-сценарии по зимним окнам, требующие подготовки оборудования и взаимодействия с автономными коммуннами.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      {
        "title": "Master Timeline CIS",
        "link": "../../timeline/MASTER-TIMELINE-INDEX.yaml"
      },
      {
        "title": "Red+ Era Overview",
        "link": "../../timeline/2040-2060-red-time.yaml"
      }
    ],
    "decisions": []
  },
  "implementation": {
    "github_issue": 1259,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "1.1.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Переведено в шаблон 1.1.0, добавлены системные хуки, фракционная карта и контентные возможности."
    },
    {
      "version": "0.2.0",
      "date": "2025-11-11",
      "author": "narrative_team",
      "changes": "Конвертация событий Новосибирска в YAML и выделение механик."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    1
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;