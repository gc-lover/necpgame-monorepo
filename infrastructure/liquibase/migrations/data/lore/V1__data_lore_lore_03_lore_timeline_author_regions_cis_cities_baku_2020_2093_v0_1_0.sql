-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\baku-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.421685

BEGIN;

-- Lore: canon-lore-cis-baku-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-cis-baku-2020-2093',
        'Баку — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-cis-baku-2020-2093",
        "title": "Баку — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T03:55:00+00:00",
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
          "baku"
        ],
        "topics": [
          "timeline-author",
          "trade"
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
            "id": "github-issue-1264",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1264",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T03:55:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/baku-2020-2093.md",
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
        "problem": "Таймлайн Баку в Markdown не позволял увязать каспийские логистические и культурные арки с игровыми механиками.",
        "goal": "Структурировать пятнадцатилетние эпохи Баку как каспийского мегахаба и закрепить ключевые хуки.",
        "essence": "Баку балансирует между нефтяным наследием и цифровым каспийским узлом, экспортируя набор протоколов для водных городов.",
        "key_points": [
          "Выделены пять эпох от огненной земли до экспорта каспийского пакета.",
          "Зафиксированы хуки для AR-символов, водных дата-центров и кибер-гонок.",
          "Подготовлена база для сюжетов о региональных альянсах и логистике."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Огненная земля 2.0",
            "body": "- «Пламенные башни AR»: цифровой символ города.\n- «Нефтегаз-данные транзит»: переход от нефти к технологическим потокам.\n- «Каспийские порты»: смешанная оффлайн-логистика.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Каспийский хаб",
            "body": "- «Транскаспийский коридор»: мост между Европой и Азией.\n- «Формула 1 кибер»: гибрид гоночной культуры и киберспорта.\n- «Подземный Баку»: расширенная система метро для логистики.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Южнокавказская крепость",
            "body": "- «Каспий-серверы»: дата-центры и гидроплатформы на воде.\n- «Кавказский альянс»: попытка регионального объединения.\n- «Нефтяное наследие»: конверсия постиндустриальных зон.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Региональная столица",
            "body": "- «Азербайджан-Грузия коридор»: технологическая ось.\n- «Культурный экспорт»: продвижение каспийского неона и брендов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет Каспия",
            "body": "- Экспорт управленческих протоколов каспийских портовых городов.\n- Распространение стандартов водной логистики и культурного бренда.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Пламенные башни AR, каспий-серверы, кибер-гонки и нефтяные руины.\n- Конфликты вокруг регионального альянса и экспорта водных протоколов.\n",
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
        "github_issue": 1264,
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
          "changes": "Конвертация авторских событий Баку в структурированный YAML."
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