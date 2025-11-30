# Find Tasks

Search open tasks for Architect via MCP GitHub Project.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"Architect - Todo" OR Status:"Architect - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Check readiness:** Idea from Idea Writer exists, NOT UI task, NOT content quest

3. **Show list:** number, title, priority, Status

**Primary filter: Project Status. Status determines the stage.**
