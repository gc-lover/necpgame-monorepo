# API Designer: Вернуть задачу

Вернуть задачу Architect с объяснением причины возврата.

## Инструкции

1. **Прочитай Issue через MCP GitHub (используй кэширование)**

2. **Определи причину возврата:**
   - Нет архитектуры от Architect
   - Архитектура недостаточно проработана
   - Это контентный квест (передай Content Writer)

3. **Обнови метки Issue:**
   - Удали: `agent:api-designer`, `stage:api-design`
   - Добавь: `returned`
   - Добавь: `agent:architect`, `stage:design` (если нет архитектуры)
   - Или: `agent:content-writer`, `stage:content` (если контентный квест)

4. **Добавь комментарий с объяснением**

5. **Используй батчинг для >=3 Issues**

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - полная документация возврата задач
- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками

