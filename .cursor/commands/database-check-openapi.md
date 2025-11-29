# Database Engineer: Проверить наличие OpenAPI спецификации

Проверить, есть ли OpenAPI спецификация для проектирования схемы БД.

## Инструкции

1. **Прочитай Issue через MCP GitHub:**
   ```javascript
   const issue = await getCachedIssue(issueNumber);
   ```

2. **Проверь наличие OpenAPI спецификации:**
   - Файлы в `proto/openapi/`
   - Схемы данных определены

3. **Проверь, что схемы данных содержат:**
   - Все необходимые поля для БД
   - Правильные типы данных для PostgreSQL
   - Constraints и валидацию

4. **Покажи результат:**
   - OK Спецификация найдена → можно проектировать БД
   - ❌ Спецификация не найдена → верни задачу API Designer

## Если спецификация не найдена

- Используй `/return-task` для возврата задачи API Designer
- Добавь комментарий с объяснением

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - процесс возврата задач
- `.cursor/rules/agent-database.mdc` - правила Database Engineer

