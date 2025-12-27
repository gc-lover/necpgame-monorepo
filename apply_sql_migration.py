#!/usr/bin/env python3
"""
Apply SQL migration file directly to PostgreSQL database
"""

import psycopg2
import sys
from pathlib import Path

def apply_sql_migration(sql_file_path):
    """Apply SQL migration to database"""

    if not Path(sql_file_path).exists():
        print(f"[ERROR] SQL file not found: {sql_file_path}")
        return False

    # Database connection
    conn_params = {
        'host': 'localhost',
        'port': 5432,
        'database': 'necpgame',
        'user': 'postgres',
        'password': 'postgres'
    }

    try:
        conn = psycopg2.connect(**conn_params)
        print(f"[INFO] Connected to database: necpgame")

        with conn.cursor() as cursor:
            # Read and execute SQL file
            with open(sql_file_path, 'r', encoding='utf-8') as f:
                sql_content = f.read()

            print(f"[INFO] Executing SQL migration: {sql_file_path}")
            cursor.execute(sql_content)

        conn.commit()
        print(f"[OK] Successfully applied SQL migration: {sql_file_path}")

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
    if len(sys.argv) != 2:
        print("Usage: python apply_sql_migration.py <sql_file>")
        sys.exit(1)

    sql_file = sys.argv[1]
    if not Path(sql_file).exists():
        print(f"File not found: {sql_file}")
        sys.exit(1)

    success = apply_sql_migration(sql_file)
    sys.exit(0 if success else 1)
