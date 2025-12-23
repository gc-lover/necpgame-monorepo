-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\moscow-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.442442

BEGIN;

-- Lore: canon-region-cis-moscow-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-cis-moscow-2020-2093',
        'Москва 2020-2093 — Досье мегаполиса',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-cis-moscow-2020-2093",
        "title": "Москва 2020-2093 — Досье мегаполиса",
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
          "moscow",
          "winter-logistics"
        ],
        "topics": [
          "regional-history",
          "logistics",
          "urban-security"
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
            "id": "github-issue-1261",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1261",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:00:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/moscow-2020-2093.md",
        "visibility": "internal",
        "audience": [
          "lore",
          "narrative",
          "systems"
        ],
        "risk_level": "high"
      },
      "review": {
        "chain": [
          {
            "role": "narrative_lead",
            "reviewer": "",
            "reviewed_at": "",
            "status": "pending"
          },
          {
            "role": "security_director",
            "reviewer": "",
            "reviewed_at": "",
            "status": "pending"
          }
        ],
        "next_actions": []
      },
      "summary": {
        "problem": "Материал о Москве не связывал купольные уставы, подземную логистику и зимние окна в один сценарный каркас.",
        "goal": "Перевести досье мегаполиса на шаблон 1.1.0, акцентируя конфликт легальных и серых маршрутов, защиту ремесленников и стратегию зимних коридоров.",
        "essence": "«Купольная Москва балансирует между кодексами ремесленников, серой логистикой и сезонными окнами через Blackwall».",
        "key_points": [
          "Выстроена пятиэтапная хронология от формирования архологии до экспорта “московского пакета” правил.",
          "Системные крючки описывают зимние окна, подземные магистрали и нейтральные зоны для escort/convoy-геймплея.",
          {
            "Уточнены фракции": "городские уставники, логистические консорциумы и серые артели, задающие точки напряжения."
          }
        ]
      },
      "content": {
        "sections": [
          {
            "id": "city_profile",
            "title": "Параметры мегаполиса",
            "body": "Москва — купольный мегаполис (~15 млн), сочетающий многоуровневую архологию центра и сеть подземных магистралей.\nКлимат: суровые зимы, регулярные снежные штормы; поверхностные маршруты закрываются, основная логистика уходит под землю.\nКлючевые опоры: распределённая идентификация граждан, “фиксёрские станции” для транзитных контрактов, купольные уставы безопасности.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "timeline_eras",
            "title": "Хронология 2020–2093",
            "body": "2020–2029 — Архология центра и многоуровневая идентификация доступа к куполам.\n2030–2039 — «Карта подземки»: строительство скрытых магистралей и узлов «Стык-города».\n2040–2060 — Эпоха Red+ и “зимние окна” безопасности через Blackwall, развитие сезонных конвоев.\n2061–2077 — Купольные уставы и “кодекс ремесленников”, защита рипдоков и независимых техников.\n2078–2093 — «Московский пакет»: экспорт кодексов, нейтралитет конвоев и стандарты зимних маршрутов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "systems_hooks",
            "title": "Системные крючки",
            "body": "world-service: динамические погодные события, влияние купольных барьеров, закрытие/открытие зимних окон.\neconomy-service: тарифы на использование подземных магистралей, лицензии на нейтральные “Белые флаги”, серый рынок логистики.\nnarrative-service: миссии по сопровождению караванов, защита ремесленников, расследования в “невидимых слоях”.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "faction_dynamics",
            "title": "Фракции и напряжение",
            "body": "«Купольные уставники» — муниципалитет + корпорации, управляющие кодексами безопасности.\n«Серые артели» — независимые логисты и фиксёры, контролирующие скрытые маршруты и “невидимые слои”.\n«Ремесленный союз» — рипдоки и техники, добивающиеся защиты через кодекс ремесленников.\nКризисы: слом идентификации купола, саботаж зимних окон, войны за контроль над узлами “Стык-города”.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "content_opportunities",
            "title": "Контентные возможности",
            "body": "Конвойные PvE-сценарии по зимним окнам, где игрок взаимодействует с “Белыми флагами” и ремесленным союзом.\nСоциальные квесты в Архологии: доступ к закрытым уровням, переговоры с уставниками, раскрытие серых граждан.\nСтелс-операции в подземных магистралях, направленные на перехват грузов и восстановление сетевых маршрутов.\n",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {
        "glossary": [],
        "references": [
          {
            "title": "Red+ Era Moscow Events",
            "link": "../../timeline/2040-2060-red-time.yaml"
          },
          {
            "title": "Underground Logistics Memo",
            "link": "../../timeline/timeline-gameplay-integration.yaml"
          }
        ],
        "decisions": []
      },
      "implementation": {
        "github_issue": 1261,
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
          "changes": "Переведено в шаблон 1.1.0, добавлены фракции, системные хуки и сценарные возможности."
        },
        {
          "version": "0.2.0",
          "date": "2025-11-11",
          "author": "narrative_team",
          "changes": "Перенос авторского описания Москвы в структурированный YAML."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        1) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;