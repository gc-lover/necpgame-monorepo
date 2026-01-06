#!/usr/bin/env python3
"""
Check total count of Seattle quests in database
"""

import psycopg2

def check_quest_count():
    """Check quest counts"""
    try:
        conn = psycopg2.connect(
            host="localhost",
            port="5432",
            database="necpgame",
            user="postgres",
            password="postgres"
        )

        cur = conn.cursor()

        # Total quests
        cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
        total = cur.fetchone()[0]

        # Seattle quests (001-010, 016-039)
        # This covers: 001-010, then 016-039 (skipping 011-015)
        cur.execute("""
            SELECT COUNT(*) FROM gameplay.quest_definitions
            WHERE quest_id ~ '^quest-(00[1-9]|010|01[6-9]|0[2-3][0-9]|03[0-9])'
        """)
        seattle_count = cur.fetchone()[0]

        print(f"Total quests in database: {total}")
        print(f"Seattle quests (001-010, 016-039): {seattle_count}")

        # Show some Seattle quest IDs for verification
        cur.execute("""
            SELECT quest_id FROM gameplay.quest_definitions
            WHERE quest_id ~ '^quest-(00[1-9]|010|01[6-9]|0[2-3][0-9]|03[0-9])'
            ORDER BY quest_id
        """)
        seattle_quests = cur.fetchall()
        print("\nSeattle quest IDs:")
        for quest in seattle_quests[:10]:  # Show first 10
            print(f"  {quest[0]}")
        if len(seattle_quests) > 10:
            print(f"  ... and {len(seattle_quests) - 10} more")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    check_quest_count()
