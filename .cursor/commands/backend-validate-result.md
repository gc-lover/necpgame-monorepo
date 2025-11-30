# Validate Result

Check backend readiness and determine next agent.

## Criteria

- [ ] Backend implemented, API works
- [ ] Tests passed, code meets standards
- [ ] Metrics and health checks configured

## Determine Next Agent

**Content quest (labels `canon`, `lore`, `quest`):**
- ✅ Ready → handoff to QA, Update Status to `QA - Todo`
- ❌ Not ready → fix issues, don't handoff

**System task:**
- ✅ Ready → handoff to Network, Update Status to `Network - Todo`
- ❌ Not ready → fix issues, don't handoff

**Result:** ✅ Ready → handoff to determined agent / ❌ Not ready → fix issues
