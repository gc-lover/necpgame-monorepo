#!/usr/bin/env python3
"""
Apply Seattle quests V8 migration
"""

import psycopg2

def apply_migration():
    """Apply the V008 Seattle quests migration"""

    # Database connection
    conn_params = {
        'host': 'localhost',
        'port': '5432',
        'database': 'necpgame',
        'user': 'necpgame',
        'password': 'necpgame_password'
    }

    try:
        conn = psycopg2.connect(**conn_params)
        print("Connected to database")

        # Read and execute migration
        with open('infrastructure/liquibase/migrations/data/quests/V008__import_new_seattle_2020_2029_quests.sql', 'r', encoding='utf-8') as f:
            sql_content = f.read()

        with conn.cursor() as cursor:
            # Split into statements and execute
            statements = [stmt.strip() for stmt in sql_content.split(';') if stmt.strip() and not stmt.strip().startswith('--')]

            for stmt in statements:
                if stmt:
                    print(f"Executing: {stmt[:80]}...")
                    cursor.execute(stmt)

        conn.commit()
        print("[SUCCESS] Applied Seattle quests V8 migration")

        # Verify import
        with conn.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE location LIKE '%Seattle%'")
            seattle_count = cursor.fetchone()[0]
            print(f"[INFO] Seattle quests in database: {seattle_count}")

            # Check specific new quests
            new_quests = [
                'ai-rights-movement-seattle-2020-2029',
                'space-elevator-sabotage-seattle-2020-2029',
                'underwater-data-center-mystery-seattle-2020-2029',
                'underground-music-revolution-seattle-2020-2029',
                'rainforest-resistance-seattle-2020-2029',
                'rain-city-hackers-seattle-2020-2029',
                'corporate-shadow-wars-seattle-2020-2029',
                'coffee-conspiracy-seattle-2020-2029'
            ]

            imported_count = 0
            for quest_id in new_quests:
                cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE quest_id = %s", (quest_id,))
                if cursor.fetchone()[0] > 0:
                    imported_count += 1
                    print(f"[OK] Imported: {quest_id}")

            print(f"[RESULT] Successfully imported {imported_count}/8 new Seattle quests")

    except Exception as e:
        print(f"[ERROR] Migration failed: {e}")
        if 'conn' in locals():
            conn.rollback()
        return False

    finally:
        if 'conn' in locals():
            conn.close()

    return True

if __name__ == "__main__":
    success = apply_migration()
    exit(0 if success else 1)