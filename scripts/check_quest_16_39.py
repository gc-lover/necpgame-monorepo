#!/usr/bin/env python3
"""
Check quests 016-039 in quest_definitions table
"""

import psycopg2

def check_quests_16_39():
    """Check quests 016-039"""
    try:
        conn = psycopg2.connect(
            host="localhost",
            port="5432",
            database="necpgame",
            user="postgres",
            password="postgres"
        )

        cur = conn.cursor()

        # Check quests 016-039 in quest_definitions
        cur.execute("""
            SELECT quest_id, title FROM gameplay.quest_definitions
            WHERE quest_id ~ '^quest-(01[6-9]|0[2-3][0-9]|03[0-9])'
            ORDER BY quest_id
        """)
        rows = cur.fetchall()
        existing_16_39 = [row[0] for row in rows]

        print(f"Existing quests 016-039 in quest_definitions: {len(existing_16_39)}")
        for quest_id, title in rows:
            print(f"  {quest_id}: {title[:50]}...")

        # Check all Seattle-related quests in quest_definitions
        cur.execute("""
            SELECT COUNT(*) FROM gameplay.quest_definitions
            WHERE quest_id LIKE '%seattle%' OR description LIKE '%Сиэтл%' OR location LIKE '%Seattle%'
        """)
        seattle_defs_count = cur.fetchone()[0]
        print(f"\nTotal Seattle-related quests in quest_definitions: {seattle_defs_count}")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    check_quests_16_39()
