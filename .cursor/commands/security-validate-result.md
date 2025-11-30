# Validate Result

Check security audit readiness before handoff to DevOps.

## Criteria

- [ ] Audit completed, vulnerabilities fixed
- [ ] Input validation checked, rate limiting configured

**Result:**
- OK Ready → handoff to DevOps, Update Status to `DevOps - Todo`
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
    value: '{option_id}'  // id опции 'DevOps - Todo' из list_project_fields
  }
});
```
