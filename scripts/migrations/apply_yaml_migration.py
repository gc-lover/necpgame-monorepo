#!/usr/bin/env python3
"""
Script to apply YAML Liquibase migrations directly to PostgreSQL
"""

import psycopg2
import yaml
import json
from pathlib import Path

def apply_yaml_migration(yaml_file_path):
    """Apply YAML migration to database"""

    # Load YAML migration
    with open(yaml_file_path, 'r', encoding='utf-8') as f:
        migration_data = yaml.safe_load(f)

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

        with conn.cursor() as cursor:
            # Process each changeSet
            for change_set in migration_data['databaseChangeLog']:
                if 'changeSet' in change_set:
                    changeset = change_set['changeSet']
                    changeset_id = changeset['id']
                    author = changeset['author']

                    print(f"Processing changeset: {changeset_id} by {author}")

                    # Process each change
                    for change in changeset['changes']:
                        if 'insert' in change:
                            insert_data = change['insert']
                            table_name = insert_data['tableName']

                            # Build INSERT statement
                            columns = [col['column']['name'] for col in insert_data['columns']]
                            values = [col['column']['value'] for col in insert_data['columns']]

                            placeholders = ', '.join(['%s'] * len(values))
                            columns_str = ', '.join(columns)

                            sql = f"INSERT INTO {table_name} ({columns_str}) VALUES ({placeholders})"

                            print(f"Executing: INSERT INTO {table_name}")
                            cursor.execute(sql, values)

        conn.commit()
        print(f"[OK] Successfully applied YAML migration: {yaml_file_path}")

    except Exception as e:
        print(f"[ERROR] Failed to apply migration: {e}")
        if 'conn' in locals():
            conn.rollback()
        return False

    finally:
        if 'conn' in locals():
            conn.close()

    return True

if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("Usage: python apply_yaml_migration.py <yaml_file>")
        sys.exit(1)

    yaml_file = sys.argv[1]
    if not Path(yaml_file).exists():
        print(f"File not found: {yaml_file}")
        sys.exit(1)

    success = apply_yaml_migration(yaml_file)
    sys.exit(0 if success else 1)
