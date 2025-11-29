# API Designer: Найти мои задачи

Найти все открытые задачи для API Designer через MCP GitHub с оптимизацией запросов.

## Инструкции

1. **Используй поиск с кэшированием (ОБЯЗАТЕЛЬНО):**

   ```javascript
   const query = 'is:issue is:open label:agent:api-designer';
   const result = await mcp_github_search_issues({
     query: query,
     perPage: 100
   });
   ```

2. **Проверь готовность входных данных:**
   - Должна быть архитектура от Architect (Issue с меткой `stage:design`)
   - **Проверь, что это НЕ контентный квест** (метки `canon`, `lore`, `quest`)

3. **Отфильтруй и покажи список задач:**
   - Номер Issue
   - Название
   - Есть ли архитектура от Architect
   - Приоритет

4. **Спроси пользователя, с какой задачей работать**

## Оптимизация

- **КРИТИЧЕСКИ ВАЖНО:** Используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
- Используй кэширование для повторных запросов
- Добавляй задержки между запросами (500ms)

## Ссылки

- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - полная документация поиска задач
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов

