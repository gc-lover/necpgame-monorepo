# Check OpenAPI

Check if OpenAPI spec exists before starting.

## Check

- [ ] Agent = `DB`, Status = `Todo` или `In Progress`
- [ ] OpenAPI spec exists from API Designer

**Result:**
- OK Has spec → can start
- ❌ No spec → return to API: Status `Returned`, Agent `API`

**Update fields (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },   // Status: Returned
    { id: 243899542, value: '6aa5d9af' },   // Agent: API
  ]
});
```
