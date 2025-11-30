# Find Tasks

Search open tasks for Idea Writer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"Idea Writer - Todo" OR Status:"Idea Writer - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. **Show list:** number, title, priority, Status

   **Важно:** В списке показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

3. **Ask user which task to work on**

4. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `Idea Writer - In Progress`:

   **Примечание:** `item_id` используется только для API вызова. В комментариях и сообщениях всегда указывай номер Issue (например, `Issue: #123`).
   
   **ВАЖНО: Используй константы из `.cursor/GITHUB_PROJECT_CONFIG.md`!**
   ```javascript
   mcp_github_update_project_item({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     item_id: project_item_id,  // из результата list_project_items
     updated_field: {
       id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
       value: 'd9960d37'  // STATUS_OPTIONS['Idea Writer - In Progress'] из GITHUB_PROJECT_CONFIG.md
     }
   });
   ```
   
   **Если статуса нет в константах:** получи через `mcp_github_list_project_fields` (см. `.cursor/AGENT_COMMON_RULES.md`)

**Alternative:** Search issues and filter by Project Status. Always use Project Status as primary filter, not labels.
