# Check Architecture

Check if architecture exists before starting.

## Check

- [ ] Files in `knowledge/implementation/architecture/`
- [ ] Components, microservices, API endpoints described
- [ ] Status is `Backend - Todo` or `Backend - In Progress`

**Result:**
- OK Found → can start
- ❌ Not found → return to Architect, Update Status to `Architect - Returned`

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Architect - Returned' из list_project_fields
  }
});
```
