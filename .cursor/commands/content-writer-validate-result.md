# Validate Result

Check quest YAML readiness before handoff to Backend.

## Criteria

- [ ] YAML created, syntax valid
- [ ] Structure matches architecture
- [ ] File <=500 lines

**Result:**
- OK Ready → handoff to Backend
- ❌ Not ready → fix issues

**On handoff:** Update Status to `Backend - Todo`
