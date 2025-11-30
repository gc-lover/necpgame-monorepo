# Check UI Concept

Check if UI concept exists before starting.

## Check

- [ ] Status is `UI/UX - Todo` or `UI/UX - In Progress`
- [ ] UI description in Issue or files in `knowledge/`
- [ ] Visual design, UX mechanics, user scenarios described

**Result:**
- OK Found → can create design
- ❌ Not found → return to Idea Writer, Update Status to `Idea Writer - Returned`

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Idea Writer - Returned' из list_project_fields
  }
});
```
