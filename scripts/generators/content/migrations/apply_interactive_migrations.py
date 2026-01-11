#!/usr/bin/env python3
"""
Apply interactive objects migrations to database
"""

import os
import sys
import psycopg2
import yaml
from pathlib import Path

def apply_yaml_migration(conn, yaml_file):
    """Apply a single YAML migration"""
    try:
        with open(yaml_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        changeset = data['databaseChangeLog'][0]['changeSet']
        insert_data = changeset['changes'][0]['insert']

        table_name = insert_data['tableName']
        columns_data = insert_data['columns']

        # Build INSERT statement
        columns = [col['column']['name'] for col in columns_data]
        values = [col['column']['value'] for col in columns_data]

        placeholders = ', '.join(['%s'] * len(values))
        columns_str = ', '.join(columns)

        sql = f"INSERT INTO {table_name} ({columns_str}) VALUES ({placeholders})"

        with conn.cursor() as cursor:
            cursor.execute(sql, values)
            conn.commit()

        print(f"[OK] Applied migration: {yaml_file}")
        return True

    except Exception as e:
        print(f"[ERROR] Failed to apply {yaml_file}: {e}")
        conn.rollback()
        return False

def main():
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

        # Find all interactive migration files
        migrations_dir = Path(__file__).parent.parent.parent / 'infrastructure' / 'liquibase' / 'migrations' / 'knowledge' / 'interactives'

        if not migrations_dir.exists():
            print(f"Migrations directory not found: {migrations_dir}")
            return

        yaml_files = list(migrations_dir.glob('*.yaml'))
        print(f"Found {len(yaml_files)} YAML migration files")

        if not yaml_files:
            print("No YAML migration files found")
            return

        # Apply each migration
        applied = 0
        failed = 0

        for yaml_file in sorted(yaml_files):
            if apply_yaml_migration(conn, yaml_file):
                applied += 1
            else:
                failed += 1

        print(f"Applied: {applied}, Failed: {failed}")

    except Exception as e:
        print(f"Database error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == '__main__':
    main()
