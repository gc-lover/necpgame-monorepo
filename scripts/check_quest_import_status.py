#!/usr/bin/env python3
"""
Check status of Seattle quest imports
Issue: #2273
"""

import psycopg2

def check_import_status():
    """Check the status of Seattle quest imports"""
    try:
        conn = psycopg2.connect(
            host="localhost",
            port="5432",
            database="necpgame",
            user="postgres",
            password="postgres"
        )

        cur = conn.cursor()

        # Check total count
        cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
        total = cur.fetchone()[0]
        print(f"Total quests in database: {total}")

        # Check Seattle quests 001-010
        cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-001-%' OR quest_id LIKE 'quest-002-%' OR quest_id LIKE 'quest-003-%' OR quest_id LIKE 'quest-004-%' OR quest_id LIKE 'quest-005-%' OR quest_id LIKE 'quest-006-%' OR quest_id LIKE 'quest-007-%' OR quest_id LIKE 'quest-008-%' OR quest_id LIKE 'quest-009-%' OR quest_id LIKE 'quest-010-%'")
        count_001_010 = cur.fetchone()[0]
        print(f"Seattle quests 001-010: {count_001_010}")

        # Check Seattle quests 016-039
        cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-016-%' OR quest_id LIKE 'quest-017-%' OR quest_id LIKE 'quest-018-%' OR quest_id LIKE 'quest-019-%' OR quest_id LIKE 'quest-02%' OR quest_id LIKE 'quest-03%'")
        count_016_039 = cur.fetchone()[0]
        print(f"Seattle quests 016-039: {count_016_039}")

        # Show some examples
        cur.execute("SELECT quest_id, title FROM gameplay.quest_definitions WHERE quest_id LIKE 'quest-001-%' OR quest_id LIKE 'quest-016-%' OR quest_id LIKE 'quest-020-%' ORDER BY quest_id LIMIT 10")
        examples = cur.fetchall()
        print("\nExamples of imported quests:")
        for quest in examples:
            print(f"  - {quest[0]}: {quest[1][:50]}...")

        conn.close()

        # Summary
        print("\n=== IMPORT STATUS SUMMARY ===")
        print(f"Quests 001-010: {count_001_010}/10 imported")
        print(f"Quests 016-039: {count_016_039}/24 imported")
        total_expected = 10 + 24  # 001-010 + 016-039
        total_imported = count_001_010 + count_016_039
        print(f"Total expected: {total_expected}, Total imported: {total_imported}")

        if total_imported >= total_expected:
            print("[SUCCESS] All required quests have been imported!")
            return True
        else:
            print(f"[INCOMPLETE] Missing {total_expected - total_imported} quests")
            return False

    except Exception as e:
        print(f"Error: {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    check_import_status()