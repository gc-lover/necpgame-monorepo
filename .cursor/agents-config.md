# Cursor Background Agents Configuration

## Настройка агентов в Cursor

Cursor Background Agents позволяют автоматизировать работу с задачами через GitHub Projects. Каждый агент имеет свои правила в `.cursor/rules/agent-*.mdc`.

## Доступные агенты

### 1. Idea Writer (`agent-idea-writer.mdc`)
- **Статус:** `idea-writer`
- **Метки:** `agent:idea-writer`, `stage:idea`, `game-design`
- **Что делает:** Создает идеи, лор, квесты, текстовые описания

### 2. Architect (`agent-architect.mdc`)
- **Статус:** `architect`
- **Метки:** `agent:architect`, `stage:design`
- **Что делает:** Структурирует идеи, проектирует архитектуру

### 3. API Designer (`agent-api-designer.mdc`)
- **Статус:** `api-designer`
- **Метки:** `agent:api-designer`, `stage:api-design`, `protocol`
- **Что делает:** Создает OpenAPI спецификации

### 4. Backend Developer (`agent-backend.mdc`)
- **Статус:** `backend-dev`
- **Метки:** `agent:backend`, `stage:backend-dev`, `backend`
- **Что делает:** Реализует Go сервисы

### 5. Network Engineer (`agent-network.mdc`)
- **Статус:** `network-dev`
- **Метки:** `agent:network`, `stage:network`, `infrastructure`, `protocol`
- **Что делает:** Настраивает Envoy, gRPC, WebSocket, оптимизирует протокол

### 6. DevOps (`agent-devops.mdc`)
- **Статус:** `devops`
- **Метки:** `agent:devops`, `stage:infrastructure`, `infrastructure`
- **Что делает:** Docker, Kubernetes, деплой, CI/CD, observability

### 7. Performance Engineer (`agent-performance.mdc`)
- **Статус:** `performance`
- **Метки:** `agent:performance`, `stage:performance`
- **Что делает:** Оптимизирует производительность, профилирование

### 8. UE5 Developer (`agent-ue5.mdc`)
- **Статус:** `ue5-dev`
- **Метки:** `agent:ue5`, `stage:client-dev`, `client`
- **Что делает:** Реализует клиент на Unreal Engine 5.7

### 9. QA/Testing (`agent-qa.mdc`)
- **Статус:** `testing`
- **Метки:** `agent:qa`, `stage:testing`
- **Что делает:** Тестирует функциональность, ищет баги

### 10. Release (`agent-release.mdc`)
- **Статус:** `release`
- **Метки:** `agent:release`, `stage:release`
- **Что делает:** Готовит релиз, создает release notes

## Workflow переходов

```
[Idea Writer] → [Architect] → [API Designer] → [Backend Dev] → [Network] → [UE5 Dev] → [QA] → [Release]
                    ↓                                                      ↓
              [DevOps] ←───────────────────────────────────────────────
                    ↓
              [Performance] (может работать параллельно)
```

## Использование в Cursor

### Активация правил агента

Правила агента активируются автоматически когда:
- Issue имеет соответствующую метку `agent:*`
- Issue имеет статус в Project = соответствующему статусу
- Вы явно указываете агента в промпте

### Примеры команд

**Для Idea Writer:**
```
"@agent-idea-writer Создай идею для системы крафта в стиле Cyberpunk"
```

**Для Architect:**
```
"@agent-architect Структурируй идею из Issue #5"
```

**Для Network Engineer:**
```
"@agent-network Оптимизируй Protocol Buffers для realtime синхронизации"
```

**Для DevOps:**
```
"@agent-devops Создай Docker образ для character-service"
```

**Для Performance Engineer:**
```
"@agent-performance Профилируй inventory-service и найди узкие места"
```

## Автоматическая маршрутизация

GitHub Actions автоматически:
1. Определяет нужного агента по меткам
2. Обновляет статус в Project
3. Добавляет соответствующие метки
4. Переводит задачу к следующему агенту при готовности

## Настройка Project

В GitHub Project создайте Custom Field "Development Stage" со значениями:
- `idea-writer`
- `architect`
- `api-designer`
- `backend-dev`
- `network-dev`
- `devops`
- `performance`
- `ue5-dev`
- `testing`
- `release`

## Создание Views

Создайте отдельные views для каждого агента, фильтруя по:
- `Development Stage` = соответствующий статус
- Метки `agent:*` для дополнительной фильтрации


