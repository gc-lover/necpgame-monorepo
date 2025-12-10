# Check UI Concept

Check if UI concept exists before starting.

## Check

- [ ] Agent = `UI/UX`, Status = `Todo` или `In Progress`
- [ ] UI description in Issue or files in `knowledge/`
- [ ] Visual design, UX mechanics, user scenarios described

**Result:**
- OK Found → can create design
- ❌ Not found → return to Idea: set Status `Returned`, Agent `Idea`

**Update fields (if returning to Idea):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },   // Status: Returned
    { id: 243899542, value: '8c3f5f11' },   // Agent: Idea
  ]
});
```
