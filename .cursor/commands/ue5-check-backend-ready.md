# Check Backend Ready

Check backend readiness for UE5.

## Check

- [ ] Status is `UE5 - Todo` or `UE5 - In Progress`
- [ ] Backend implemented, API working
- [ ] For UI tasks: design doc exists in `knowledge/design/ui/`

**Result:**
- OK Ready → can start
- ❌ Not ready → return to Backend, Update Status to `Backend - Returned`

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Backend - Returned' из list_project_fields
  }
});
```
