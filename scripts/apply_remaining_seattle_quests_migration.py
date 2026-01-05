#!/usr/bin/env python3
"""
Apply remaining Seattle 2020-2029 quests migration to database
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
        migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_2020_2029_quests.sql")

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
            # Check total count in quest_definitions table
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
            total_count = cur.fetchone()[0]
            print(f"[RESULT] Total quests in quest_definitions table: {total_count}")

            # Check Seattle quests specifically
            try:
                cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE id LIKE 'canon-quest-seattle-%'")
                seattle_count = cur.fetchone()[0]
                print(f"[RESULT] Found {seattle_count} Seattle quests in quest_definitions table")

                # Show quest details
                cur.execute("SELECT id, quest_id, title FROM gameplay.quest_definitions WHERE id LIKE 'canon-quest-seattle-%' ORDER BY quest_id")
                quests = cur.fetchall()
                print("[IMPORTED QUESTS]:")
                for quest in quests:
                    print(f"  - {quest[1]}: {quest[2]}")

            except Exception as query_error:
                print(f"[QUERY ERROR] {query_error}")

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
