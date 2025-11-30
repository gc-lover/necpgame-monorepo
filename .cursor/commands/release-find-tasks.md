# Find Tasks

Search open tasks for Release Manager via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"Release - Todo" OR Status:"Release - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Show list:** number, title, priority, Status

**Primary filter: Project Status. Status determines the stage.**
