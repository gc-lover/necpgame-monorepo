# Show Stats

Show statistics for all agents.

## Steps

1. Search Project items with Status field:
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: ''
   });
   ```
   **Note:** Для сбора статистики по всем задачам используй пустой query или фильтр по конкретным статусам. Не используй `is:issue` - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. Group by Status, count: total, open, done, in progress, returned

3. Show table with progress percentage

**Group by Status values, not labels.**
