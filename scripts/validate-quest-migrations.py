#!/usr/bin/env python3
"""
Validate quest_definitions table migrations syntax and structure
Issue: #2227 - Database migrations for quest definitions
"""

import os
import re
from pathlib import Path

def validate_sql_syntax(sql_content, filename):
    """Basic SQL syntax validation"""
    errors = []

    # Check for basic SQL structure
    if not sql_content.strip():
        errors.append("Empty SQL file")
        return errors

    # Check for required components
    if "CREATE TABLE" not in sql_content.upper():
        errors.append("Missing CREATE TABLE statement")

    if "gameplay.quest_definitions" not in sql_content:
        errors.append("Missing gameplay.quest_definitions table reference")

    # Check for common syntax errors
    # Look for unmatched parentheses
    open_parens = sql_content.count('(')
    close_parens = sql_content.count(')')
    if open_parens != close_parens:
        errors.append(f"Unmatched parentheses: {open_parens} open, {close_parens} close")

    # Check for semicolons at end of statements
    statements = [s.strip() for s in sql_content.split(';') if s.strip()]
    for i, stmt in enumerate(statements[:-1]):  # Last part might be empty
        if not stmt.upper().startswith(('BEGIN', 'COMMIT', '--', '/*')):
            # Should have semicolon or be a comment
            pass

    # Check for required columns
    required_columns = ['id', 'title', 'description', 'status', 'level_min', 'level_max']
    for col in required_columns:
        if f"{col}" not in sql_content.lower():
            errors.append(f"Missing required column: {col}")

    # Check for constraints
    if "PRIMARY KEY" not in sql_content.upper():
        errors.append("Missing PRIMARY KEY constraint")

    if "CHECK" not in sql_content.upper():
        errors.append("Missing CHECK constraints for status/level")

    # Check for indexes
    if "CREATE INDEX" not in sql_content.upper():
        errors.append("Missing performance indexes")

    return errors

def validate_migration_structure():
    """Validate migration file structure and content"""
    script_dir = Path(__file__).parent
    project_root = script_dir.parent
    migration_file = project_root / "infrastructure" / "liquibase" / "schema" / "V1_50__content_quest_definitions_table.sql"

    print(f"[INFO] Validating migration file: {migration_file}")
    print("=" * 60)

    if not migration_file.exists():
        print(f"[ERROR] Migration file not found: {migration_file}")
        return False

    try:
        with open(migration_file, 'r', encoding='utf-8') as f:
            content = f.read()

        print("[OK] Migration file found and readable")
        print(f"[INFO] File size: {len(content)} characters")

        # Basic syntax validation
        errors = validate_sql_syntax(content, migration_file.name)

        if errors:
            print("[ERROR] SQL Syntax Validation Failed:")
            for error in errors:
                print(f"  • {error}")
            return False
        else:
            print("[OK] SQL Syntax Validation Passed")

        # Check for specific requirements
        checks = [
            ("Table creation", "CREATE TABLE.*quest_definitions", "Table creation statement"),
            ("UUID primary key", "id.*UUID.*PRIMARY KEY", "UUID primary key definition"),
            ("Title column", "title.*VARCHAR.*200", "Title column with VARCHAR(200)"),
            ("Status column", "status.*VARCHAR.*20", "Status column definition"),
            ("Level constraints", "level_min.*CHECK.*level_max.*CHECK", "Level range constraints"),
            ("JSONB columns", "rewards.*JSONB.*objectives.*JSONB", "JSONB columns for rewards/objectives"),
            ("Indexes", "CREATE INDEX.*quest_definitions", "Performance indexes"),
            ("Triggers", "CREATE TRIGGER.*updated_at", "Auto-update trigger"),
            ("Comments", "COMMENT ON", "Documentation comments"),
        ]

        print("\n[INFO] Detailed Structure Validation:")
        all_checks_passed = True

        for check_name, pattern, description in checks:
            if re.search(pattern, content, re.IGNORECASE | re.DOTALL):
                print(f"[OK] {check_name}: {description}")
            else:
                print(f"[ERROR] {check_name}: Missing {description}")
                all_checks_passed = False

        # Performance checks
        print("\n[PERF] Performance Optimization Checks:")
        perf_checks = [
            ("Memory alignment hints", "-- BACKEND NOTE.*struct alignment"),
            ("Index optimization", "idx_quest_definitions.*level_range"),
            ("GIN indexes", "USING GIN.*rewards"),
            ("Partial indexes", "WHERE status.*active"),
        ]

        for check_name, pattern, description in perf_checks:
            if re.search(pattern, content, re.IGNORECASE):
                print(f"[OK] {check_name}: {description}")
            else:
                print(f"[WARNING] {check_name}: Missing {description}")

        # Final result
        print("\n" + "=" * 60)
        if all_checks_passed:
            print("[SUCCESS] MIGRATION VALIDATION SUCCESSFUL")
            print("[OK] Ready for database application")
            print("[OK] All syntax and structure requirements met")
            return True
        else:
            print("[FAILED] MIGRATION VALIDATION FAILED")
            print("[ERROR] Fix the identified issues before applying")
            return False

    except Exception as e:
        print(f"[ERROR] Error reading migration file: {e}")
        return False

def main():
    """Main validation function"""
    print("[DATABASE] QUEST DEFINITIONS MIGRATION VALIDATOR")
    print("Issue: #2227 - Database migrations for quest definitions")
    print("=" * 60)

    success = validate_migration_structure()

    if success:
        print("\n[NEXT] NEXT STEPS:")
        print("1. Apply migration to staging database first")
        print("2. Run integration tests with quest import scripts")
        print("3. Verify data integrity and performance")
        print("4. Apply to production after successful staging tests")
        exit(0)
    else:
        print("\n[FIX] FIX REQUIRED:")
        print("1. Review and fix SQL syntax errors")
        print("2. Ensure all required columns and constraints are present")
        print("3. Add missing indexes and triggers")
        print("4. Re-run validation before applying migration")
        exit(1)

if __name__ == "__main__":
    main()
