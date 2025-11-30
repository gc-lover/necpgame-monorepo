# Check OpenAPI Ready

Check OpenAPI spec quality before handoff to Backend.

## Quality Checklist

- [ ] Spec validated (`swagger-cli validate`)
- [ ] All endpoints present, schemas defined
- [ ] Security configured, pagination from `common.yaml`

**Result:**
- OK Ready → handoff to Backend, Update Status to `Backend - Todo`
- ❌ Not ready → fix issues, don't handoff
