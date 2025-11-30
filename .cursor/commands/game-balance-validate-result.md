# Validate Result

Check game balance readiness before handoff to Release.

## Criteria

- [ ] Balance completed, config files updated
- [ ] Formulas configured, economy balanced

**Result:**
- OK Ready → handoff to Release, Update Status to `Release - Todo`
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
    value: '{option_id}'  // id опции 'Release - Todo' из list_project_fields
  }
});
```
