# Check Quest Architecture

Check quest architecture exists before starting.

## Check

- [ ] Agent = `Content`, Status = `Todo` или `In Progress`
- [ ] Quest architecture exists in `knowledge/implementation/architecture/quests/`

**Result:**
- OK Has architecture → can start
- ❌ No architecture → return to Architect: Status `Returned`, Agent `Architect`

**Update fields (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },   // Status: Returned
    { id: 243899542, value: 'd109c7f9' },   // Agent: Architect
  ]
});
```
