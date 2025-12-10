# Check UI Design

Check UI design exists for UI tasks.

## Check

- [ ] Agent = `UE5`, Status = `Todo` или `In Progress`
- [ ] Labels `ui`, `ux`, `client` or title contains `UI:`, `UX:`
- [ ] Design doc exists in `knowledge/design/ui/`

**Result:**
- OK Has design → can start
- ❌ No design → return to UI/UX: set Status `Returned`, Agent `UI/UX`

**Update fields (if returning to UI/UX):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },   // Status: Returned
    { id: 243899542, value: '98c65039' },   // Agent: UI/UX
  ]
});
```
