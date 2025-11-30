# Find Tasks

Search all tasks for Stats Agent (statistics).

## Steps

1. **Search in Project:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue',
     fields: ['Status', 'Title']
   });
   ```

2. **Process:** Group by agent, count stats

**For statistics only - shows all tasks grouped by agent.**
