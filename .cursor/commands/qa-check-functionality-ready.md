# Check Functionality Ready

Check if functionality is ready for QA.

## Check

— [ ] Agent = `QA`, Status = `Todo` или `In Progress`
— [ ] Backend ready (from Backend Developer)
- [ ] Client ready (from UE5 Developer, if applicable)
- [ ] Content imported to DB (if content quest) - check via API `GET /api/v1/gameplay/quests/{quest_id}` or SQL query
- [ ] Enterprise-grade service optimizations validated (run `python scripts/validate-backend-optimizations.sh`)

## Return To

**Content quest (NOT imported to DB):**

- [ERROR] Return to Backend: Status `Returned`, Agent `Backend`
- Комментарий: "[WARNING] Quest not imported to DB. Backend must import first. Issue: #{number}"

**Backend bugs:**

- [ERROR] Return to Backend: Status `Returned`, Agent `Backend`

**Client bugs:**

- [ERROR] Return to UE5: Status `Returned`, Agent `UE5`

**Result:**

- [OK] Ready → can start QA
- [ERROR] Not ready → return to determined agent, update Status/Agent accordingly

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
