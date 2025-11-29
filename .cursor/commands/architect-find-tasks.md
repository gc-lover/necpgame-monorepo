# Architect: Найти мои задачи

Найти все открытые задачи для Architect через MCP GitHub с оптимизацией запросов.

## Инструкции

1. **Используй поиск с кэшированием (ОБЯЗАТЕЛЬНО):**

   ```javascript
   // ОБЯЗАТЕЛЬНО: Используй поиск вместо множественных issue_read
   const query = 'is:issue is:open label:agent:architect';
   const result = await mcp_github_search_issues({
     query: query,
     perPage: 100
   });
   ```

2. **Проверь Project статус (опционально):**
   - Используй `mcp_github_list_project_items` с фильтром по `Development Stage = architect`
   - Кэшируй результаты (TTL: 2-3 минуты)

3. **Проверь готовность входных данных:**
   - Должна быть идея от Idea Writer (Issue с меткой `stage:idea`)
   - **Проверь, что это НЕ UI задача** (метки `ui`, `ux`)
   - **Проверь, что это НЕ контентный квест** (метки `canon`, `lore`, `quest`)

4. **Отфильтруй и покажи список задач:**
   - Номер Issue
   - Название
   - Приоритет (если есть)
   - Есть ли идея от Idea Writer
   - Статус

5. **Спроси пользователя, с какой задачей работать**

## Оптимизация

- **КРИТИЧЕСКИ ВАЖНО:** Используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
- Используй кэширование для повторных запросов в рамках одной сессии
- Добавляй задержки между запросами (500ms)

## Ссылки

- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - полная документация поиска задач
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов

