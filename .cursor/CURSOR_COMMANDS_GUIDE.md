# Руководство по командам Cursor для агентов

## Что такое команды Cursor?

**Cursor Commands** - это предопределенные промпты, хранящиеся в Markdown файлах:
- **Project Commands**: `.cursor/commands/*.md` - команды для конкретного проекта
- **User Commands**: `~/.cursor/commands/*.md` или `~/.claude/commands/*.md` - персональные команды

## Как работают команды

1. **Вызов через `/` в чате:**
   - Введите `/` в поле ввода чата Cursor
   - Cursor автоматически покажет доступные команды
   - Выберите команду или введите её название

2. **Структура команды:**
   ```markdown
   # Название команды
   
   Описание команды
   
   ## Инструкции
   
   [Детальные инструкции для выполнения]
   ```

3. **Агенты могут выполнять команды:**
   - Агенты могут вызывать команды через чат
   - Команды выполняются в контексте агента
   - Результаты передаются агенту для обработки

## WARNING ВАЖНО: Передача задач

**Агенты передают задачи вручную через MCP GitHub**, как указано в правилах агентов. Команды передачи задач не создаются - это позволяет:
- Добавлять детальные комментарии при передаче
- Полный контроль над процессом передачи
- Соответствие текущему workflow

## Полный список команд

### Команды для каждого агента

Каждый агент имеет персональные команды (только свои, без лишнего контекста):

#### Поиск задач
- `/idea-writer-find-tasks` - Idea Writer
- `/architect-find-tasks` - Architect
- `/api-designer-find-tasks` - API Designer
- `/backend-find-tasks` - Backend Developer
- `/content-writer-find-tasks` - Content Writer
- `/ue5-find-tasks` - UE5 Developer
- `/qa-find-tasks` - QA
- `/database-find-tasks` - Database Engineer
- `/ui-ux-designer-find-tasks` - UI/UX Designer
- `/network-find-tasks` - Network Engineer
- `/devops-find-tasks` - DevOps
- `/security-find-tasks` - Security Agent
- `/performance-find-tasks` - Performance Engineer
- `/game-balance-find-tasks` - Game Balance Agent
- `/release-find-tasks` - Release
- `/stats-find-tasks` - Stats Agent

#### Возврат задач
- `/idea-writer-return-task` - Idea Writer
- `/architect-return-task` - Architect
- `/api-designer-return-task` - API Designer
- `/backend-return-task` - Backend Developer
- `/content-writer-return-task` - Content Writer
- `/ue5-return-task` - UE5 Developer
- `/qa-return-to-developer` - QA
- `/database-return-task` - Database Engineer
- `/ui-ux-designer-return-task` - UI/UX Designer
- `/performance-return-to-developer` - Performance Engineer

#### Валидация результата
- `/idea-writer-validate-result` - Idea Writer
- `/architect-validate-result` - Architect
- `/api-designer-validate-result` - API Designer
- `/backend-validate-result` - Backend Developer
- `/content-writer-validate-result` - Content Writer
- `/ue5-validate-result` - UE5 Developer
- `/qa-validate-result` - QA
- `/database-validate-result` - Database Engineer
- `/ui-ux-designer-validate-result` - UI/UX Designer
- `/network-validate-result` - Network Engineer
- `/devops-validate-result` - DevOps
- `/security-validate-result` - Security Agent
- `/game-balance-validate-result` - Game Balance Agent
- `/release-validate-result` - Release

#### Проверка входных данных (специфичные для агентов)
- `/idea-writer-check-idea` - Idea Writer
- `/architect-check-architecture` - Architect
- `/api-designer-check-openapi-ready` - API Designer
- `/backend-check-openapi` - Backend Developer
- `/backend-check-architecture` - Backend Developer
- `/content-writer-check-quest-architecture` - Content Writer
- `/content-writer-validate-quest-yaml` - Content Writer
- `/ue5-check-backend-ready` - UE5 Developer
- `/ue5-check-ui-design` - UE5 Developer
- `/qa-check-functionality-ready` - QA
- `/database-check-openapi` - Database Engineer
- `/ui-ux-designer-check-idea` - UI/UX Designer

#### Специфичные операции
- `/backend-import-quest-to-db` - Backend Developer (импорт контентных квестов в БД)
- `/stats-show-stats` - Stats Agent (показать статистику)
- `/release-close-issue` - Release (закрыть Issue после релиза)

## Использование команд

### Пример: Поиск задач

```
@backend /backend-find-tasks
```

Команда автоматически:
- Выполнит поиск через MCP GitHub с оптимизацией
- Покажет список задач с приоритетами и статусами
- Спросит, с какой задачей работать

### Пример: Проверка входных данных

```
@backend /backend-check-openapi #123
@backend /backend-check-architecture #123
```

Команды проверят наличие необходимых входных данных перед началом работы.

### Пример: Валидация результата

```
@api-designer /api-designer-validate-result #123
```

Команда проверит готовность результата перед передачей следующему агенту.

### Пример: Возврат задачи

```
@backend /backend-return-task #123
```

Команда вернет задачу с объяснением причины возврата.

## Передача задач

**Агенты передают задачи вручную через MCP GitHub**, как указано в правилах:

1. Удали свою метку агента
2. Добавь метку следующего агента
3. Обнови статус Project
4. Добавь комментарий с объяснением

**Подробнее:** см. `.cursor/rules/AGENT_LABEL_MANAGEMENT.md`

## Преимущества персональных команд

1. **Меньше контекста:** Каждый агент видит только свои команды
2. **Специфичность:** Команды адаптированы под конкретного агента
3. **Простота:** Меньше команд = проще использовать
4. **Гибкость:** Передача задач вручную дает больше контроля

## Ссылки

- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - полная документация поиска задач
- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками
- `.cursor/rules/AGENT_TASK_RETURN.md` - процесс возврата задач
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов
- `.cursor/agents-config.md` - конфигурация всех агентов
