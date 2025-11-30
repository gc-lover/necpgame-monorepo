# Find Tasks

Search open tasks for UI/UX Designer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"UI/UX - Todo" OR Status:"UI/UX - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. **Show list:** number, title, priority, Status

   **Важно:** В списке показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

3. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `UI/UX - In Progress`:

   **Примечание:** `item_id` используется только для API вызова. В комментариях и сообщениях всегда указывай номер Issue (например, `Issue: #123`).
   ```javascript
   mcp_github_update_project_item({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     item_id: project_item_id,
     updated_field: {
       id: 239690516  // число,
       value: '{option_id}'  // id опции 'UI/UX - In Progress' из list_project_fields
     }
   });
   ```

**Primary filter: Project Status. Status determines the stage.**
