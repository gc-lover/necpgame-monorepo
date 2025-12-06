# Python Scripts for Liquibase Migrations

## Overview

Liquibase supports executing Python scripts in addition to SQL scripts. This is useful for:
- Complex data transformations
- Data validation
- Multi-step migrations
- Integration with external systems

## Usage

### In XML Changelog

```xml
<changeSet id="example_python_script" author="database-engineer">
    <executeCommand executable="python">
        <arg value="${project.basedir}/infrastructure/liquibase/migrations/scripts/your_script.py"/>
        <arg value="--database-url"/>
        <arg value="${database.url}"/>
        <arg value="--username"/>
        <arg value="${database.username}"/>
        <arg value="--password"/>
        <arg value="${database.password}"/>
    </executeCommand>
</changeSet>
```

### In YAML Changelog

```yaml
databaseChangeLog:
  - changeSet:
      id: example_python_script
      author: database-engineer
      changes:
        - executeCommand:
            executable: python
            args:
              - ${project.basedir}/infrastructure/liquibase/migrations/scripts/your_script.py
              - --database-url
              - ${database.url}
              - --username
              - ${database.username}
              - --password
              - ${database.password}
```

## Script Requirements

1. **Exit codes:**
   - `0` - Success
   - Non-zero - Error (migration will fail)

2. **Command-line arguments:**
   - Scripts receive database connection parameters via arguments
   - Use `argparse` for parsing

3. **Dependencies:**
   - Install required Python packages (e.g., `psycopg2` for PostgreSQL)
   - Document in script header

4. **Error handling:**
   - Always use try/except blocks
   - Print errors to stderr
   - Return appropriate exit codes

## Example Script

See `example_migration.py` for a complete example.

## Best Practices

1. **Idempotency:** Scripts should be safe to run multiple times
2. **Logging:** Print progress and results to stdout
3. **Validation:** Verify prerequisites before making changes
4. **Rollback:** Consider rollback logic if needed
5. **Testing:** Test scripts in development before production

## Database Connection

Scripts receive connection parameters via command-line arguments:
- `--database-url`: JDBC URL (e.g., `jdbc:postgresql://localhost:5432/necpgame`)
- `--username`: Database username
- `--password`: Database password

Parse the JDBC URL to extract host, port, and database name for your database driver.

## Adding to Changelog

Add the migration file to `infrastructure/liquibase/changelog.yaml`:

```yaml
- include:
    file: migrations/V2_2__python_script_example.xml
```

## Issue

Related: #142718723

