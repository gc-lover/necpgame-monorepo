#!/usr/bin/env python3
"""
Simple script to apply SQL migrations to PostgreSQL database
"""

import psycopg2
import os
from pathlib import Path

def apply_sql_migration(conn, sql_file):
    """Apply a single SQL migration"""
    try:
        with open(sql_file, 'r', encoding='utf-8') as f:
            sql = f.read()

        print(f"Applying migration: {sql_file}")

        with conn.cursor() as cursor:
            cursor.execute(sql)
            conn.commit()

        print(f"[OK] Successfully applied: {sql_file}")
        return True

    except Exception as e:
        print(f"[ERROR] Failed to apply {sql_file}: {e}")
        conn.rollback()
        return False

def main():
    print(f"Current working directory: {Path.cwd()}")

    # Database connection
    conn_params = {
        'host': 'localhost',
        'port': '5432',
        'database': 'necpgame',
        'user': 'postgres',
        'password': 'postgres'
    }

    try:
        conn = psycopg2.connect(**conn_params)
        print("Connected to database")

        # Apply schema migrations first
        script_dir = Path(__file__).parent
        project_root = script_dir.parent.parent  # Go up one more level to project root

        # Schema migrations (V1_00 to V1_49)
        schema_dir = project_root / 'infrastructure' / 'liquibase' / 'schema'
        print(f"Looking for schema migrations in: {schema_dir.absolute()}")
        schema_files = sorted(schema_dir.glob('V*.sql'))
        print(f"Found {len(schema_files)} schema files")

        # Data migrations (V1_50+)
        migrations_dir = project_root / 'infrastructure' / 'liquibase' / 'migrations'
        print(f"Looking for data migrations in: {migrations_dir.absolute()}")
        migration_files = sorted(migrations_dir.glob('V*.sql'))
        print(f"Found {len(migration_files)} data migration files")

        sql_files = schema_files + migration_files
        print(f"Total SQL files to apply: {len(sql_files)}")

        if not sql_files:
            print("No SQL migration files found")
            return

        print(f"Found {len(sql_files)} SQL migration files")

        # Apply each migration
        applied = 0
        failed = 0

        for sql_file in sql_files:
            if apply_sql_migration(conn, sql_file):
                applied += 1
            else:
                failed += 1

        print("\nMigration summary:")
        print(f"Applied: {applied}")
        print(f"Failed: {failed}")

    except Exception as e:
        print(f"Database connection failed: {e}")

    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == '__main__':
    main()
