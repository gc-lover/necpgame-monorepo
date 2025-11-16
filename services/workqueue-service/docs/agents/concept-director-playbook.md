# Плейбук Concept Director

## Миссия
Concept Director единственный агент, который:
- создаёт и обновляет знания через `POST /api/content/entities`;
- запускает пайплайн задач через `POST /api/ingest/tasks`;
- валидирует, что handoff построит Vision → API цепочку без ручного вмешательства.

## Предварительные условия
- Docker-компоновка `docker-compose.workqueue.yml` поднята: `docker compose -f docker-compose.workqueue.yml up -d`.
- В Postgres есть агент `concept-director` и его preferences (миграция `019-seed-core-data`).
- Сервис доступен по `http://localhost:8090`, security по роли (`X-Agent-Role`).
- Копия рабочей репы примонтирована внутрь контейнера (`/workspace`), чтобы `knowledgeRefs` можно было сверять и архивировать.

## Команды для окружения
```bash
docker compose -f docker-compose.workqueue.yml logs -f workqueue-service
docker exec workqueue-service curl -s http://localhost:8090/actuator/health
```

## Важно
- Идентификация только заголовком `X-Agent-Role: concept-director`. Не используйте `AGENT_ID` и `X-Agent-Key`.
- REST‑пути без `agentId` в URL. Для чтения активной задачи используйте `GET /api/agents/tasks/items/{itemId}`.
- На Windows используйте PowerShell/CMD примеры ниже, чтобы избежать проблем экранирования JSON.

## Шаг 1. Создание знания
1. Собери payload для `POST /api/content/entities`.
2. Минимальный пример:
```bash
docker exec workqueue-service curl -s -X POST http://localhost:8090/api/content/entities \
  -H "Content-Type: application/json" \
  -H "X-Agent-Role: concept-director" \
  -d '{
        "code": "vision-beat-2025-q4",
        "title": "Q4 Vision Beat",
        "summary": "Новая сюжетная арка и её KPI.",
        "typeCode": "lore_entry",
        "statusCode": "in_review",
        "visibilityCode": "internal",
        "riskLevelCode": "medium",
        "ownerRole": "concept-director",
        "version": "2025.11",
        "lastUpdated": "2025-11-15T10:00:00Z",
        "sourceDocument": "/api/content/entities",
        "tags": ["vision","beat"],
        "topics": ["story","kpi"],
        "metadata": {
          "pillars": ["immersion","agency"],
          "impact": "affects-main-story"
        }
      }'
```
3. После успешного ответа зафиксируй `contentId` — он попадёт в `knowledgeRefs`.

## Шаг 2. Ингест задачи
Concept Director единственный, кто вызывает `/api/ingest/tasks`.
```bash
docker exec workqueue-service curl -s -X POST http://localhost:8090/api/ingest/tasks \
  -H "Content-Type: application/json" \
  -H "X-Agent-Role: concept-director" \
  -d '{
        "sourceId": "VISION-2025-Q4",
        "segment": "concept",
        "initialStatus": "queued",
        "priority": 4,
        "title": "Q4 Vision Beat",
        "summary": "Прописать новую арку и handoff в Vision.",
        "knowledgeRefs": [
          "/api/reference/templates/knowledge-entry-template",
          "/api/content/entities/vision-beat-2025-q4"
        ],
        "templates": {
          "primary": ["concept-director-checklist"],
          "references": [
            {"code": "structure-guidelines", "version": "2025.11", "path": "/api/reference/templates/structure-guidelines"}
          ]
        },
        "payload": {
          "feature": "vision-beat-2025-q4",
          "risk": "medium"
        },
        "handoffPlan": {
          "nextSegment": "vision",
          "conditions": [],
          "notes": "Auto-claim включён, Vision получит задачу после submit."
        }
      }'
```

## Шаг 3. Claim → Submit
Концепт-задача должна пройти через стандартную цепочку.

### Claim
PowerShell:
```powershell
$body = '{"segments":["concept"],"priorityFloor":2}'
curl.exe -s -i -X POST http://localhost:8090/api/agents/tasks/claim `
  -H "X-Agent-Role: concept-director" `
  -H "Content-Type: application/json" `
  --data-binary $body
```
CMD:
```cmd
cd /d C:\NECPGAME
echo {"segments":["concept"],"priorityFloor":2}>claim.json
curl.exe -s -i -X POST http://localhost:8090/api/agents/tasks/claim -H "X-Agent-Role: concept-director" -H "Content-Type: application/json" --data-binary @claim.json
del claim.json
```
В ответе проверь `instructions.brief` и `instructions.templates` — они уже подгружены из БД (без YAML).

Если ответ 409 и в нём есть `activeItemId`, получи инструкции по активной задаче:
```powershell
curl.exe -s -i http://localhost:8090/api/agents/tasks/items/<activeItemId> `
  -H "X-Agent-Role: concept-director"
```

### Submit
1. Подготовь metadata со ссылками на REST:
```json
{
  "knowledgeRefs": [
    "/api/reference/templates/knowledge-entry-template",
    "/api/content/entities/vision-beat-2025-q4"
  ],
  "handoff": {
    "nextSegment": "vision",
    "expectedTemplates": ["structure-guidelines","concept-canon"]
  }
}
```
2. Отправь multipart:
PowerShell:
```powershell
@'
{
  "notes":"Vision пакет собран",
  "artifacts":[{"title":"MR","url":"https://git/MR/42"}],
  "metadata":"{\"knowledgeRefs\":[\"/api/content/entities/vision-beat-2025-q4\"],\"handoff\":{\"nextSegment\":\"vision\"}}"
}
'@ | Out-File -Encoding utf8 payload.json
$submitUrl = "http://localhost:8090/api/agents/tasks/<itemId>/submit"
curl.exe -s -i -X POST $submitUrl `
  -H "X-Agent-Role: concept-director" `
  -H "Content-Type: multipart/form-data" `
  -F "payload=@payload.json;type=application/json"
Remove-Item payload.json -Force
```

## Автоматическая передача
- После submit `ContentTaskCoordinator` создаёт элемент очереди сегмента `vision` c `knowledgeRefs` = REST ссылкам.
- Проверяй `ActivityLog`: `GET /api/activity?entity=content&code=vision-beat-2025-q4` должен содержать `content.queue_enqueued`.
- Vision Manager использует только `claim → submit`; ingest для него запрещён.

## Контроль чеклистов
- Перед каждым сабмитом сравни payload с `reference_templates`:
  - `GET /api/reference/templates/knowledge-entry-template`
  - `GET /api/reference/templates/structure-guidelines`
- Если шаблон обновился, повтори `claim` и приложи новое знание, иначе валидатор выдаст `validation.missing_artifact`.

## Диагностика
- `active_task_exists` → у Concept Director уже есть задача (release lock или завершай её).
- `ingest.forbidden.segment` → ingest вызван не из сегмента `concept` или с другим агентом.
- `handoff.rule_missing` → нет handoff из concept → vision; проверь запись в `handoff_rules`.

## Результат
После выполнения плейбука:
1. В `content_entities` хранится новая запись (`code=vision-beat-2025-q4`).
2. В очереди `vision` появился auto-claim элемент.
3. ActivityLog содержит `content.created`, `content.queue_enqueued`, `task.completed`.

