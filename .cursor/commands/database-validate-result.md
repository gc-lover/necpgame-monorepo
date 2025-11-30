# Validate Result

Check DB schema readiness before handoff to API Designer.

## Criteria

- [ ] DB schema designed
- [ ] Liquibase migrations created

**Result:**
- OK Ready → handoff to API Designer, Update Status to `API Designer - Todo`
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
    value: '{option_id}'  // id опции 'API Designer - Todo' из list_project_fields
  }
});
```
