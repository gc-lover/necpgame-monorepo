# UE5 Developer: Вернуть задачу

Вернуть задачу разработчику с объяснением причины возврата.

## Инструкции

1. **Прочитай Issue через MCP GitHub (используй кэширование)**

2. **Определи причину возврата:**
   - Нет готового бекенда → верни Backend Developer
   - Нет дизайн-документа (для UI задач) → верни UI/UX Designer

3. **Обнови метки Issue:**
   - Удали: `agent:ue5`, `stage:client-dev`
   - Добавь: `returned`
   - Добавь: `agent:{correct-agent}`, `stage:{correct-stage}`

4. **Добавь комментарий с объяснением**

5. **Используй батчинг для >=3 Issues**

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - полная документация возврата задач
- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками

