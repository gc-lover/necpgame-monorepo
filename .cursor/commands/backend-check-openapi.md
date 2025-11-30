# Check OpenAPI

Check if OpenAPI spec exists before starting.

## Check

1. Verify Status is `Backend - Todo` or `Backend - In Progress`
2. Check file: `proto/openapi/{service-name}.yaml`
3. Validate: `npx -y @redocly/cli lint proto/openapi/{service-name}.yaml`

**Result:**
- OK Found and valid → can start
- ❌ Not found → return to API Designer, Update Status to `API Designer - Returned`
