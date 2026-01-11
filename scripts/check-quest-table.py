#!/usr/bin/env python3
"""
Check quest_definitions table structure
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
    cur.execute("""
        SELECT column_name, data_type, is_nullable
        FROM information_schema.columns
        WHERE table_schema = 'gameplay'
        AND table_name = 'quest_definitions'
        ORDER BY ordinal_position
    """)

    columns = cur.fetchall()
    print("quest_definitions table structure:")
    for col in columns:
        print(f"  {col[0]}: {col[1]} ({'NOT NULL' if col[2] == 'NO' else 'NULL'})")

    cur.close()
    conn.close()

if __name__ == "__main__":
    main()