#!/usr/bin/env python3
"""
Apply Seattle quests 001-010 migration to database
Issue: #2273
"""

import os
import psycopg2
from pathlib import Path

def apply_migration():
    """Apply the Seattle quests 001-010 migration"""
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

            # Check quests 001-010 specifically
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-001-%' OR quest_id LIKE 'quest-002-%' OR quest_id LIKE 'quest-003-%' OR quest_id LIKE 'quest-004-%' OR quest_id LIKE 'quest-005-%' OR quest_id LIKE 'quest-006-%' OR quest_id LIKE 'quest-007-%' OR quest_id LIKE 'quest-008-%' OR quest_id LIKE 'quest-009-%' OR quest_id LIKE 'quest-010-%'")
            quests_001_010_count = cur.fetchone()[0]
            print(f"[RESULT] Found {quests_001_010_count} quests 001-010 in quest_definitions table")

            # Show quest details for 001-010
            cur.execute("SELECT quest_id, title FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-001-%' OR quest_id LIKE 'quest-002-%' OR quest_id LIKE 'quest-003-%' OR quest_id LIKE 'quest-004-%' OR quest_id LIKE 'quest-005-%' OR quest_id LIKE 'quest-006-%' OR quest_id LIKE 'quest-007-%' OR quest_id LIKE 'quest-008-%' OR quest_id LIKE 'quest-009-%' OR quest_id LIKE 'quest-010-%' ORDER BY quest_id")
            quests = cur.fetchall()
            print("[SEATTLE QUESTS 001-010]:")
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