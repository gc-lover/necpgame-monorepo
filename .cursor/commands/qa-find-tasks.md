# Find Tasks

Search open tasks for QA via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"QA - Todo" OR Status:"QA - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Check readiness:** Backend ready, Client ready (if applicable), NOT content quest (YAML)

3. **Show list:** number, title, functionality status, priority, Status

**Primary filter: Project Status. Status determines the stage.**
