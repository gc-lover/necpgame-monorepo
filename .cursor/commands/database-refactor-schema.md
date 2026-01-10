# Database Agent: Refactor Schema Command

## Command
```
/database-refactor-schema {table_name}
```

## Description
Analyzes existing database table and provides refactoring recommendations for performance optimization.

## Usage
Execute this command when working with existing tables that may need optimization.

## Analysis Areas

### Column Order Optimization
- Current: field sizes and order
- Recommended: large → small field ordering
- Memory savings calculation

### Index Optimization
- Missing indexes for hot queries
- Redundant indexes to remove
- Covering indexes recommendations

### Query Performance
- Slow queries identification
- Execution plan analysis
- Optimization suggestions

## Implementation
```bash
# Analyze table structure
python scripts/analyze-table-structure.py {table_name}

# Check query performance
EXPLAIN ANALYZE SELECT * FROM {table_name} WHERE {common_condition};

# Generate optimization plan
python scripts/generate-optimization-plan.py {table_name}
```

## Response Format
```
[DATABASE REFACTOR] Analyzing table: players

📊 Current Structure:
- id (BIGINT) - 8 bytes
- health (INTEGER) - 4 bytes
- level (INTEGER) - 4 bytes
- name (VARCHAR) - variable

🔧 Recommended Optimizations:

1. Column Order (30% memory savings)
   Current: 24 bytes/row → Optimized: 16 bytes/row
   Command: ALTER TABLE players REORDER COLUMNS (id, level, health, name);

2. Missing Indexes
   - CREATE INDEX idx_players_level ON players(level);
   - CREATE INDEX idx_players_health_level ON players(health, level);

3. Query Optimizations
   - Hot query: SELECT * FROM players WHERE level > 10
   - Add covering index for better performance

💡 Expected Gains:
- Memory: 30% reduction
- Query speed: 5x faster for level queries
- Index size: Optimized for hot paths

[RECOMMENDATION] Apply optimizations via migration script
```

## Next Steps
- Create migration script with recommendations
- Test performance improvements
- Apply changes in staging first