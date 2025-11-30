# Validate Result

Check OpenAPI spec readiness before handoff to Backend.

## Criteria

- [ ] OpenAPI spec created, validated
- [ ] All endpoints described, schemas defined
- [ ] Quality checklist passed

**Result:**
- OK Ready → handoff to Backend
- ❌ Not ready → fix issues, don't handoff

**On handoff:** Update Status to `Backend - Todo`

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Backend - Todo' из list_project_fields
  }
});
```
