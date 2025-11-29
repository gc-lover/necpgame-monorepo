# Файлы для удаления

## OK Команды передачи задач (удалены)

Все команды передачи задач удалены - агенты передают задачи вручную через MCP GitHub, как указано в правилах.

### Idea Writer
- `.cursor/commands/idea-writer-transfer-to-architect.md`
- `.cursor/commands/idea-writer-transfer-to-content-writer.md`
- `.cursor/commands/idea-writer-transfer-to-ui-ux-designer.md`

### Architect
- `.cursor/commands/architect-transfer-to-api-designer.md`

### API Designer
- `.cursor/commands/api-designer-transfer-to-backend.md`

### Backend Developer
- `.cursor/commands/backend-transfer-to-ue5.md`
- `.cursor/commands/backend-transfer-to-qa.md`

### Content Writer
- `.cursor/commands/content-writer-transfer-to-backend-for-import.md`

### Database Engineer
- `.cursor/commands/database-transfer-to-backend.md`

### UI/UX Designer
- `.cursor/commands/ui-ux-designer-transfer-to-ue5.md`

### Network Engineer
- `.cursor/commands/network-transfer-to-ue5.md`

### DevOps
- `.cursor/commands/devops-transfer-to-qa.md`

### Security Agent
- `.cursor/commands/security-transfer-to-qa.md`

### Game Balance Agent
- `.cursor/commands/game-balance-transfer-to-qa.md`

### QA
- `.cursor/commands/qa-transfer-to-release.md`

### UE5 Developer
- `.cursor/commands/ue5-transfer-to-qa.md` (найден и удален)

## Оставляем команды

### Общие команды для каждого агента:
- `{agent}-find-tasks.md` - поиск задач
- `{agent}-return-task.md` - возврат задачи
- `{agent}-validate-result.md` - валидация результата

### Специфичные команды проверки:
- `{agent}-check-*.md` - проверка входных данных
- `backend-import-quest-to-db.md` - импорт квестов (специфичная операция)
- `stats-show-stats.md` - показ статистики
- `release-close-issue.md` - закрытие Issue

## Причина удаления

Команды передачи задач избыточны - агенты должны передавать задачи вручную через MCP GitHub, как указано в правилах. Это:
- Дает больше контроля над процессом передачи
- Позволяет добавлять детальные комментарии
- Соответствует текущему workflow
- Уменьшает количество команд (было ~70, станет ~50)
- Каждый агент видит только свои команды (меньше контекста)

## Итого

**OK Удалено:** 16 команд передачи задач (включая найденный `ue5-transfer-to-qa.md`)
**Осталось:** ~50 команд (find-tasks, return-task, validate-result, check-*, специфичные операции)

Все команды передачи задач успешно удалены. Агенты теперь передают задачи вручную через MCP GitHub, как указано в правилах.
