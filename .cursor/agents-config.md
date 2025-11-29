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

### 3. Database Engineer (`agent-database.mdc`)
- **Статус:** `database-dev`
- **Метки:** `agent:database`, `stage:database`, `database`
- **Что делает:** Проектирует схемы БД, создает Liquibase миграции

### 4. API Designer (`agent-api-designer.mdc`)
- **Статус:** `api-designer`
- **Метки:** `agent:api-designer`, `stage:api-design`, `protocol`
- **Что делает:** Создает OpenAPI спецификации

### 5. Backend Developer (`agent-backend.mdc`)
- **Статус:** `backend-dev`
- **Метки:** `agent:backend`, `stage:backend-dev`, `backend`
- **Что делает:** Реализует Go сервисы

### 6. Network Engineer (`agent-network.mdc`)
- **Статус:** `network-dev`
- **Метки:** `agent:network`, `stage:network`, `infrastructure`, `protocol`
- **Что делает:** Настраивает Envoy, gRPC, WebSocket, оптимизирует протокол

### 7. Security Agent (`agent-security.mdc`)
- **Статус:** `security`
- **Метки:** `agent:security`, `stage:security`, `security`
- **Что делает:** Аудит безопасности, валидация, интеграция anti-cheat

### 8. DevOps (`agent-devops.mdc`)
- **Статус:** `devops`
- **Метки:** `agent:devops`, `stage:infrastructure`, `infrastructure`
- **Что делает:** Docker, Kubernetes, деплой, CI/CD, observability

### 9. Performance Engineer (`agent-performance.mdc`)
- **Статус:** `performance`
- **Метки:** `agent:performance`, `stage:performance`
- **Что делает:** Оптимизирует производительность, профилирование

### 10. UE5 Developer (`agent-ue5.mdc`)
- **Статус:** `ue5-dev`
- **Метки:** `agent:ue5`, `stage:client-dev`, `client`
- **Что делает:** Реализует клиент на Unreal Engine 5.7

### 11. QA/Testing (`agent-qa.mdc`)
- **Статус:** `testing`
- **Метки:** `agent:qa`, `stage:testing`
- **Что делает:** Тестирует функциональность, ищет баги

### 12. Content Writer (`agent-content-writer.mdc`)
- **Статус:** `content-writer`
- **Метки:** `agent:content-writer`, `stage:content`, `content`, `canon`, `lore`, `quest`
- **Что делает:** Создает контентные квесты, лор, наратив, диалоги. Работает с готовой архитектурой системы квестов.

### 13. Game Balance Agent (`agent-game-balance.mdc`)
- **Статус:** `game-balance`
- **Метки:** `agent:game-balance`, `stage:balance`, `game-balance`
- **Что делает:** Балансирует игровые механики, экономику, сложность

### 14. Release (`agent-release.mdc`)
- **Статус:** `release`
- **Метки:** `agent:release`, `stage:release`
- **Что делает:** Готовит релиз, создает release notes

### 15. Stats/Dashboard (`agent-stats.mdc`)
- **Статус:** `stats`
- **Метки:** `agent:stats`, `stage:stats`, `dashboard`
- **Что делает:** Собирает статистику по задачам всех агентов, составляет таблицы с метриками производительности

## Workflow переходов

### Системные задачи (требуют архитектуры):
```
[Idea Writer] → [Architect] → [Database Engineer] → [API Designer] → [Backend Dev] → [Network] → [Security] → [DevOps] → [UE5 Dev] → [QA] → [Game Balance] → [Release]
                    ↓                                                                                                              ↓
              [Performance] (может работать параллельно)                                                                     [Performance]
```

### Контентные задачи (НЕ требуют архитектуры):
```
[Idea Writer] → [Content Writer] → [QA] → [Release]
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

**Для Content Writer:**
```
"@content-writer Создай контентный квест из Issue #1123"
```

**Для Stats/Dashboard:**
```
"@stats Покажи статистику по всем агентам"
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
- `database-dev`
- `api-designer`
- `backend-dev`
- `network-dev`
- `security`
- `devops`
- `performance`
- `ue5-dev`
- `content-writer`
- `testing`
- `game-balance`
- `release`
- `stats`

## Создание Views

Создайте отдельные views для каждого агента, фильтруя по:
- `Development Stage` = соответствующий статус
- Метки `agent:*` для дополнительной фильтрации


