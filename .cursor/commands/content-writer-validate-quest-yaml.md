# Validate Quest YAML

Validate quest YAML file before handoff.

## Validation

1. YAML syntax: `yamllint quest-*.yaml`
2. Structure: all required fields present
3. Data correctness: IDs unique, types valid
4. Size: file <=500 lines

**Result:**
- OK Valid → handoff to Backend, Update Status to `Backend - Todo`
- ❌ Invalid → fix errors, don't handoff
