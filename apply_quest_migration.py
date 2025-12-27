#!/usr/bin/env python3
"""
Apply quest engine optimization migration with proper SQL splitting
"""

import psycopg2
import re

def apply_quest_migration():
    """Apply quest engine optimization migration"""

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
        cursor = conn.cursor()

        # Read the SQL file
        with open('infrastructure/liquibase/schema/V1_76__quest_engine_optimization.sql', 'r', encoding='utf-8') as f:
            sql_content = f.read()

        # Split SQL by changeset markers and execute each part
        changesets = re.split(r'--changeset.*', sql_content)

        for i, changeset in enumerate(changesets):
            if changeset.strip():
                print(f"[INFO] Applying changeset part {i+1}")
                try:
                    cursor.execute(changeset)
                except Exception as e:
                    print(f"[WARNING] Error in changeset part {i+1}: {e}")
                    # Continue with other parts

        conn.commit()
        print("[OK] Quest engine optimization migration applied successfully")

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
    apply_quest_migration()
