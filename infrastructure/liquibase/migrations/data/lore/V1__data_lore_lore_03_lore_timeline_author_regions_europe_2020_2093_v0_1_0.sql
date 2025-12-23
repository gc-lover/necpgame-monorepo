-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.609894

BEGIN;

-- Lore: canon-lore-regions-europe-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-regions-europe-2020-2093',
        'Европа 2020–2093 — авторские события',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-lore-regions-europe-2020-2093",
        "title": "Европа 2020–2093 — авторские события",
        "document_type": "canon",
        "category": "timeline-author",
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
          "europe",
          "timeline"
        ],
        "topics": [
          "timeline-author",
          "city-states",
          "media"
        ],
        "related_systems": [
          "narrative-service",
          "world-service",
          "economy-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-2045-2060-author-events",
            "relation": "references"
          },
          {
            "id": "canon-lore-2078-2090-author-events",
            "relation": "references"
          },
          {
            "id": "canon-lore-regions-cis-2020-2093",
            "relation": "complements"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe-2020-2093.md",
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
        "problem": "Региональный таймлайн Европы находился в Markdown и не связывался с механиками пан-еврогрида и экспортируемых пакетов правил.",
        "goal": "Перенести события Европы в YAML для интеграции с сетевыми стандартами, прокси-конфликтами и параметрикой мира.",
        "essence": "Европейские мегаполисы эволюционируют от корпоративных хартий к купольным альянсам и экспорту параметров перед перезапуском лиг.",
        "key_points": [
          "Корпоративные хартии и дата-вены Балкан формируют инфраструктуру 2020-х.",
          "Пан-ЕвроГрид и кибер-караваны задают миграцию и логистику 2030-х.",
          "Эпоха Red+ создаёт сеть городов-государств с тёплыми коридорами и сетевыми щитами.",
          "Нео-союзы 2060-х синхронизируют безопасность, медиарегулирование и прокси-войны.",
          "2080-е экспортируют переносимые пакеты правил и проводят карнавалы параметров."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "timeline_2020_2029",
            "title": "2020–2029 — корпоративные харты и города-узлы",
            "body": "- «Лондонский устав хартии»: корпоративные квартальные юрисдикции вокруг Канэри-Уорф.\n- «Парижские Катакомбы Красного рынка»: подземные ярмарки BD и софт-артефактов с нейроэтикой кураторов.\n- «Балканские датавены»: приграничные хранилища и маршруты экстракции под контролем кланов фиксеров.\n",
            "mechanics_links": [
              "mechanics/world/events/world-events-framework.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2030_2039",
            "title": "2030–2039 — Пан-ЕвроГрид и миграции",
            "body": "- «Пан-ЕвроГрид 1.0» снижает трение торговых протоколов, открывая новые эксплойты.\n- Кибер-караваны беженцев и номадов снабжают порты Роттердама и Антверпена.\n- «Рейн-Рур неон-спирс» ведут рейды за слотами пропускной способности.\n",
            "mechanics_links": [
              "mechanics/world/events/live-events-system.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2040_2060",
            "title": "2040–2060 — Red+: сеть городов-государств",
            "body": "- «Черное Гданьское кольцо» охотится на чёрных провайдеров тёплых коридоров.\n- «Сканди блок-щиты» создают сетевые арморы коммун.\n- «Медитеран протокол» обеспечивает морской обмен оффлайн-пакетами.\n",
            "mechanics_links": [
              "mechanics/economy/economy-logistics.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2061_2077",
            "title": "2061–2077 — нео-союзы и прокси-конфликты",
            "body": "- Альянс куполов синхронизирует режимы безопасности и страховки рейдов.\n- «Серые театры» ведут прокси-войны за офшорные дата-хабы.\n- Медиа-квоты ограничивают ИИ-пропаганду, вызывая подпольные BD-студии.\n",
            "mechanics_links": [
              "mechanics/economy/economy-contracts.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2078_2093",
            "title": "2078–2093 — параметрики мира и экспорт правил",
            "body": "- Евро-конституции лиг публикуют переносимые пакеты городских правил.\n- «Карнавал параметров» в Венеции тестирует погоду, экономику и патрули.\n- «Архив Артемиды» ведёт экспедиции за Blackwall для выкупа культурных артефактов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics_and_hooks",
            "title": "Ключевые механики и хуки",
            "body": "- Совместимые сетевые стандарты, купольные голосования, страховки рейдов и экспорт правил.\n- Хуки: охота на чёрных провайдеров, суд по медиаквотам, экспедиция архива Артемиды.\n",
            "mechanics_links": [
              "mechanics/world/events/world-events-framework.yaml"
            ],
            "assets": []
          }
        ]
      },
      "appendix": {
        "glossary": [],
        "references": [
          "timeline-author/2045-2060-author-events.md",
          "timeline-author/2078-2090-author-events.md"
        ],
        "decisions": []
      },
      "implementation": {
        "needs_task": false,
        "github_issue": 73,
        "queue_reference": [
          "shared/trackers/queues/concept/queued.yaml#canon-lore-regions-europe-2020-2093"
        ],
        "blockers": []
      },
      "history": [
        {
          "version": "0.1.0",
          "date": "2025-11-12",
          "author": "narrative_team",
          "changes": "Конвертирован региональный таймлайн Европы в YAML и связан с механиками купольных альянсов и экспорта правил."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        0) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;