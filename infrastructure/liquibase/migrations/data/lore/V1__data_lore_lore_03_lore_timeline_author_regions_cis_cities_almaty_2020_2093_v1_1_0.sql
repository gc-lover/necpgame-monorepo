-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\almaty-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.415780

BEGIN;

-- Lore: canon-lore-cis-almaty-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-cis-almaty-2020-2093',
        'Алматы 2020-2093 — Степной ретрансляционный хаб',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-lore-cis-almaty-2020-2093",
        "title": "Алматы 2020-2093 — Степной ретрансляционный хаб",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "approved",
        "version": "1.1.0",
        "last_updated": "2025-11-23T03:55:00+00:00",
        "concept_approved": true,
        "concept_reviewed_at": "2025-11-12T00:45:00+00:00",
        "owners": [
          {
            "role": "narrative_team",
            "contact": "narrative@necp.game"
          }
        ],
        "tags": [
          "regions",
          "cis",
          "almaty"
        ],
        "topics": [
          "timeline-author",
          "logistics",
          "aerospace"
        ],
        "related_systems": [
          "narrative-service",
          "world-service",
          "economy-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-cis-2020-2093",
            "relation": "references"
          },
          {
            "id": "canon-lore-regions-asia-2020-2093",
            "relation": "complements"
          },
          {
            "id": "github-issue-1265",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1265",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T03:55:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/almaty-2020-2093.md",
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
        "problem": "Хронология Алматы была списком эпох без системного описания степной логистики, космопорта и кочевых сетей.",
        "goal": "Перевести досье в формат 1.1.0, связав горные дата-центры, магистрали ЦентрАзии, орбитальный проект и номадские сети.",
        "essence": "«Алматы — степной ретрансляционный узел, где горные серверы, кибер-юрт сети и космопорт управляют потоками между степью и орбитой».",
        "key_points": [
          {
            "Структурированы пять эпох 2020–2093 с драйверами": "горный узел, ЦентрАзия-хаб, Red+ буфер, федерация, экспорт “пакета степей”."
          },
          {
            "Добавлены системные крючки для world/economy": "степные ретрансляторы, космический лифт, многоязычные протоколы торговли."
          },
          "Описаны конфликты между номадскими кланами, корпорациями космопорта и политическим проектом “Пять станов”."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "regional_profile",
            "title": "Параметры региона",
            "body": "Алматы (~4 млн) расположен у подножия Заилийского Алатау; климат резко континентальный, с резкими перепадами температуры.\nЭкономика сочетает высокогорные дата-центры, маглев-логистику, космопорт и сервисы обслуживания орбиты.\nНаследие кочевой культуры интегрировано в цифровой уклад — кибер-юрты, мобильные бары и рынки.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "timeline_eras",
            "title": "Хронология 2020–2093",
            "body": "2020–2029 — «Горный узел»: дата-центры в горах, Шёлковый путь 3.0, Медео-станция исследований.\n2030–2039 — «ЦентрАзия-хаб»: Каспийско-тихоокеанский коридор, космопорт и кибер-юрт сети.\n2040–2060 — «Red+ Степной буфер»: ретрансляторы через степь, горные убежища и многоязычные коммуникации.\n2061–2077 — «Пять станов»: политическая федерация, проект космического лифта и координация кланов.\n2078–2093 — «Пакет степей»: экспорт протоколов управления расстояниями и климатами.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "systems_hooks",
            "title": "Системные крючки",
            "body": "world-service: погодные события степи, песчаные бури, влияние высоты на логистику; “космический лифт” как мировое событие.\neconomy-service: тарифы на степные ретрансляторы, экспорт холодной энергии из горных серверов, лицензии космопорта.\nnarrative-service: конфликты за управление маглевами, миссии в кибер-юрт сетях, защита космического лифта.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "faction_map",
            "title": "Фракции и напряжение",
            "body": "«Казахстанская орбитальная корпорация» — контролирует космопорт и проект лифта.\n«Номадские кланы» — владеют мобильными сетями, требуют долю в ретрансляторах.\n«ЦентрАзия администрация» — политический орган, балансирующий интересы республик и поддерживающий “Пакет степей”.\nКонфликты: доступ к орбитальным слотам, контроль за кибер-юртовыми сетями, распределение прибыли по маршруту Каспий–Тихий океан.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "content_opportunities",
            "title": "Контентные возможности",
            "body": "PvE-конвои через степь, требующие синхронизации с ретрансляторами и защитой от песчаных бурь.\nСоциальные миссии с номадскими кланами: дипломатия, обмен культурными артефактами, защита кибер-юртов.\nСпецоперации космопорта: предотвращение саботажа проекта космического лифта, квесты по обслуживанию орбитальных станций.\n",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {
        "glossary": [],
        "references": [
          {
            "title": "Central Asia Logistics",
            "link": "../../timeline/timeline-gameplay-integration.yaml"
          }
        ],
        "decisions": []
      },
      "implementation": {
        "github_issue": 1265,
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
          "changes": "Переведено в шаблон 1.1.0, добавлены системные крючки, фракции и контентные сценарии."
        },
        {
          "version": "0.2.0",
          "date": "2025-11-11",
          "author": "narrative_team",
          "changes": "Конвертация авторских событий Алматы в структурированный YAML."
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