# Backend Agent: Validate Optimizations Command

## Command
```
/backend-validate-optimizations #123
```

## Description
**CRITICAL COMMAND** - Validates that Backend service meets all performance and optimization requirements before task handoff.

## Usage
Execute this command BEFORE handing off any Backend task to ensure compliance with performance standards.

## Validation Checks

### Level 1: Basic (All Services)
- [ ] Context timeouts in all external calls
- [ ] DB connection pool configured (25-50 connections)
- [ ] Health/metrics endpoints implemented
- [ ] Structured JSON logging (no fmt.Println)
- [ ] Error handling for all operations

### Level 2: Hot Path (>100 RPS)
- [ ] Memory pooling for response objects
- [ ] Batch DB operations where possible
- [ ] Lock-free atomic operations
- [ ] String vs []byte optimization
- [ ] Zero allocations in critical path

### Level 3: Game Servers (Real-time)
- [ ] UDP support for game state
- [ ] Spatial partitioning (>100 players)
- [ ] Delta compression
- [ ] Adaptive tick rate
- [ ] GC tuning (GOGC=50)

### Level 4: MMO Features
- [ ] Redis session store
- [ ] Multi-level caching
- [ ] Optimistic locking
- [ ] Materialized views for leaderboards

## Implementation
```bash
# Automated validation script
python scripts/check-performance-optimizations.py services/{service}-go/
```

## Response Format
```
[SEARCH] Validating optimizations for {service}-go...

[OK] Struct alignment: OK
[OK] Goroutine leak tests: OK
[OK] Context timeouts: OK
[OK] DB pool config: OK
[ERROR] Memory pooling: NOT FOUND (BLOCKER!)
[WARNING] Benchmarks: Missing

━━━━━━━━━━━━━━━━━━━━━━━━
RESULT: [ERROR] VALIDATION FAILED
BLOCKERS: 1
WARNINGS: 1
━━━━━━━━━━━━━━━━━━━━━━━━

Cannot proceed to next stage.
Fix blockers and run validation again.
```

## BLOCKER Rules
- **Context timeouts missing** → Cannot proceed
- **DB pool not configured** → Cannot proceed
- **Goroutine leaks** → Cannot proceed

## Next Steps
- If BLOCKERS found: Fix issues and re-run validation
- If all OK: Proceed with task handoff
- Add validation results to handoff comment