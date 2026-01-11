#!/usr/bin/env python3
"""
Apply America cities quests migration to database
Issue: #2046 - Backend: Импорт 6 квестов America cities (Miami, Detroit, Mexico City) в базу данных
"""

import yaml
import psycopg2
import os
import uuid
from pathlib import Path

def apply_quest_migration():
    """Apply the America cities quests migration"""

    print("=" * 80)
    print("APPLYING AMERICA CITIES QUESTS MIGRATION")
    print("Issue: #2046 - Backend: Импорт 6 квестов America cities (Miami, Detroit, Mexico City) в базу данных")
    print("=" * 80)

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
        print("[CONNECTED] Database connection established")

        # Load migration file
        migration_file = Path("infrastructure/liquibase/migrations/gameplay/quests/data_quests_america-cities-miami-detroit-mexico-city-2020-2029.yaml")

        if not migration_file.exists():
            print(f"[ERROR] Migration file not found: {migration_file}")
            return False

        print(f"[LOADING] Loading migration file: {migration_file}")

        with open(migration_file, 'r', encoding='utf-8') as f:
            migration_data = yaml.safe_load(f)

        # Extract changesets
        changesets = [item for item in migration_data['databaseChangeLog'] if isinstance(item, dict) and 'changeSet' in item]

        print(f"[INFO] Found {len(changesets)} changesets to apply")

        applied = 0
        failed = 0

        with conn.cursor() as cur:
            for changeset in changesets:
                changeset_data = changeset['changeSet']
                changeset_id = changeset_data['id']
                author = changeset_data['author']

                print(f"[APPLYING] Changeset: {changeset_id} (author: {author})")

                try:
                    # Process each change in the changeset
                    changes = changeset_data['changes']

                    for change in changes:
                        if 'insert' in change:
                            insert_data = change['insert']
                            table_name = insert_data['tableName']
                            columns_data = insert_data['columns']

                            # Build INSERT query
                            column_names = []
                            values = []

                            for col in columns_data:
                                col_data = col['column']
                                column_names.append(col_data['name'])

                                # Special handling for id field - generate UUID
                                if col_data['name'] == 'id':
                                    values.append(str(uuid.uuid4()))
                                # Handle both 'value' and 'valueComputed'
                                elif 'value' in col_data:
                                    values.append(col_data['value'])
                                elif 'valueComputed' in col_data:
                                    # For valueComputed, we need to handle it differently
                                    # For NOW(), we can use current timestamp
                                    if col_data['valueComputed'] == 'NOW()':
                                        values.append('NOW()')
                                    else:
                                        values.append(col_data['valueComputed'])
                                else:
                                    raise ValueError(f"Column {col_data['name']} has neither 'value' nor 'valueComputed'")

                            # Handle valueComputed fields
                            processed_values = []
                            placeholders = []

                            for val in values:
                                if val == 'NOW()':
                                    placeholders.append('NOW()')
                                else:
                                    placeholders.append('%s')
                                    processed_values.append(val)

                            placeholders_str = ', '.join(placeholders)
                            columns_str = ', '.join(column_names)

                            query = f"INSERT INTO {table_name} ({columns_str}) VALUES ({placeholders_str})"

                            print(f"[EXECUTING] INSERT into {table_name} for quest: {processed_values[1] if len(processed_values) > 1 else 'unknown'}")

                            cur.execute(query, processed_values)

                    conn.commit()
                    applied += 1
                    print(f"[SUCCESS] Applied changeset: {changeset_id}")

                except Exception as e:
                    conn.rollback()
                    failed += 1
                    print(f"[ERROR] Failed to apply changeset {changeset_id}: {e}")

        print("\n" + "=" * 80)
        print("MIGRATION SUMMARY:")
        print(f"Applied: {applied}")
        print(f"Failed: {failed}")
        print("=" * 80)

        if failed == 0:
            print("[SUCCESS] All America cities quests imported successfully!")

            # Verify import
            with conn.cursor() as cur:
                cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE id LIKE 'content-world-quests-america%'")
                count = cur.fetchone()[0]
                print(f"[VERIFIED] Imported {count} America cities quests")

            return True
        else:
            print("[ERROR] Some changesets failed to apply")
            return False

    except Exception as e:
        print(f"[ERROR] Migration failed: {e}")
        return False

    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

if __name__ == "__main__":
    success = apply_quest_migration()
    exit(0 if success else 1)