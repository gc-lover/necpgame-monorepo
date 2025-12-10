# Check Functionality Ready

Check if functionality is ready for QA.

## Check

- [ ] Agent = `QA`, Status = `Todo` или `In Progress`
- [ ] Backend ready (from Backend Developer)
- [ ] Client ready (from UE5 Developer, if applicable)
- [ ] NOT content quest (YAML) - if labels `canon`, `lore`, `quest` → return to Content Writer

## Return To

**Content quest (YAML):**
- ❌ Return to Content: Status `Returned`, Agent `Content`

**Backend bugs:**
- ❌ Return to Backend: Status `Returned`, Agent `Backend`

**Client bugs:**
- ❌ Return to UE5: Status `Returned`, Agent `UE5`

**Result:**
- OK Ready → can start QA
- ❌ Not ready → return to determined agent, update Status/Agent accordingly

**Update fields (if returning):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' },        // Status: Returned
    { id: 243899542, value: '{agent_id}' },      // Agent: Content/Backend/UE5
  ]
});
```
