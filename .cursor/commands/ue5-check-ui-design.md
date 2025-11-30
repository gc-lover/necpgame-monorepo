# Check UI Design

Check UI design exists for UI tasks.

## Check

- [ ] Status is `UE5 - Todo` or `UE5 - In Progress`
- [ ] Labels `ui`, `ux`, `client` or title contains `UI:`, `UX:`
- [ ] Design doc exists in `knowledge/design/ui/`

**Result:**
- OK Has design → can start
- ❌ No design → return to UI/UX Designer, Update Status to `UI/UX - Returned`

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'UI/UX - Returned' из list_project_fields
  }
});
```
