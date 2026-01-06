#!/usr/bin/env python3
"""
Apply remaining Seattle quests migration (016-039) to database
Issue: #2273
"""

import os
import psycopg2
from pathlib import Path

def apply_migration():
    """Apply the remaining Seattle quests migration"""
    try:
        # Connect to database
        conn = psycopg2.connect(
            host="localhost",
            port="5432",
            database="necpgame",
            user="postgres",
            password="postgres"
        )

        print("[CONNECTED] Database connection established")

        # Read migration file
        migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_quests.sql")

        if not migration_file.exists():
            print(f"[ERROR] Migration file not found: {migration_file}")
            return

        print(f"[READING] Migration file: {migration_file}")

        with open(migration_file, 'r', encoding='utf-8') as f:
            sql_content = f.read()

        # Execute migration
        print("[APPLYING] Executing migration...")

        with conn.cursor() as cur:
            try:
                cur.execute(sql_content)
                conn.commit()
                print("[SUCCESS] Migration applied successfully!")
            except Exception as exec_error:
                print(f"[EXECUTION ERROR] Failed to execute migration: {exec_error}")
                conn.rollback()
                return

        # Verify quests were inserted
        print("[VERIFYING] Checking imported quests...")
        with conn.cursor() as cur:
            # Check total count
            cur.execute("SELECT COUNT(*) FROM gameplay.quests")
            total_count = cur.fetchone()[0]
            print(f"[RESULT] Total quests in quests table: {total_count}")

            # Check Seattle quests
            cur.execute("SELECT COUNT(*) FROM gameplay.quests WHERE metadata::text LIKE '%seattle%'")
            seattle_count = cur.fetchone()[0]
            print(f"[RESULT] Seattle quests in quests table: {seattle_count}")

            # Show some quest titles
            cur.execute("SELECT title FROM gameplay.quests WHERE metadata::text LIKE '%seattle%' ORDER BY title LIMIT 5")
            quests = cur.fetchall()
            print("[SAMPLE SEATTLE QUESTS]:")
            for quest in quests:
                print(f"  - {quest[0][:50]}...")

    except Exception as e:
        print(f"[ERROR] Migration failed: {e}")
        import traceback
        traceback.print_exc()
    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

if __name__ == "__main__":
    apply_migration()
