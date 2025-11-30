# Find Tasks

Search open tasks for API Designer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"API Designer - Todo" OR Status:"API Designer - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Check readiness:** Architecture exists, NOT content quest

3. **Show list:** number, title, architecture status, priority, Status

**Primary filter: Project Status. Status determines the stage.**
