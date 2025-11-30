# Check OpenAPI Ready

Check OpenAPI spec quality before handoff to Backend.

## Quality Checklist

- [ ] Spec validated (`swagger-cli validate`)
- [ ] All endpoints present, schemas defined
- [ ] Security configured, pagination from `common.yaml`

**Result:**
- OK Ready → handoff to Backend, Update Status to `Backend - Todo`
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
    value: '{option_id}'  // id опции 'Backend - Todo' из list_project_fields
  }
});
```
