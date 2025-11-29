# Release: Закрыть Issue

Закрыть Issue после успешного релиза.

## Инструкции

1. **Проверь, что релиз завершен:**
   - Release notes созданы
   - GitHub Release создан
   - Деплой выполнен
   - Мониторинг настроен

2. **Прочитай Issue через MCP GitHub:**
   ```javascript
   const issue = await getCachedIssue(issueNumber);
   ```

3. **Закрой Issue:**
   ```javascript
   await mcp_github_issue_write({
     method: 'update',
     owner: 'gc-lover',
     repo: 'necpgame-monorepo',
     issue_number: issueNumber,
     state: 'closed',
     state_reason: 'completed'
   });
   ```

4. **Удали метки агента:**
   - Удали: `agent:release`, `stage:release`

5. **Добавь комментарий:**
   ```markdown
   OK Релиз выполнен
   
   **Детали релиза:**
   - Release notes: [ссылка]
   - GitHub Release: [ссылка]
   - Деплой: [статус]
   - Мониторинг: настроен
   ```

## Ссылки

- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками
- `.cursor/rules/agent-release.mdc` - правила Release

