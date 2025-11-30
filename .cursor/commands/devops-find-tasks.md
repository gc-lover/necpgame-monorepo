# Find Tasks

Search open tasks for DevOps via MCP GitHub Project, including CI/CD monitoring.

## Steps

1. **Search in Project by Status:**
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Status:"DevOps - Todo" OR Status:"DevOps - In Progress"'
   });
   ```
   **Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. **Search CI Reports:**
   ```javascript
   // Get recent CI reports (by title starting with [CI])
   await mcp_github_search_issues({
     query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]"',
     perPage: 10,
     sort: 'updated',
     order: 'desc'
   });
   
   // Get only failed CI reports
   await mcp_github_search_issues({
     query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]" title:"FAILURE"',
     perPage: 10
   });
   ```

3. **Show list:** number, title, priority, Status, type (task or CI report)

   **Важно:** В списке показывай номер Issue (например, `#123`), а не `item_id`. Номер Issue берется из `content.number`.

4. **При выборе задачи:** ОБЯЗАТЕЛЬНО обнови статус на `DevOps - In Progress`:

   **Примечание:** `item_id` используется только для API вызова. В комментариях и сообщениях всегда указывай номер Issue (например, `Issue: #123`).
   ```javascript
   mcp_github_update_project_item({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     item_id: project_item_id,
     updated_field: {
       id: 239690516  // число,
       value: '{option_id}'  // id опции 'DevOps - In Progress' из list_project_fields
     }
   });
   ```

**Primary filter: Project Status. Status determines the stage.**

**CI Reports:** Automatically created by `ci-monitor.yml` workflow, show CI/CD job statuses and failures.
