# Validate Result

Check UE5 implementation readiness before handoff to QA.

## Criteria

- [ ] UE5 code implemented
- [ ] Backend integration working
- [ ] Tests passed

**Result:**
- OK Ready → handoff to QA
- ❌ Not ready → fix issues

**On handoff:** Update Status to `QA - Todo`

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'QA - Todo' из list_project_fields
  }
});
```
