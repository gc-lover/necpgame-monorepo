# Find Tasks

Search open tasks for UE5 Developer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"UE5 - Todo" OR Status:"UE5 - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. **Check readiness:** Backend ready, UI design ready (if UI task)

3. **Show list:** number, title, backend/design status, priority, Status

   **Важно:** В списке показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

4. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `UE5 - In Progress`:

   **Примечание:** `item_id` используется только для API вызова. В комментариях и сообщениях всегда указывай номер Issue (например, `Issue: #123`).
   ```javascript
   mcp_github_update_project_item({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     item_id: project_item_id,
     updated_field: {
       id: 239690516  // число,
       value: '{option_id}'  // id опции 'UE5 - In Progress' из list_project_fields
     }
   });
   ```

**Primary filter: Project Status. Status determines the stage.**
