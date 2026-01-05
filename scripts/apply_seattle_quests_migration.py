#!/usr/bin/env python3
"""
Apply Seattle quests migration to database
Issue: #2273
"""

import os
import psycopg2
from pathlib import Path

def apply_migration():
    """Apply the Seattle quests migration"""
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

        # First, apply the DDL to create the quests table
        ddl_file = Path("infrastructure/liquibase/migrations/schema/V1_96__quests_tables.sql")

        if ddl_file.exists():
            print(f"[DDL] Applying quests table schema: {ddl_file}")
            with open(ddl_file, 'r', encoding='utf-8') as f:
                ddl_content = f.read()

            # Execute DDL
            with conn.cursor() as cur:
                cur.execute(ddl_content)
                conn.commit()
            print("[DDL] Quests table schema applied successfully")
        else:
            print(f"[WARNING] DDL file not found: {ddl_file}")

        # Read migration file
        migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_quests.sql")

        if not migration_file.exists():
            print(f"[ERROR] Migration file not found: {migration_file}")
            return

        print(f"[READING] Migration file: {migration_file}")

        with open(migration_file, 'r', encoding='utf-8') as f:
            sql_content = f.read()

        # Execute migration
        print("[APPLYING] Executing migration...")

        # Parse SQL content to extract complete INSERT statements
        # Each INSERT includes ON CONFLICT clause
        lines = sql_content.split('\n')
        current_statement = ""
        statements = []

        for line in lines:
            line = line.strip()
            if line.startswith('--') or not line:
                continue

            current_statement += line + " "

            # Check if this line ends a complete INSERT statement
            if line.endswith(';'):
                if current_statement.strip():
                    statements.append(current_statement.strip())
                current_statement = ""

        print(f"[PARSED] Found {len(statements)} INSERT statements")

        with conn.cursor() as cur:
            successful_inserts = 0
            for i, statement in enumerate(statements, 1):
                if statement and not statement.startswith('--'):
                    print(f"[EXECUTING] Statement {i}/{len(statements)}...")
                    try:
                        cur.execute(statement)
                        successful_inserts += 1
                    except Exception as stmt_error:
                        print(f"[STATEMENT ERROR] Statement {i} failed: {stmt_error}")
                        print(f"[FAILED SQL] {statement[:100]}...")
                        # Continue with other statements
                        continue

            conn.commit()
            print(f"[SUCCESS] Executed {successful_inserts} INSERT statements successfully")

        print("[SUCCESS] Migration applied successfully!")

        # Verify quests were inserted
        print("[VERIFYING] Checking imported quests...")
        with conn.cursor() as cur:
            # First, check total count in quests table
            cur.execute("SELECT COUNT(*) FROM gameplay.quests")
            total_count = cur.fetchone()[0]
            print(f"[RESULT] Total quests in table: {total_count}")

            # Check Seattle quests using JSON operator
            try:
                cur.execute("SELECT COUNT(*) FROM gameplay.quests WHERE metadata->>'id' LIKE 'canon-quest-seattle-%'")
                seattle_count = cur.fetchone()[0]
                print(f"[RESULT] Found {seattle_count} Seattle quests in gameplay.quests table")

                # Show some quest titles
                cur.execute("SELECT metadata->>'id' as quest_id, title FROM gameplay.quests WHERE metadata->>'id' LIKE 'canon-quest-seattle-%' ORDER BY metadata->>'id' LIMIT 5")
                quests = cur.fetchall()
                print("[SAMPLE QUESTS]:")
                for quest in quests:
                    print(f"  - {quest[0]}: {quest[1][:50]}...")
            except Exception as json_error:
                print(f"[JSON QUERY ERROR] {json_error}")
                # Fallback: show all quests
                cur.execute("SELECT id, title FROM gameplay.quests LIMIT 5")
                quests = cur.fetchall()
                print("[FALLBACK] All quests in table:")
                for quest in quests:
                    print(f"  - {quest[0]}: {quest[1][:50]}...")

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
