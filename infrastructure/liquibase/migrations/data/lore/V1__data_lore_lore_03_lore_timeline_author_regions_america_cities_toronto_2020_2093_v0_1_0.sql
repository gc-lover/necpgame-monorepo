-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\toronto-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.313209

BEGIN;

-- Lore: canon-lore-america-toronto-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-toronto-2020-2093',
    'Торонто — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-toronto-2020-2093",
    "title": "Торонто — авторские события 2020–2093",
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
      "america",
      "toronto"
    ],
    "topics": [
      "timeline-author",
      "northlands"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/toronto-2020-2093.md",
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
    "problem": "Таймлайн Торонто в Markdown не связывал мультикультурные протоколы, климатическое убежище и северную федерацию.",
    "goal": "Описать путь Торонто от мультикультурного узла до экспортёра северных социальных стандартов.",
    "essence": "Торонто строит PATH-горизонт, принимает климатических беженцев и превращает север в упреждающий «пакет севера».",
    "key_points": [
      "Этапы от CN-ретранслятора до северной федерации и универсального базового импланта.",
      {
        "Хуки": "мультикультурные протоколы, PATH-расширение, климатические беженцы, арктические экспедиции, канадский стандарт."
      },
      "Сценарии о северной безопасности и социальной инженерии."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Канадская мультикультура",
        "body": "- «CN-башня ретранслятор»: ключевой узел связи.\n- «Мультикультурные протоколы»: интеграция диаспор.\n- «Онтарио-серверы»: холодный климат для охлаждения дата-центров.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Северный щит",
        "body": "- «Граница НСША»: нейтральная зона и конфликты.\n- «Зимние технологии»: специализация на холодном климате.\n- «PATH расширение»: подземный город растёт.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Убежище севера",
        "body": "- «Климатические беженцы»: Торонто как новый дом для переселенцев.\n- «Квебек-Онтарио союз»: восточноканадский альянс.\n- «Арктические экспедиции»: контроль Северного прохода.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Северная федерация",
        "body": "- «Канадский стандарт»: мирная альтернатива НСША.\n- «Универсальный базовый имплант»: социальная программа.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет севера",
        "body": "- Экспорт протоколов социального государства и северной инфраструктуры.\n- Торонто становится штабом северных коалиций.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- PATH, климатические беженцы, арктические экспедиции, универсальный базовый имплант, северная федерация.\n- Сюжеты о диаспорах и северной политике.\n",
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
    "github_issue": 1277,
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
      "changes": "Конвертация авторских событий Торонто в структурированный YAML."
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