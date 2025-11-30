# Show Stats

Show statistics for all agents.

## Steps

1. Search Project items with Status field:
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

2. Group by Status, count: total, open, done, in progress, returned

3. Show table with progress percentage

**Group by Status values, not labels.**
