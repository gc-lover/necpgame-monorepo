# Find Tasks

Search open tasks for Game Balance Agent via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"Game Balance - Todo" OR Status:"Game Balance - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Show list:** number, title, priority, Status

**Primary filter: Project Status. Status determines the stage.**
