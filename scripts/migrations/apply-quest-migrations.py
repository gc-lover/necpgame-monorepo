#!/usr/bin/env python3
"""
Apply quest_definitions table migrations for quest import
Issue: #2227 - Database migrations for quest definitions
"""

import os
import sys
import psycopg2
from pathlib import Path
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

def get_db_connection():
    """Get database connection"""
    return psycopg2.connect(
        host=os.getenv('DB_HOST', 'localhost'),
        port=os.getenv('DB_PORT', '5432'),
        database=os.getenv('DB_NAME', 'necpgame'),
        user=os.getenv('DB_USER', 'postgres'),
        password=os.getenv('DB_PASSWORD', 'postgres')
    )

def check_migration_applied(conn, migration_version):
    """Check if migration has been applied"""
    try:
        with conn.cursor() as cur:
            cur.execute("""
                SELECT COUNT(*) FROM public.databasechangelog
                WHERE filename LIKE %s
            """, (f'%{migration_version}%',))
            count = cur.fetchone()[0]
            return count > 0
    except Exception as e:
        print(f"Warning: Could not check migration status: {e}")
        return False

def apply_migration(conn, migration_file):
    """Apply a single migration"""
    try:
        print(f"[APPLYING] {migration_file}")

        with open(migration_file, 'r', encoding='utf-8') as f:
            sql_content = f.read()

        # Split SQL into statements
        statements = [stmt.strip() for stmt in sql_content.split(';') if stmt.strip()]

        with conn.cursor() as cur:
            for stmt in statements:
                if stmt:
                    print(f"[EXECUTING] {stmt[:100]}...")
                    cur.execute(stmt)

        conn.commit()
        print(f"[SUCCESS] Applied migration: {migration_file}")

    except Exception as e:
        conn.rollback()
        print(f"[ERROR] Failed to apply migration {migration_file}: {e}")
        raise

def main():
    print("=" * 60)
    print("QUEST DEFINITIONS MIGRATION APPLIER")
    print("Issue: #2227 - Database migrations for quest definitions")
    print("=" * 60)

    # Get migration file path
    migration_file = Path(__file__).parent.parent.parent / "infrastructure" / "liquibase" / "migrations" / "schema" / "V1_95__quest_definitions_tables.sql"

    if not migration_file.exists():
        print(f"[ERROR] Migration file not found: {migration_file}")
        sys.exit(1)

    try:
        # Connect to database
        print("[CONNECTING] Connecting to database...")
        conn = get_db_connection()
        print("[CONNECTED] Database connection established")

        # Check if migration already applied
        migration_name = "V1_95__quest_definitions_tables.sql"
        if check_migration_applied(conn, migration_name):
            print(f"[SKIP] Migration already applied: {migration_name}")
        else:
            # Apply migration
            apply_migration(conn, migration_file)

        # Verify quest_definitions table exists
        print("[VERIFYING] Checking quest_definitions table...")
        with conn.cursor() as cur:
            cur.execute("""
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = 'gameplay'
                AND table_name = 'quest_definitions'
            """)
            count = cur.fetchone()[0]

            if count > 0:
                print("[SUCCESS] quest_definitions table exists")

                # Get table info
                cur.execute("""
                    SELECT column_name, data_type, is_nullable
                    FROM information_schema.columns
                    WHERE table_schema = 'gameplay'
                    AND table_name = 'quest_definitions'
                    ORDER BY ordinal_position
                """)
                columns = cur.fetchall()
                print(f"[INFO] Table has {len(columns)} columns:")
                for col in columns[:5]:  # Show first 5 columns
                    print(f"  - {col[0]}: {col[1]} ({'NOT NULL' if col[2] == 'NO' else 'NULL'})")
                if len(columns) > 5:
                    print(f"  ... and {len(columns) - 5} more columns")

            else:
                print("[ERROR] quest_definitions table does not exist!")
                sys.exit(1)

        print("[COMPLETE] Quest definitions migration applied successfully!")

    except Exception as e:
        print(f"[ERROR] Migration failed: {e}")
        sys.exit(1)
    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

if __name__ == "__main__":
    main()
