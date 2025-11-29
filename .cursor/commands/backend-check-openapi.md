# Backend Developer: Проверить наличие OpenAPI спецификации

Проверить, есть ли OpenAPI спецификация для задачи перед началом работы.

## Инструкции

1. **Прочитай Issue через MCP GitHub:**
   ```javascript
   const issue = await getCachedIssue(issueNumber);
   ```

2. **Определи имя сервиса:**
   - Из названия Issue
   - Из меток Issue
   - Из описания Issue

3. **Проверь наличие файла:**
   - Путь: `proto/openapi/{service-name}.yaml`
   - Или: `proto/openapi/{service-name}-*.yaml` (для множественных спецификаций)

4. **Проверь валидность спецификации:**
   ```bash
   npx -y @redocly/cli lint proto/openapi/{service-name}.yaml
   ```

5. **Покажи результат:**
   - OK Спецификация найдена и валидна → можно начинать работу
   - ❌ Спецификация не найдена → верни задачу API Designer

## Если спецификация не найдена

- Используй `/return-task` для возврата задачи API Designer
- Добавь комментарий с объяснением

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - процесс возврата задач
- `.cursor/rules/agent-backend.mdc` - правила Backend Developer

