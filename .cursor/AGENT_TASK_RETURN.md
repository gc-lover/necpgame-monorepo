# Task Return Rules

## Return Process

**If task not ready for you:**

1. **Update Status:**
   - Set Status to `{CorrectAgent} - Returned` (if known)
   - Or keep current status if unclear

2. **Add comment:**
   - Explain reason
   - List what's missing
   - Indicate correct agent

## Return Reasons

### Missing Input Data
- No OpenAPI spec (for Backend)
- No architecture (for API Designer)
- No functionality (for QA)
- No backend (for UE5)

### Wrong Task Type
- Content quest → pass to Content Writer
- System task → pass to Architect

### Already Processed
- Files already created
- Issue already closed
- Wrong stage

## Comment Template

```markdown
WARNING **Task returned: {reason}**

**Missing:**
- {what_is_missing}

**Correct agent:** {Agent Name}

**Status updated:** `{CorrectAgent} - Returned`
```

## Agent-Specific Returns

### QA Agent
- No functionality → Backend or UE5
- Content quest (YAML) → Content Writer
- No OpenAPI → API Designer

### Backend Developer
- No OpenAPI → API Designer
- No architecture → Architect
- Content quest → Content Writer

### API Designer
- No architecture → Architect
- Content quest → Content Writer

### Architect
- No idea → Idea Writer
- Content quest → Content Writer

### UE5 Developer
- No backend → Backend
- No OpenAPI → API Designer
- Content quest → Content Writer

## Checklist

- [ ] Verified input data readiness
- [ ] Verified task type
- [ ] Updated Status to `{CorrectAgent} - Returned`
- [ ] Added comment with reason
