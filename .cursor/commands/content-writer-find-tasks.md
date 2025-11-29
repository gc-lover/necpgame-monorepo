# Content Writer: Найти мои задачи

Найти все открытые задачи для Content Writer через MCP GitHub с оптимизацией запросов.

## Инструкции

1. **Используй поиск с кэшированием (ОБЯЗАТЕЛЬНО):**

   ```javascript
   const query = 'is:issue is:open label:agent:content-writer';
   const result = await mcp_github_search_issues({
     query: query,
     perPage: 100
   });
   ```

2. **Отфильтруй и покажи список задач:**
   - Номер Issue
   - Название
   - Приоритет (если есть)
   - Статус

3. **Спроси пользователя, с какой задачей работать**

## Оптимизация

- **КРИТИЧЕСКИ ВАЖНО:** Используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
- Используй кэширование для повторных запросов
- Добавляй задержки между запросами (500ms)

## Ссылки

- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - полная документация поиска задач
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов

