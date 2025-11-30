# Find Tasks

Search open tasks for Idea Writer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"Idea Writer - Todo" OR Status:"Idea Writer - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Show list:** number, title, priority, Status

3. **Ask user which task to work on**

**Alternative:** Search issues and filter by Project Status. Always use Project Status as primary filter, not labels.
