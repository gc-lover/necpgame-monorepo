-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\minsk-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.436877

BEGIN;

-- Lore: canon-lore-cis-minsk-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-cis-minsk-2020-2093',
        'Минск — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-cis-minsk-2020-2093",
        "title": "Минск — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:00:00+00:00",
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
          "minsk"
        ],
        "topics": [
          "timeline-author",
          "eco-tech"
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
            "id": "canon-lore-regions-europe-2020-2093",
            "relation": "complements"
          },
          {
            "id": "github-issue-1260",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1260",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:00:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/minsk-2020-2093.md",
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
        "problem": "Минский таймлайн лежал в Markdown и не обеспечивал доступ к эко-технологическим хукам для сценаристов.",
        "goal": "Оцифровать ключевые эпохи Минска как зелёного тех-города и подготовить материалы для квестов.",
        "essence": "Минск превращает парк высоких технологий и лесные архивы в устойчивый эко-хаб с партизанскими сетями.",
        "key_points": [
          "Структурированы эпохи от IT-страны до экспорта зелёного пакета.",
          "Зафиксированы хуки для пущанских серверов, зелёных протоколов и партизанских сетей.",
          "Подготовлена база для сюжетов о нейтральном коридоре и био-совместимости."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — IT-страна",
            "body": "- «Парк высоких технологий 2.0»: беларусский кремниевый лес.\n- «Немига-андеграунд»: подпольная киберкультура и серые рынки.\n- «Свислочь-хабы»: речные дата-центры в городской черте.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Нейтральный коридор",
            "body": "- «Балтийско-черноморский путь»: транзит Север-Юг.\n- «Ремесленные кооперативы»: кастомные импланты и локальная производственная сеть.\n- «Беловежские архивы»: экологические дата-резервации.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Лесной щит",
            "body": "- «Пущанские серверы»: дата-центры в заповедниках.\n- «Зелёные протоколы»: стандарты эко-технологий.\n- «Партизанские сети»: децентрализованная инфраструктура и сопротивление.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Экологический центр",
            "body": "- «Зелёная столица»: устойчивый кибер-город.\n- «Био-совместимость»: интеграция природы и технологий.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет леса",
            "body": "- Экспорт эко-технологических протоколов и стандартов.\n- Распространение лесных моделей управления ресурсами.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- ПВТ 2.0, пущанские серверы, зелёные протоколы и партизанские сети.\n- Сценарии про нейтральный коридор и био-совместимость для мировых арок.\n",
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
        "github_issue": 1260,
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
          "changes": "Конвертация авторских событий Минска в структурированный YAML."
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