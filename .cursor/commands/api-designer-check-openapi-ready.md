# API Designer: Проверить готовность OpenAPI спецификации

Проверить, готова ли OpenAPI спецификация для передачи Backend Developer.

## Инструкции

1. **Прочитай Issue через MCP GitHub:**
   ```javascript
   const issue = await getCachedIssue(issueNumber);
   ```

2. **Проверь критерии готовности:**
   - [ ] OpenAPI спецификация создана
   - [ ] Все endpoints описаны
   - [ ] Схемы данных определены
   - [ ] Примеры запросов добавлены
   - [ ] Спецификация валидирована (`swagger-cli validate`)
   - [ ] Все требования к качеству выполнены (см. правила API Designer)

3. **Проверь наличие файлов:**
   - Файлы в `proto/openapi/`
   - Используются общие компоненты из `common.yaml`
   - Файлы не превышают 500 строк (если больше - разбиты)

4. **Проверь валидацию:**
   ```bash
   swagger-cli validate proto/openapi/{service-name}.yaml
   # или
   npx -y @redocly/cli lint proto/openapi/{service-name}.yaml
   ```

5. **Покажи результат:**
   - OK Спецификация готова → можно передавать Backend Developer
   - ❌ Спецификация не готова → нужно доработать

## Ссылки

- `.cursor/rules/agent-api-designer.mdc` - правила API Designer (раздел "Требования к качеству")
- `.cursor/rules/AGENT_TASK_DISCOVERY.md` - критерии готовности

