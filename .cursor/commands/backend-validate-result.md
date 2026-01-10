# Backend Agent: Validate Result Command

## Command
```
/backend-validate-result #123
```

## Description
Validates that the Backend implementation is complete and ready for handoff to the next agent.

## Usage
Execute this command after completing Backend work to ensure everything is properly implemented.

## Validation Checks

### Functional Validation
- [ ] Service compiles without errors
- [ ] Unit tests pass
- [ ] API endpoints respond correctly
- [ ] Database operations work
- [ ] Error handling implemented

### Code Quality
- [ ] Follows Go coding standards
- [ ] Proper error handling
- [ ] Context usage for timeouts
- [ ] Structured logging
- [ ] No TODO comments left

### Performance Requirements
- [ ] Optimizations applied (see `/backend-validate-optimizations`)
- [ ] Benchmarks pass
- [ ] Memory usage within limits
- [ ] No performance regressions

### Documentation
- [ ] Code is documented
- [ ] API endpoints documented
- [ ] Migration scripts created if needed

## Implementation
```bash
# Run comprehensive validation
cd services/{service}-go
go build .
go test ./... -v
go vet ./...
golint ./...
```

## Response Format
```
[VALIDATION] Checking backend implementation...

✅ Code compiles successfully
✅ Unit tests pass (15/15)
✅ API endpoints functional
✅ Database operations verified
✅ Error handling complete
✅ Performance requirements met
⚠️  Minor: Some TODO comments remain

[RESULT] Backend validation PASSED
Ready for handoff to next agent
```

## Next Steps
- If validation fails: Fix issues and re-run
- If validation passes: Proceed with handoff
- Update GitHub Project status and agent