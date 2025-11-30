# Check OpenAPI

Check if OpenAPI spec exists before starting.

## Check

- [ ] Status is `Database - Todo` or `Database - In Progress`
- [ ] OpenAPI spec exists from API Designer

**Result:**
- OK Has spec → can start
- ❌ No spec → return to API Designer, Update Status to `API Designer - Returned`

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'API Designer - Returned' из list_project_fields
  }
});
```
