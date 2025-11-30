# Find Tasks

Search open tasks for DevOps via MCP GitHub Project, including CI/CD monitoring.

## Steps

1. **Search in Project by Status:**
   ```javascript
   // Project config: .cursor/GITHUB_PROJECT_CONFIG.md
   await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'is:issue Status:"DevOps - Todo" OR Status:"DevOps - In Progress"',
     fields: ['Status', 'Title']
   });
   ```

2. **Search CI Reports:**
   ```javascript
   // Get recent CI reports (by title starting with [CI])
   await mcp_github_search_issues({
     query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]"',
     perPage: 10,
     sort: 'updated',
     order: 'desc'
   });
   
   // Get only failed CI reports
   await mcp_github_search_issues({
     query: 'repo:gc-lover/necpgame-monorepo is:issue is:open title:"[CI]" title:"FAILURE"',
     perPage: 10
   });
   ```

3. **Show list:** number, title, priority, Status, type (task or CI report)

**Primary filter: Project Status. Status determines the stage.**

**CI Reports:** Automatically created by `ci-monitor.yml` workflow, show CI/CD job statuses and failures.
