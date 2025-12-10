# Check Backend Ready

Check backend readiness for UE5.

## Check

- [ ] Agent = `UE5`, Status = `Todo` или `In Progress`
- [ ] Backend implemented, API working
- [ ] For UI tasks: design doc exists in `knowledge/design/ui/`

**Result:**
- OK Ready → can start
- ❌ Not ready → return to Backend: set Status `Returned`, Agent `Backend`

**Update fields (if returning to Backend):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },   // Status: Returned
    { id: 243899542, value: '1fc13998' },   // Agent: Backend
  ]
});
```
