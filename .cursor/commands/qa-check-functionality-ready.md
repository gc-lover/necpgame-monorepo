# Check Functionality Ready

Check if functionality is ready for QA.

## Check

- [ ] Status is `QA - Todo` or `QA - In Progress`
- [ ] Backend ready (from Backend Developer)
- [ ] Client ready (from UE5 Developer, if applicable)
- [ ] NOT content quest (YAML) - if labels `canon`, `lore`, `quest` → return to Content Writer

## Return To

**Content quest (YAML):**
- ❌ Return to Content Writer, Update Status to `Content Writer - Returned`

**Backend bugs:**
- ❌ Return to Backend, Update Status to `Backend - Returned`

**Client bugs:**
- ❌ Return to UE5, Update Status to `UE5 - Returned`

**Result:**
- OK Ready → can start QA
- ❌ Not ready → return to determined agent, Update Status accordingly

**Update Status (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Content Writer - Returned' из list_project_fields  // или 'Backend - Returned' или 'UE5 - Returned'
  }
});
```
