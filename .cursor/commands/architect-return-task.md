# Architect: Вернуть задачу

Вернуть задачу Idea Writer с объяснением причины возврата.

## Инструкции

1. **Прочитай Issue через MCP GitHub (используй кэширование)**

2. **Определи причину возврата:**
   - Нет идеи от Idea Writer
   - Идея недостаточно проработана
   - Это UI задача (передай UI/UX Designer)
   - Это контентный квест (передай Content Writer)

3. **Обнови метки Issue:**
   - Удали: `agent:architect`, `stage:design`
   - Добавь: `returned`
   - Добавь: `agent:idea-writer`, `stage:idea` (если нет идеи)
   - Или: `agent:ui-ux-designer`, `stage:ui-design` (если UI задача)
   - Или: `agent:content-writer`, `stage:content` (если контентный квест)

4. **Добавь комментарий с объяснением:**
   ```markdown
   WARNING Задача возвращена: {причина}
   
   **Причина возврата:**
   - {детальное описание}
   
   **Требуется:**
   - {что нужно для продолжения работы}
   ```

5. **Используй батчинг для >=3 Issues**

## Ссылки

- `.cursor/rules/AGENT_TASK_RETURN.md` - полная документация возврата задач
- `.cursor/rules/AGENT_LABEL_MANAGEMENT.md` - управление метками

