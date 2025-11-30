# Find Tasks

Search all tasks for Stats Agent (statistics).

## Steps

1. **Search in Project:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"Stats - Todo" OR Status:"Stats - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.
   
   **Для сбора статистики:** Используй `/stats-show-stats` - он собирает статистику по всем задачам всех агентов.

2. **Process:** Group by agent, count stats

   **Важно:** При отображении задач показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

3. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `Stats - In Progress`:

   **Примечание:** `item_id` используется только для API вызова. В комментариях и сообщениях всегда указывай номер Issue (например, `Issue: #123`).
   ```javascript
   // Получить id опции через mcp_github_list_project_fields
   mcp_github_update_project_item({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     item_id: project_item_id,
     updated_field: {
       id: 239690516,  // число
       value: '{option_id}'  // id опции '{option_id}' из list_project_fields  // id опции "Stats - In Progress"
     }
   });
   ```

**For statistics only - shows all tasks grouped by agent.**
