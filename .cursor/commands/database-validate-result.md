# Database Agent: Validate Result Command

## Command
```
/database-validate-result #123
```

## Description
Validates that database work is complete and meets requirements.

## Usage
Execute this command after completing database tasks to ensure they are ready for handoff.

## Validation Checks

### Schema Tasks
- [ ] Tables created with correct structure
- [ ] Indexes added for hot queries
- [ ] Foreign keys and constraints defined
- [ ] Column order optimized (large → small)
- [ ] Performance hints documented

### Migration Tasks
- [ ] Liquibase syntax valid
- [ ] Forward/backward migrations work
- [ ] Rollback procedures documented
- [ ] Data integrity preserved

### Performance Tasks
- [ ] Query execution plans optimized
- [ ] Indexes covering hot paths
- [ ] Memory usage within limits
- [ ] Concurrent access handled

## Implementation
```bash
# Validate schema
python scripts/migrations/validate-all-migrations.py

# Check column order
python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/{file}.sql --dry-run

# Performance validation
EXPLAIN ANALYZE SELECT * FROM {table} WHERE {hot_condition};
```

## Response Format
```
[DATABASE VALIDATION] Checking implementation...

✅ Schema structure valid
✅ Indexes optimized
✅ Migrations syntactically correct
✅ Performance requirements met
⚠️  Column order could be optimized

[RESULT] Database validation PASSED
Ready for handoff to next agent
```

## Next Steps
- Fix any issues found
- Re-run validation if needed
- Proceed with handoff when all checks pass