#!/usr/bin/env python3
"""
Check difficulty constraint for quest_definitions table
"""

import psycopg2

def main():
    conn = psycopg2.connect(
        host='localhost',
        port='5432',
        database='necpgame',
        user='necpgame',
        password='necpgame_password'
    )

    cur = conn.cursor()

    # Check the check constraint for difficulty
    cur.execute("""
        SELECT conname, pg_get_constraintdef(oid)
        FROM pg_constraint
        WHERE conname LIKE '%difficulty%' AND conrelid = 'gameplay.quest_definitions'::regclass
    """)

    constraints = cur.fetchall()
    print("Difficulty constraints:")
    for constraint in constraints:
        print(f"  {constraint[0]}: {constraint[1]}")

    # Also check enum type if it exists
    cur.execute("""
        SELECT enumtypid, enumlabel
        FROM pg_enum
        WHERE enumtypid = (
            SELECT atttypid
            FROM pg_attribute
            WHERE attrelid = 'gameplay.quest_definitions'::regclass
            AND attname = 'difficulty'
        )
        ORDER BY enumsortorder
    """)

    enums = cur.fetchall()
    if enums:
        print("\nDifficulty enum values:")
        for enum in enums:
            print(f"  {enum[1]}")

    cur.close()
    conn.close()

if __name__ == "__main__":
    main()