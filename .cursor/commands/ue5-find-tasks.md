# Find Tasks

Search open tasks for UE5 Developer via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"UE5 - Todo" OR Status:"UE5 - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Check readiness:** Backend ready, UI design ready (if UI task)

3. **Show list:** number, title, backend/design status, priority, Status

**Primary filter: Project Status. Status determines the stage.**
