# Validate Result

Check network readiness before handoff to Security.

## Criteria

- [ ] Envoy configured, protocol optimized
- [ ] Realtime sync working

**Result:**
- OK Ready → handoff to Security, Update Status to `Security - Todo`
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
    value: '{option_id}'  // id опции 'Security - Todo' из list_project_fields
  }
});
```
