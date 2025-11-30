# Check Quest Architecture

Check quest architecture exists before starting.

## Check

- [ ] Status is `Content Writer - Todo` or `Content Writer - In Progress`
- [ ] Quest architecture exists in `knowledge/implementation/architecture/quests/`

**Result:**
- OK Has architecture → can start
- ❌ No architecture → return to Architect, Update Status to `Architect - Returned`

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
