# Check Idea

Check idea readiness and determine task type for handoff.

## Criteria

- [ ] Idea described, lore developed
- [ ] Quest structured (if applicable)
- [ ] Game mechanics described

## Task Type (determine by labels or content)

- System task → Architect: Status `Todo`, Agent `Architect`
- UI/UX (labels `ui`, `ux`, `client`) → UI/UX Designer: Status `Todo`, Agent `UI/UX`
- Content quest (labels `canon`, `lore`, `quest`) → Content Writer: Status `Todo`, Agent `Content`

**Result:**
- OK Ready → handoff to determined agent (set Status `Todo`, Agent as выше)
- ❌ Not ready → fix issues, don't handoff

**Update fields:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '{agent_id}' }, // Architect/UI/UX/Content
  ]
});
```
