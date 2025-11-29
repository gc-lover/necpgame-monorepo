# Idea Writer: Найти мои задачи

Найти все открытые задачи для Idea Writer через MCP GitHub с оптимизацией запросов.

## Инструкции

1. **Используй поиск с кэшированием (ОБЯЗАТЕЛЬНО):**

   ```javascript
   // Инициализируй кэш в памяти сессии
   const issueCache = new Map();
   const searchCache = new Map();
   const SEARCH_TTL = 2 * 60 * 1000; // 2 минуты
   
   // ОБЯЗАТЕЛЬНО: Используй поиск вместо множественных issue_read
   const query = 'is:issue is:open label:agent:idea-writer';
   const result = await mcp_github_search_issues({
     query: query,
     perPage: 100
   });
   
   // Кэшируй результаты
   searchCache.set(cacheKey, { data: result, timestamp: Date.now() });
   result.items.forEach(issue => {
     issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
   });
   ```

2. **Проверь Project статус (опционально):**
   - Используй `mcp_github_list_project_items` с фильтром по `Development Stage = idea-writer`
   - Кэшируй результаты (TTL: 2-3 минуты)

3. **Отфильтруй и покажи список задач:**
   - Номер Issue
   - Название
   - Приоритет (если есть)
   - Статус (открыто/в работе/возвращено)
   - Development Stage
   - Есть ли метка `returned` (требует внимания)

4. **Спроси пользователя, с какой задачей работать**

## Оптимизация

- **КРИТИЧЕСКИ ВАЖНО:** Используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
- Используй кэширование для повторных запросов в рамках одной сессии
- Добавляй задержки между запросами (500ms)

## Ссылки

- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - полная документация поиска задач
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов
- `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` - шаблоны кода для кэширования

