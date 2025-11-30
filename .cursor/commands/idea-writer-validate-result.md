# Validate Result

Check idea readiness and determine next agent.

## Criteria

- [ ] Idea described, lore developed
- [ ] Quest structured (if applicable)
- [ ] Game mechanics described

## Determine Next Agent

**System task (default):**
- OK Ready → handoff to Architect, Update Status to `Architect - Todo`
- ❌ Not ready → fix issues, don't handoff

**UI/UX task (labels `ui`, `ux`, `client`):**
- OK Ready → handoff to UI/UX Designer, Update Status to `UI/UX - Todo`
- ❌ Not ready → fix issues, don't handoff

**Content quest (labels `canon`, `lore`, `quest`):**
- OK Ready → handoff to Content Writer, Update Status to `Content Writer - Todo`
- ❌ Not ready → fix issues, don't handoff

**Result:** OK Ready → handoff to determined agent / ❌ Not ready → fix issues
