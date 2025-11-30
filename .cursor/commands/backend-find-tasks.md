# Find Tasks

Search open tasks for Backend Developer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"Backend - Todo" OR Status:"Backend - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. **Check readiness:** OpenAPI spec exists, architecture exists, NOT content quest

3. **Show list:** number, title, OpenAPI/architecture status, priority, Status

   **Важно:** В списке показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

4. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `Backend - In Progress`:

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
       value: '{option_id}'  // id опции '7bc9d20f' из list_project_fields  // id опции "Backend - In Progress"
     }
   });
   ```

**Primary filter: Project Status, not labels. Status determines the stage.**
