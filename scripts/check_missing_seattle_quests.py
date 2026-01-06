#!/usr/bin/env python3
"""
Check which Seattle quests are missing from database
"""

import psycopg2

def check_missing_quests():
    """Check which Seattle quests are missing"""
    try:
        conn = psycopg2.connect(
            host="localhost",
            port="5432",
            database="necpgame",
            user="postgres",
            password="postgres"
        )

        cur = conn.cursor()

        # Check existing quests
        cur.execute("""
            SELECT quest_id FROM gameplay.quest_definitions
            WHERE quest_id LIKE 'quest-004%' OR quest_id LIKE 'quest-006%'
               OR quest_id LIKE 'quest-007%' OR quest_id LIKE 'quest-008%'
               OR quest_id LIKE 'quest-009%'
        """)

        existing = [row[0] for row in cur.fetchall()]
        missing = [
            'quest-004-grunge-music',
            'quest-006-mount-rainier',
            'quest-007-rain-rain-rain',
            'quest-008-boeing-factory',
            'quest-009-seafood-salmon'
        ]

        print("Checking missing Seattle quests:")
        for quest_id in missing:
            if quest_id in existing:
                print(f"EXISTS: {quest_id}")
            else:
                print(f"MISSING: {quest_id}")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    check_missing_quests()
