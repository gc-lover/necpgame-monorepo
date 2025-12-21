# Check Architecture

Check architecture readiness before handoff to API Designer.

## Criteria

— [ ] Architecture designed, components defined
— [ ] Microservices identified, API endpoints described
— [ ] Requirements ready
— [ ] Enterprise-grade domain selected (see .cursor/DOMAIN_REFERENCE.md)

## Enterprise-Grade Domain Validation

**Before handoff, validate domain selection:**

```bash
# Check if selected domain is enterprise-grade
python scripts/validate-domains-openapi.py --list-domains | grep "your-domain-name"

# Validate domain structure
python scripts/validate-domains-openapi.py proto/openapi/your-domain-name/main.yaml
```

**Result:**

- [OK] Ready → handoff to API: Status `Todo`, Agent `API`
- [ERROR] Not ready → fix issues, don't handoff

**Update fields:**

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '6aa5d9af' }, // Agent: API
  ]
});
```
