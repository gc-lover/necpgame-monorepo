# Backend Developer: Вернуть задачу

Вернуть задачу другому агенту с объяснением причины возврата.

## Инструкции

1. **Прочитай Issue через MCP GitHub (используй кэширование)**

2. **Определи причину возврата:**
   - Нет OpenAPI спецификации → верни API Designer
   - Нет архитектуры → верни Architect
   - Это контентный квест → передай Content Writer

3. **Обнови метки Issue:**
   - Удали: `agent:backend`, `stage:backend-dev`
   - Добавь: `returned`
   - Добавь: `agent:{correct-agent}`, `stage:{correct-stage}`

4. **Добавь комментарий с объяснением**

5. **Используй батчинг для >=3 Issues**

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - полная документация возврата задач
- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками

