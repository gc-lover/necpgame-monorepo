# Task Discovery

## Search Method

**CRITICAL: Search by Project Status, not labels!**

### Algorithm

1. **Search in Project by Status:**
```javascript
// Use config from .cursor/GITHUB_PROJECT_CONFIG.md
await mcp_github_list_project_items({
  owner_type: 'user',  // from config
  owner: 'gc-lover',   // from config
  project_number: 1,   // from config
  query: 'is:issue Status:"{Agent} - Todo" OR Status:"{Agent} - In Progress"',
  fields: ['Status', 'Title']
});
```

2. **Or search issues and filter by Status:**
```javascript
const result = await mcp_github_search_issues({
  query: 'is:issue is:open',
  perPage: 100
});
// Then filter by Project Status via mcp_github_list_project_items
```

3. **Check readiness:**
- Status matches `{Agent} - Todo` or `{Agent} - In Progress`
- Has input data (OpenAPI, architecture, etc.)
- Not already processed

### Status Values

- Idea Writer: `Idea Writer - Todo`, `Idea Writer - In Progress`
- Architect: `Architect - Todo`, `Architect - In Progress`
- API Designer: `API Designer - Todo`, `API Designer - In Progress`
- Backend: `Backend - Todo`, `Backend - In Progress`
- Network: `Network - Todo`, `Network - In Progress`
- DevOps: `DevOps - Todo`, `DevOps - In Progress`
- Performance: `Performance - Todo`, `Performance - In Progress`
- UE5: `UE5 - Todo`, `UE5 - In Progress`
- Content Writer: `Content Writer - Todo`, `Content Writer - In Progress`
- QA: `QA - Todo`, `QA - In Progress`
- Release: `Release - Todo`, `Release - In Progress`
- Stats: `Stats - Todo`, `Stats - In Progress`

### Project Status Check

**Primary filter is Status in Project:**
- Use `mcp_github_list_project_items` with Status filter
- Status determines stage, not labels
- Labels are secondary/optional

### User Request

**User can specify task:**
```
@agent Work on Issue #123
```

**Then:**
1. Check cache first
2. Read Issue #123 via MCP
3. Check Project Status matches your agent
4. Verify readiness
5. Start work

## Caching

**Optional (for session):**
- Cache Project items (TTL: 3 minutes)
- Cache search results (TTL: 2 minutes)
- Cache Issue reads (TTL: 5 minutes)

## Batch Operations

- **<3 Issues:** Sequential with delays (300-500ms)
- **3-9 Issues:** Batch (5 per batch, delays between)
- **>=10 Issues:** Use GitHub Actions Batch Processor
