#!/usr/bin/env python3
"""
Check if quest_definitions table exists and show migration status
"""

import psycopg2

def check_quest_table():
    """Check quest_definitions table existence and content"""
    conn = psycopg2.connect(
        host='localhost',
        port=5432,
        dbname='necpgame',
        user='postgres',
        password='postgres'
    )

    cursor = conn.cursor()

    # Check if table exists
    cursor.execute("""
        SELECT EXISTS (
            SELECT FROM information_schema.tables
            WHERE table_schema = 'gameplay'
            AND table_name = 'quest_definitions'
        );
    """)

    table_exists = cursor.fetchone()[0]

    if table_exists:
        # Check for quest engine optimization tables
        cursor.execute("""
            SELECT
                (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'gameplay' AND table_name = 'player_quest_progress') as progress_table,
                (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'gameplay' AND table_name = 'quest_completion_stats') as stats_table,
                (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'gameplay' AND table_name = 'player_quest_rewards') as rewards_table
        """)

        progress_exists, stats_exists, rewards_exists = cursor.fetchone()

        if progress_exists and stats_exists and rewards_exists:
            print("[OK] Quest engine optimization tables exist")
        else:
            print("[WARNING] Some quest engine optimization tables missing:")
            if not progress_exists:
                print("  - player_quest_progress table missing")
            if not stats_exists:
                print("  - quest_completion_stats table missing")
            if not rewards_exists:
                print("  - player_quest_rewards table missing")
        print("[OK] Table gameplay.quest_definitions exists")

        # Count total quests
        cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
        total_quests = cursor.fetchone()[0]
        print(f"[OK] Total quests in database: {total_quests}")

        # Show recent quests
        cursor.execute("""
            SELECT title, status, level_min, level_max
            FROM gameplay.quest_definitions
            ORDER BY created_at DESC
            LIMIT 10
        """)

        print("\nRecent quests:")
        for row in cursor.fetchall():
            print(f"  - {row[0]} [{row[1]}] Lv.{row[2]}-{row[3]}")

    else:
        print("[ERROR] Table gameplay.quest_definitions does not exist")
        print("Schema migration V1_50__content_quest_definitions_table.sql needs to be applied")

    cursor.close()
    conn.close()

if __name__ == "__main__":
    check_quest_table()
