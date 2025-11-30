# Check Idea

Check idea readiness and determine task type for handoff.

## Criteria

- [ ] Idea described, lore developed
- [ ] Quest structured (if applicable)
- [ ] Game mechanics described

## Task Type (determine by labels or content)

- System task → Architect, Update Status to `Architect - Todo`
- UI/UX (labels `ui`, `ux`, `client`) → UI/UX Designer, Update Status to `UI/UX - Todo`
- Content quest (labels `canon`, `lore`, `quest`) → Content Writer, Update Status to `Content Writer - Todo`

**Result:**
- OK Ready → handoff to determined agent, update Status
- ❌ Not ready → fix issues, don't handoff

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Architect - Todo' из list_project_fields  // или 'UI/UX - Todo' или 'Content Writer - Todo'
  }
});
```
