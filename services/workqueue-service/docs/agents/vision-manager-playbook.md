# Плейбук Vision Manager

## Миссия
Vision Manager получает задачи только через `claim → submit`. Создавать задачи и обращаться к БД напрямую запрещено. Основная зона ответственности:
- уточнение видения и спецификаций (`/api/content/entities/{id}`, `/api/quests`, `/api/items`);
- подготовка handoff для API Task Architect (сегмент `api`);
- контроль auto-claim цепочки Vision → API → Backend.

## Предварительные условия
- Concept Director уже создал запись в `content_entities` и запустил ingest.
- Для агента `vision-manager` заведены preferences (см. `019-seed-core-data`).
- REST доступ настроен: `X-Agent-Role=vision-manager`.

## Быстрый чек окружения
```bash
docker compose -f docker-compose.workqueue.yml ps
docker exec workqueue-service curl -s http://localhost:8090/actuator/info
```

## Шаг 1. Claim задачи
Vision Manager не создаёт задачи вручную, он их забирает:
```bash
docker exec workqueue-service curl -s -X POST \
  http://localhost:8090/api/agents/tasks/claim \
  -H "Content-Type: application/json" \
  -H "X-Agent-Role: vision-manager" \
  -d '{"segments":["vision"],"priorityFloor":2}'
```
- `instructions.brief` подтягивается из `agent_briefs` (миграция `025`).
- `instructions.templates` содержит ссылки вида `/api/reference/templates/structure-guidelines`.
- `instructions.knowledgeRefs` уже отсылает к REST (`/api/content/entities/<uuid>`). Если встретишь файловый путь — задача устарела, отклони её.

## Шаг 2. Обновление контента
Vision Manager уточняет сущность через `PUT /api/content/entities/{id}` и специализированные CRUD.

### Пример обновления контента
```bash
docker exec workqueue-service curl -s -X PUT \
  http://localhost:8090/api/content/entities/<contentId> \
  -H "Content-Type: application/json" \
  -H "X-Agent-Role: vision-manager" \
  -d '{
        "code": "vision-beat-2025-q4",
        "title": "Q4 Vision Beat (detailed)",
        "summary": "Расширенный пакет требований.",
        "typeCode": "lore_entry",
        "statusCode": "approved",
        "visibilityCode": "internal",
        "version": "2025.11.1",
        "lastUpdated": "2025-11-15T13:45:00Z",
        "sourceDocument": "/api/content/entities/vision-beat-2025-q4",
        "tags": ["vision","beat","handoff"],
        "topics": ["story","api"],
        "metadata": {
          "audience": ["api-task-architect","backend-implementer"],
          "impact": "requires-new-endpoints"
        }
      }'
```

### Типизированные данные (квест/предмет и т.п.)
Если задача приводит к созданию квеста:
```bash
docker exec workqueue-service curl -s -X POST \
  http://localhost:8090/api/quests \
  -H "Content-Type: application/json" \
  -H "X-Agent-Role: vision-manager" \
  -d '{
        "contentCode": "vision-beat-2025-q4",
        "questType": "main",
        "categoryCode": "main_story",
        "difficulty": "story",
        "stages": [
          {
            "code": "stage-visualize",
            "title": "Визуализировать арку",
            "objectives": [{"type":"dialogue","description":"Подтвердить pitch"}]
          }
        ],
        "rewards": [],
        "worldEffects": []
      }'
```
Все CRUD-эндпоинты автоматически:
- логируют событие `content.updated`;
- пушат задачу в следующую очередь через `ContentTaskCoordinator`;
- добавляют `knowledgeRefs` `/api/quests/{contentId}` в payload handoff.

## Шаг 3. Submit
Vision Manager не передаёт задачи вручную — только через submit.

1. Подготовь metadata:
```json
{
  "restRefs": [
    "/api/content/entities/vision-beat-2025-q4",
    "/api/quests/vision-beat-2025-q4"
  ],
  "handoff": {
    "nextSegment": "api",
    "templates": ["structure-guidelines","concept-canon","knowledge-entry-template"]
  }
}
```
2. Отправь результат:
```bash
docker exec workqueue-service curl -s -X POST \
  http://localhost:8090/api/agents/tasks/<itemId>/submit \
  -H "X-Agent-Role: vision-manager" \
  -F 'payload={
        "notes":"Vision пакет передан в API",
        "artifacts":[{"title":"Vision spec","url":"https://docs/specs/vision-beat-2025-q4"}],
        "metadata":"{\"restRefs\":[\"/api/content/entities/vision-beat-2025-q4\"],\"handoff\":{\"nextSegment\":\"api\"}}"
      };type=application/json'
```
3. После `200 OK` убедись:
   - в ответе `nextSegment = "api"`;
   - `ActivityLog` содержит `content.queue_enqueued` с `nextSegment=api`;
  - `POST /api/agents/tasks/claim` (с `X-Agent-Role: api-task-architect`) возвращает новую задачу с тем же `externalRef`.

## Автоматическая защита
- Независимый `ingest` отключён для Vision Manager (`ingest.forbidden.agent`).
- Любая ссылка в `knowledgeRefs`, не начинающаяся с `/api/` или `http(s)` будет отклонена валидатором (`ingest.validation.knowledge_ref_missing`).
- `submission.not_owner` появится, если попытаться закрыть задачу чужого сегмента.

## Контрольные списки
- Перед submit вызови:
  - `GET /api/reference/templates/structure-guidelines`;
  - `GET /api/reference/templates/knowledge-entry-template`.
- Убедись, что все требования из `agent_briefs` выполнены: кратко опиши в `payload.notes`, приложи минимум один артефакт, добавь `metadata` с REST-ссылками.

## Диагностика цепочки
1. `GET /api/activity?code=<contentCode>` — проверь события `content.updated`, `content.queue_enqueued`.
2. `GET /api/queues/api/items?externalRef=content/<code>::<version>::api` — убедись, что auto-claim задача создана.
3. Если задача не появилась, проверь `handoff_rules` (миграция `022`) и наличие активного агента API.

## Итог
Успешное прохождение плейбука гарантирует, что:
- контент Vision обновлён строго через REST;
- очередь API автоматически получила задачу;
- все ссылки в `knowledgeRefs` указывают на `/api` ресурсы, что исключает работу с устаревшими YAML.

