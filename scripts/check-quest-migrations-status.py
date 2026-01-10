#!/usr/bin/env python3
"""
Check quest_definitions table migrations status
Issue: #2227 - Apply quest_definitions table migrations for quest import
"""

import psycopg2
import os
from pathlib import Path

def check_migration_status():
    """Check which quest-related migrations have been applied"""

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
        print("[CHECKING] Quest definitions migrations status")
        print()

        with conn.cursor() as cur:
            # Check if liquibase tables exist
            cur.execute("""
                SELECT EXISTS (
                    SELECT 1 FROM information_schema.tables
                    WHERE table_schema = 'public'
                    AND table_name = 'databasechangelog'
                )
            """)
            liquibase_exists = cur.fetchone()[0]

            if not liquibase_exists:
                print("[ERROR] Liquibase tracking tables not found!")
                print("Database migrations may not have been applied.")
                return False

            # Check quest_definitions table exists
            cur.execute("""
                SELECT EXISTS (
                    SELECT 1 FROM information_schema.tables
                    WHERE table_schema = 'gameplay'
                    AND table_name = 'quest_definitions'
                )
            """)
            quest_table_exists = cur.fetchone()[0]

            if not quest_table_exists:
                print("[ERROR] quest_definitions table does not exist!")
                print("Base migration V1_50__content_quest_definitions_table.sql not applied.")
                return False

            print("[SUCCESS] quest_definitions table exists")

            # Check applied migrations related to quests
            cur.execute("""
                SELECT filename, dateexecuted, orderexecuted, md5sum
                FROM databasechangelog
                WHERE filename LIKE '%quest%'
                ORDER BY orderexecuted DESC
                LIMIT 20
            """)

            applied_migrations = cur.fetchall()

            if not applied_migrations:
                print("[WARNING] No quest-related migrations found in changelog")
                return False

            print(f"[INFO] Found {len(applied_migrations)} quest-related migrations:")
            for filename, dateexecuted, order, md5sum in applied_migrations:
                print(f"  {order:3d}. {filename}")
                print(f"       Applied: {dateexecuted}, MD5: {md5sum[:8]}...")

            # Check table structure
            print("\n[CHECKING] quest_definitions table structure...")
            cur.execute("""
                SELECT column_name, data_type, is_nullable, column_default
                FROM information_schema.columns
                WHERE table_schema = 'gameplay'
                AND table_name = 'quest_definitions'
                ORDER BY ordinal_position
            """)

            columns = cur.fetchall()
            print(f"Table has {len(columns)} columns:")

            required_columns = [
                'id', 'title', 'description', 'status', 'level_min', 'level_max',
                'rewards', 'objectives', 'metadata', 'created_at', 'updated_at'
            ]

            missing_columns = []
            for col_name, data_type, nullable, default in columns:
                status = "✅"
                if col_name in required_columns:
                    required_columns.remove(col_name)
                else:
                    status = "➕"  # additional column

                nullable_str = "NOT NULL" if nullable == "NO" else "NULL"
                print(f"  {status} {col_name:20} {data_type:15} {nullable_str}")

            if required_columns:
                print(f"\n[ERROR] Missing required columns: {required_columns}")
                return False

            # Check data count
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
            quest_count = cur.fetchone()[0]
            print(f"\n[INFO] Total quests in database: {quest_count}")

            if quest_count == 0:
                print("[WARNING] No quest data found - migrations may be incomplete")
                return False

            # Check recent quests
            cur.execute("""
                SELECT id, title, status, created_at
                FROM gameplay.quest_definitions
                ORDER BY created_at DESC
                LIMIT 5
            """)

            recent_quests = cur.fetchall()
            print(f"\n[INFO] Recent quests ({len(recent_quests)}):")
            for quest_id, title, status, created_at in recent_quests:
                print(f"  {quest_id} | {title[:50]:50} | {status:8} | {created_at}")

            # Check indexes
            cur.execute("""
                SELECT indexname, indexdef
                FROM pg_indexes
                WHERE schemaname = 'gameplay'
                AND tablename = 'quest_definitions'
                ORDER BY indexname
            """)

            indexes = cur.fetchall()
            print(f"\n[INFO] Table indexes ({len(indexes)}):")
            for index_name, index_def in indexes:
                print(f"  {index_name}")

            required_indexes = [
                'idx_quest_definitions_status',
                'idx_quest_definitions_level_range',
                'idx_quest_definitions_rewards_gin',
                'idx_quest_definitions_objectives_gin'
            ]

            missing_indexes = []
            for req_idx in required_indexes:
                if not any(req_idx in idx_name for idx_name, _ in indexes):
                    missing_indexes.append(req_idx)

            if missing_indexes:
                print(f"[WARNING] Missing indexes: {missing_indexes}")

            print("\n[SUCCESS] Quest definitions migrations verification complete")
            print(f"✅ Table exists with {len(columns)} columns")
            print(f"✅ {len(indexes)} indexes present")
            print(f"✅ {quest_count} quests loaded")
            print(f"✅ {len(applied_migrations)} migrations applied")

            return True

    except Exception as e:
        print(f"[ERROR] Database check failed: {e}")
        import traceback
        traceback.print_exc()
        return False

def check_pending_migrations():
    """Check for migration files that may not be applied"""

    print("\n[CHECKING] Pending migration files...")

    migrations_dir = Path("infrastructure/liquibase/migrations")

    # Find quest-related migration files
    quest_migrations = []
    for sql_file in migrations_dir.rglob("*.sql"):
        if 'quest' in sql_file.name.lower():
            quest_migrations.append(sql_file)

    print(f"Found {len(quest_migrations)} quest-related migration files:")

    for migration_file in sorted(quest_migrations, key=lambda x: x.name):
        relative_path = migration_file.relative_to(migrations_dir.parent.parent)
        print(f"  {relative_path}")

    return len(quest_migrations)

if __name__ == "__main__":
    print("=" * 60)
    print("Quest Definitions Migrations Status Check")
    print("Issue: #2227")
    print("=" * 60)

    migration_check = check_migration_status()
    pending_check = check_pending_migrations()

    print("\n" + "=" * 60)
    if migration_check:
        print("RESULT: ✅ Quest definitions migrations are properly applied")
        print("Ready for quest import operations")
        exit(0)
    else:
        print("RESULT: ❌ Quest definitions migrations need attention")
        print("Some migrations may not be applied or table is incomplete")
        exit(1)