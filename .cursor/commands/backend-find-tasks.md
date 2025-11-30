# Find Tasks

Search open tasks for Backend Developer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"Backend - Todo" OR Status:"Backend - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Check readiness:** OpenAPI spec exists, architecture exists, NOT content quest

3. **Show list:** number, title, OpenAPI/architecture status, priority, Status

**Primary filter: Project Status, not labels. Status determines the stage.**
