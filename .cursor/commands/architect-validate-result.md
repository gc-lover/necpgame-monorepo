# Validate Result

Check architecture readiness before handoff to API Designer.

## Criteria

- [ ] Architecture designed, components defined
- [ ] Microservices identified, API endpoints described
- [ ] Requirements ready, data sync designed

**Result:**
- OK Ready → handoff to API Designer
- ❌ Not ready → fix issues, don't handoff

**On handoff:** Update Status to `API Designer - Todo`

**Update Status:**
```javascript
// Получить id опции через mcp_github_list_project_fields
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,  // число
    value: '{option_id}'  // id опции '3eddfee3' из list_project_fields  // id опции "API Designer - Todo"
  }
});
```
