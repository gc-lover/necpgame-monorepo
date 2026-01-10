#!/usr/bin/env python3
"""
Check Seattle 2020-2029 quests import status
Issue: #2273
"""

import psycopg2
from pathlib import Path

def check_import_status():
    """Check which Seattle quests are already imported"""

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

        with conn.cursor() as cur:
            # Check quest_definitions table
            print("\n[CHECKING] quest_definitions table...")

            # Count total quests
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
            total_definitions = cur.fetchone()[0]
            print(f"Total quests in quest_definitions: {total_definitions}")

            # Check Seattle quests
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE location LIKE '%Seattle%' OR quest_id LIKE '%seattle%'")
            seattle_definitions = cur.fetchone()[0]
            print(f"Seattle quests in quest_definitions: {seattle_definitions}")

            # Check specific quest IDs from migration file
            migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_2020_2029_quests.sql")
            quest_ids = []

            if migration_file.exists():
                with open(migration_file, 'r', encoding='utf-8') as f:
                    content = f.read()
                    # Extract quest IDs from INSERT statements
                    lines = content.split('\n')
                    for line in lines:
                        if "'quest-" in line and "-seattle" in line or "-pike" in line or "-starbucks" in line:
                            start = line.find("'quest-")
                            end = line.find("'", start + 1)
                            if start != -1 and end != -1:
                                quest_id = line[start+1:end]
                                if quest_id not in quest_ids:
                                    quest_ids.append(quest_id)

            print(f"\n[ANALYZING] Found {len(quest_ids)} quest IDs in migration file")
            print("Quest IDs to import:", quest_ids[:5], "..." if len(quest_ids) > 5 else "")

            # Check which ones are already imported
            if quest_ids:
                placeholders = ','.join(['%s'] * len(quest_ids))
                query = f"SELECT quest_id FROM gameplay.quest_definitions WHERE quest_id IN ({placeholders})"
                cur.execute(query, quest_ids)
                existing_quests = [row[0] for row in cur.fetchall()]

                missing_quests = [qid for qid in quest_ids if qid not in existing_quests]

                print(f"Already imported: {len(existing_quests)} quests")
                print(f"Missing quests: {len(missing_quests)} quests")

                if missing_quests:
                    print("Missing quest IDs:", missing_quests)
                else:
                    print("[SUCCESS] All quests from migration are already imported!")

            # Show recent quests
            print("\n[RECENT QUESTS] Last 10 quests in quest_definitions:")
            cur.execute("SELECT quest_id, title FROM gameplay.quest_definitions ORDER BY created_at DESC LIMIT 10")
            recent_quests = cur.fetchall()
            for quest_id, title in recent_quests:
                print(f"  - {quest_id}: {title[:50]}...")

    except Exception as e:
        print(f"[ERROR] {e}")
        import traceback
        traceback.print_exc()
    finally:
        if 'conn' in locals():
            conn.close()
            print("\n[CLOSED] Database connection closed")

if __name__ == "__main__":
    check_import_status()