-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\kinshasa-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.197046

BEGIN;

-- Lore: canon-lore-africa-kinshasa-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-africa-kinshasa-2020-2093',
        'Киншаса — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-africa-kinshasa-2020-2093",
        "title": "Киншаса — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:20:00+00:00",
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
          "kinshasa"
        ],
        "topics": [
          "timeline-author",
          "resources"
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
            "id": "github-issue-1296",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1296",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:20:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/kinshasa-2020-2093.md",
        "visibility": "internal",
        "audience": [
          "narrative",
          "worldbuilding",
          "live_ops"
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
          }
        ],
        "next_actions": []
      },
      "summary": {
        "problem": "Конверсия Киншасы в YAML отсутствовала; ресурсы Конго, джунгли-серверы и корпоративные войны не были связаны.",
        "goal": "Структурировать путь города от ресурсного узла до экспорта «пакета джунглей».",
        "essence": "Киншаса контролирует редкоземы, биоразнообразие и племенные протоколы, превращая джунгли в стратегический актив.",
        "key_points": [
          "Этапы от сердца Африки до центральноафриканского союза и экспорта био-технологий.",
          {
            "Хуки": "конго-ресурсы, джунгли-серверы, корпоративные войны, экологические конфликты."
          },
          "Сюжеты о борьбе за месторождения, биоразнообразии и политическом объединении Конго."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Сердце Африки",
            "body": "- «Конго-ресурсы»: редкоземельные металлы.\n- «Речные хабы»: логистика по Конго.\n- «Корпоративные войны»: борьба за месторождения.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Зелёная революция",
            "body": "- «Джунгли-серверы»: дата-центры в тропиках.\n- «Био-разнообразие»: генетические лаборатории.\n- «Племенные протоколы»: интеграция традиций.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Ресурсная крепость",
            "body": "- «Защита месторождений»: военизированные зоны.\n- «Экологические войны»: конфликты за джунгли.\n- «Подземные шахты 2.0»: автоматизированная добыча.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Центральноафриканский союз",
            "body": "- «Федерация Конго»: политическое объединение.\n- «Экспорт ресурсов»: монополия на редкоземы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет джунглей",
            "body": "- Экспорт био-технологий и протоколов ресурсных территорий.\n- Киншаса становится лабораторией устойчивого использования джунглей.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Конго-ресурсы, джунгли-серверы, племенные протоколы, экологические войны, подземные шахты 2.0.\n- Сюжеты о биоразнообразии, корпорациях и политике Центральной Африки.\n",
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
        "github_issue": 1296,
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
          "changes": "Конвертация авторских событий Киншасы в структурированный YAML."
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