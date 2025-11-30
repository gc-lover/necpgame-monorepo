# Check CI Status

Проверка статуса CI/CD через GitHub Issues с отчётами.

## Использование

Спросить агента: "Проверь статус CI" или "Есть ли упавшие CI jobs?" или "Покажи последние CI отчёты"

## Реализация

Агент должен использовать MCP GitHub инструменты для поиска Issues по заголовку `[CI]`:

```javascript
// Получить открытые CI отчёты
const reports = await mcp_github_search_issues({
  query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]"',
  perPage: 10,
  sort: 'updated',
  order: 'desc'
});

// Получить отчёты для конкретного коммита (по SHA в заголовке)
const commitReports = await mcp_github_search_issues({
  query: `repo:gc-lover/necpgame-monorepo is:issue is:open title:"${shortSha}"`,
  perPage: 5
});

// Найти failed jobs (в заголовке или теле Issue)
const failedReports = await mcp_github_search_issues({
  query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]" title:"FAILURE"',
  perPage: 10
});
```

## Структура CI отчёта

Каждый CI отчёт содержит:

- **Статус workflow:** SUCCESS, FAILURE, CANCELLED, IN_PROGRESS
- **Информацию о коммите:** SHA, ветка
- **Сводку по jobs:** количество успешных, упавших, отменённых
- **Детали failed jobs:** название, статус, шаги, ссылки на логи
- **Ссылки:** на workflow run и каждый job

## Автоматизация

CI отчёты автоматически создаются workflow `ci-monitor.yml`:

- После завершения `Backend CI` workflow
- Обновляются при изменении статуса
- Старые отчёты (старше 5 коммитов) автоматически закрываются

## Интеграция с Project

Все CI отчёты автоматически добавляются в GitHub Project со статусом:

- "DevOps - Todo" (если существует)
- "Backend - Todo" (fallback)

Агенты могут просматривать эти Issues в Project для мониторинга CI/CD статусов.
