#!/usr/bin/env python3
"""
Apply Seattle quests 025-039 migration to database
Issue: #2273
"""

import os
import psycopg2
from pathlib import Path

def apply_migration():
    """Apply the Seattle quests 025-039 migration"""
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
        migration_file = Path("infrastructure/liquibase/migrations/data/quests/V007__import_seattle_quests_025_039.sql")

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

            # Check quests 025-039 specifically
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-025-%' OR quest_id LIKE 'quest-026-%' OR quest_id LIKE 'quest-027-%' OR quest_id LIKE 'quest-028-%' OR quest_id LIKE 'quest-029-%' OR quest_id LIKE 'quest-03%'")
            quests_25_39_count = cur.fetchone()[0]
            print(f"[RESULT] Found {quests_25_39_count} quests 025-039 in quest_definitions table")

            # Show quest details for 025-039
            cur.execute("SELECT quest_id, title FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-025-%' OR quest_id LIKE 'quest-026-%' OR quest_id LIKE 'quest-027-%' OR quest_id LIKE 'quest-028-%' OR quest_id LIKE 'quest-029-%' OR quest_id LIKE 'quest-03%' ORDER BY quest_id")
            quests = cur.fetchall()
            print("[SEATTLE QUESTS 025-039]:")
            for quest in quests:
                print(f"  - {quest[0]}: {quest[1][:60]}...")

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