# Check OpenAPI

Check if OpenAPI spec exists before starting.

## Check

1. Verify Status is `Backend - Todo` or `Backend - In Progress`
2. Check file: `proto/openapi/{service-name}.yaml`
3. Validate: `npx -y @redocly/cli lint proto/openapi/{service-name}.yaml`

**Result:**
- OK Found and valid → can start
- ❌ Not found → return to API Designer, Update Status to `API Designer - Returned`

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
